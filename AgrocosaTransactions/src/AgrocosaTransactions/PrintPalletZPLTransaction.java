/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.print.*;
import xmlNodeArray.*;

public class PrintPalletZPLTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUpdatePrinted;

    // Configuración de impresora
    // ======== CONFIGURACIÓN DE IMPRESIÓN ========
    // Si tienes print server o IP, ponla aquí; si no, dejará que falle y hará fallback a USB/spooler.
    //private static final String PRINTER_IP   = "192.168.1.50";   // Cambiar si tu Zebra tiene IP
    //private static final int    PRINTER_PORT = 9100;

    // Nombre EXACTO de la impresora en Windows (Panel de Control → Dispositivos e impresoras)
    private static String PRINTER_NAME = ""; // Cambia al nombre exacto en tu equipo
    
    // Cantidad del lote generado a imprimir
    //private static int BATCH_QUANTITY = 0;

    // Tamaño de etiqueta (en dots, 203 dpi): 4x2 inch → 812 x 406
    private static final int LABEL_WIDTH_DOTS  = 612; // ^PW
    private static final int LABEL_HEIGHT_DOTS = 206; // ^LL

    // Velocidad (ips) y oscuridad (ajusta según tu medio para calidad vs velocidad)
    private static final String ZPL_SPEED     = "^PR4\n";  // 3–4 suele ir bien en GC420t
    private static final String ZPL_DARKNESS  = "^MD15\n"; // 0–30 aprox. Ajusta

    public PrintPalletZPLTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.SaveType);
    }

    @Override
    public synchronized boolean PrepareStatements() {
        try {
            Connection con = this.GetSIDDataBase().GetConnection();

            pstmtSelect = con.prepareStatement(
                "SELECT p.PalletId, p.ProductName " +
                "FROM palletinfo p " +
                "JOIN palletinfofiledetail d ON p.PalletId = d.PalletId " +
                "WHERE d.idPalletInfoFile = (SELECT MAX(idPalletInfoFile) FROM palletinfofile WHERE idUser = ?)"
            );

            pstmtUpdatePrinted = con.prepareStatement(
                "UPDATE palletinfo SET Printed = 1 WHERE PalletId = ?"
            );

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtUpdatePrinted);

            return true;
        } catch (SQLException e) {
            System.out.println("PrintPalletZPLTransaction::PrepareStatements> SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = new xmlNodeArray();
        ResultSet rset = null;
        
        // Localización para la fecha
        Locale esLocale = new Locale("es", "ES");
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", esLocale);
        
        PRINTER_NAME = GetNodeArray().find("printerName").getStringValue();
        //BATCH_QUANTITY = GetNodeArray().find("quantityBatch").getIntValue();
        
        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            int idUser = GetNodeArray().find("idUser").getIntValue();
            
            pstmtSelect.setInt(1, idUser);
            rset = pstmtSelect.executeQuery();
                        
            StringBuilder batch = new StringBuilder();
            int count = 0;

            while (rset.next()) {
                String palletId = rset.getString("PalletId");
                String product  = rset.getString("ProductName");
                String fecha    = formatter.format(new Date());

                batch.append(generarZPL_Fast(palletId, product, fecha));
                count++;
                
                // Marca como Printed=1 todo lo que se imprimió
                pstmtUpdatePrinted.setString(1, rset.getString("PalletId"));
                pstmtUpdatePrinted.executeUpdate();
            }

            if (count == 0) {
                System.out.println("⚠️ [ZPL] No hay pallets recientes para imprimir (idUser=" + idUser);
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "No hay pallets recientes para imprimir.");
                resultArray.add("RESPONSE_DETAIL", "");
                return resultArray;
            }

            // Envío en UN SOLO JOB: más rápido
            //boolean impresoPorIP = false;
            /*try {
                enviarAZebraIP(batch.toString());
                System.out.println("✅ [ZPL] Impreso por IP en un solo job. Etiquetas: " + count);
                impresoPorIP = true;
            } catch (Exception ex) {
                System.out.println("⚠️ [ZPL] Falla IP: " + ex.getMessage() + " → Intentando USB/spooler…");
                enviarAZebraUSB(batch.toString(), PRINTER_NAME, BATCH_QUANTITY);
                System.out.println("✅ [ZPL] Impreso por USB/spooler en un solo job. Etiquetas: " + count);
            }*/
            
            //enviarAZebraUSB(batch.toString(), PRINTER_NAME, BATCH_QUANTITY);
            
            // Marca como Printed=1 todo lo que se imprimió
            // Re-corremos para marcar; volvemos a ejecutar el SELECT (o guarda IDs en una lista si prefieres)
            /*rset.beforeFirst(); // para reutilizar rset; si el driver no lo soporta, haz SELECT nuevamente
            while (rset.next()) {
                pstmtUpdatePrinted.setString(1, rset.getString("PalletId"));
                pstmtUpdatePrinted.executeUpdate();
            }*/
            
            conn.commit();
            /*resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", "Etiquetas enviadas a Zebra " + (impresoPorIP ? "por IP" : "por USB") + " exitosamente.");
            resultArray.add("RESPONSE_DETAIL", "Total: " + count);*/
            
            // Aquí en vez de enviar a la impresora, lo devolvemos
            //resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("ZPL_DATA", batch.toString());
            //resultArray.add("RESPONSE_MESSAGE", "ZPL generado y pallets marcados como impresos");
            return resultArray;
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception: " + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } finally {
            if (rset != null) rset.close();
            CloseStatements();
        }
    }

    // ======== ZPL generacion de etiqueta ========
    private String generarZPL_Fast(String palletId, String product, String fecha) {
        // Esta configuracion ZPL es solamente para la impresora Zebra GC420T (De etiqueta Pequeña)
        return new StringBuilder()
            .append("^XA\n")
            .append("^PW").append(LABEL_WIDTH_DOTS).append("\n")
            .append("^LL").append(LABEL_HEIGHT_DOTS).append("\n")
            .append("^MMT\n")               // Tear-off (no se detiene por etiqueta)
            .append(ZPL_SPEED)              // Velocidad
            .append(ZPL_DARKNESS)           // Oscuridad
             // ===== Encabezado: Agrocosa / A Gomez Farm Company =====
            .append("^CF0,30\n") // Fuente 0, tamaño 30
            .append("^FO0,70^FB")
            .append(LABEL_WIDTH_DOTS - 12)
            .append(",1,0,C,0^FDAgrocosa^FS\n")
            .append("^CF0,25\n") // Fuente un poco más pequeña
            .append("^FO0,120^FB")
            .append(LABEL_WIDTH_DOTS - 12)
            .append(",1,0,C,0^FDA Gomez Farm Company^FS\n")
            // Código de barras
            .append("^BY2\n")
            .append("^FO100,170^BCN,80,Y,N,N\n") // 200 = coordenada X
            .append("^FD").append(palletId).append("^FS\n")
            // Producto centrado
            .append("^CF0,35\n")
            .append("^FO0,290^FB")
            .append(LABEL_WIDTH_DOTS - 12)
            .append(",1,0,C,0^FD").append(product).append("^FS\n")
            // Fecha a la derecha
            .append("^CF0,25\n")
            .append("^FO400,340^FD").append(fecha).append("^FS\n")
            .append("^XZ\n")
            .toString();
    }

    // ======== Envío IP (Raw 9100) ========
    /*private void enviarAZebraIP(String zplBatch) throws Exception {
        try (Socket client = new Socket(PRINTER_IP, PRINTER_PORT);
                // OutputStream out = client.getOutputStream()) {
             OutputStream out = new java.io.BufferedOutputStream(client.getOutputStream())) {
            out.write(zplBatch.getBytes("UTF-8"));
            out.flush();
        }
    }*/
    
    // ======== Envío USB (Windows Spooler) ========
    /*private void enviarAZebraUSB(String zplBatch, String printerName, int quantity) throws Exception {
        PrintService target = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService ps : services) {
            System.out.println("🔎 Impresora detectada: " + ps.getName());
            if (ps.getName().equalsIgnoreCase(printerName)) {
                target = ps;
                break;
            }
        }
        if (target == null) throw new Exception("Impresora no encontrada: " + printerName);
        
        int count = 1;
        while(count <= quantity){
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; // RAW
            DocPrintJob job  = target.createPrintJob();
            Doc doc          = new SimpleDoc(zplBatch.getBytes("UTF-8"), flavor, null);
            job.print(doc, null);
            count++;
        }
    }*/

    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.PrintPalletZPLTransaction");
        nodeArr.add("idUser", "1");
        return nodeArr;
    }

    @Override
    public boolean Supports(xmlNodeArray na) {
        return true;
    }
}

