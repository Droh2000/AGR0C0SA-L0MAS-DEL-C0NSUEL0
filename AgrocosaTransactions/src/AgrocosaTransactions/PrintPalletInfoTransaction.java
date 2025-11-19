package AgrocosaTransactions;

import SIDWebEngine.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import xmlNodeArray.*;
import java.util.Date;

/**
 *
 * @author Juan
 */
public class PrintPalletInfoTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtSloc;
    //protected PreparedStatement pstmtUpdatePrinted;
    protected PreparedStatement pstmtInsertFileName;
    protected PreparedStatement pstmtGetIDFileName;
    protected PreparedStatement pstmtInsertInfoFileDetail;

    private static final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 21, Font.BOLD);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font tableFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String FILEPATH = "";

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public PrintPalletInfoTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.SaveType);
    }

    /**
     * Checks to see if the node Array is supported
     *
     * @param nodeArray
     * @return <B>true</B> if the nodeArray is supported. <B>false</B> otherwise
     * @exception (none)
     */
    @Override
    public boolean Supports(xmlNodeArray nodeArray) {

        //Add tag names as comma separated Strings to the mandatoryTags array
        String[] mandatoryTags = {};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<PrintPalletInfoTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<PrintPalletInfoTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<PrintPalletInfoTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<PrintPalletInfoTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
                errArray.add("OPTIONAL_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        return true;
    }

    /**
     * Prepares the SQL statements to be executed
     *
     * @return <B>true</B> for successful preparation;
     * <B>false</B> for unsuccessful preparation
     * @exception (none)
     */
    @Override
    public synchronized boolean PrepareStatements() {
        //Note1 : Use PreparedStatements instead of Statements where ever possible
        //Note2 : If transaction contains no prepared statements, delete entire function
        //        Unless there are nested transaction, then Prepare will call those.
        try {
            Connection con = this.GetSIDDataBase().GetConnection();
            pstmtSelect = con.prepareStatement("SELECT "
                    + "idPalletInfo, "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "CampName "
                    + "FROM palletinfo "
                    + "Where Ship = 0 "
                    + "and Printed = 0 "
                    + "and Active = 1 "
                    + "Order by PalletId");

            pstmtSloc = con.prepareStatement("SELECT "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName, "
                    + "sum(inventory.Quantity)  as Qty "
                    + "FROM inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubstorage = inventory.idSubstorage "
                    + "where PalletId in ( "
                    + "SELECT "
                    + "PalletId "
                    + "FROM palletinfo  "
                    + "Where Ship = 0 "
                    + "and Printed = 0 "
                    + ") "
                    + "Group by "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName");

            /*pstmtUpdatePrinted = con.prepareStatement("update palletinfo set "
                    + "Printed = 1 "
                    + "where Printed = 0 "
                    + "and ship = 0");*/

            pstmtInsertFileName = con.prepareStatement("insert into palletinfofile ("
                    + "FileName, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active, "
                    + "Printed, "
                    + "File "
                    + ") values("
                    + "?, "//1 FileName
                    + "?, "//2 idUser
                    + "Now(), "
                    + "Now(), "
                    + "1, "
                    + "0, "
                    + "?  "//3 File Content
                    + ")");
            
            pstmtGetIDFileName = con.prepareStatement("SELECT LAST_INSERT_ID()");
            
            pstmtInsertInfoFileDetail = con.prepareStatement("insert into palletinfofiledetail ("
                    + "idPalletInfoFile, "
                    + "PalletId"
                    + ") values("
                    + "?, "
                    + "?"
                    + ")");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtSloc);
            //this.addPreparedStatement(pstmtUpdatePrinted);
            this.addPreparedStatement(pstmtInsertFileName);
            this.addPreparedStatement(pstmtGetIDFileName);
            this.addPreparedStatement(pstmtInsertInfoFileDetail);

            return true;
        } catch (SQLException e) {
            System.out.println("PrintPalletInfoTransaction::PrepareStatements> SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * executes sql statements using input arguments and returns result
     *
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    @Override
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        HashMap contentMap = null;
        int rowsAffected = 0;
        String storageName = "";
        String substorageName = "";
        int idUser = 0;
        int len = 0;
        File file = null;
        FileInputStream fis = null;
        boolean isFirst;
        int idPalletInfoFile;
        ArrayList<String> palletsId = new ArrayList<>();
        String count = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();

            rset = pstmtSloc.executeQuery();
            if (rset.next()) {
                storageName = rset.getString("StorageName");
                substorageName = rset.getString("SubstorageName");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //temp folder
            FILEPATH = System.getProperty("java.io.tmpdir");
            Date date = new Date();
            String fileName = "AgrocosaPackingId" + sdf.format(date) + ".pdf";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILEPATH + fileName));
            document.open();
            addMetaData(document);

            //pallet info
            rset = pstmtSelect.executeQuery();
            isFirst = true;
            while (rset.next()) {
                if (!isFirst) {
                    document.newPage();  // Solo agregas una nueva página después de la primera
                }
                
                contentMap = new HashMap();
                contentMap.put("PalletId", rset.getString("PalletId"));
                contentMap.put("ProductName", rset.getString("ProductName"));
                /*contentMap.put("Size", rset.getString("Size"));
                contentMap.put("Color", rset.getString("Color"));
                contentMap.put("Quantity", rset.getString("Quantity"));
                contentMap.put("CampName", rset.getString("CampName"));
                contentMap.put("StorageName", storageName);
                contentMap.put("SubstorageName", substorageName);*/
                addContentPage(document, writer, contentMap);
                
                // Guardar los PalletsIDs para insertarlos mas adelante
                palletsId.add(rset.getString("PalletId"));
                
                isFirst = false;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            document.close();

            //update
            //pstmtUpdatePrinted.executeUpdate();

            //insert filename
            file = new File(FILEPATH + fileName);
            fis = new FileInputStream(file);
            len = (int) file.length();

            pstmtInsertFileName.setString(1, fileName);
            pstmtInsertFileName.setInt(2, idUser);
            pstmtInsertFileName.setBinaryStream(3, fis, len);
            
            idPalletInfoFile = -1;
            rowsAffected = pstmtInsertFileName.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmtGetIDFileName.executeQuery();
                
                if (generatedKeys.next()) {
                    idPalletInfoFile = generatedKeys.getInt(1);
                    
                    // Insertar en la tabla para saber el PalletID correspondiente al PDF que se va a generar en este momento                  
                    for (int i = 0; i < palletsId.size(); i++) {
                        pstmtInsertInfoFileDetail.setInt(1, idPalletInfoFile);
                        pstmtInsertInfoFileDetail.setString(2, palletsId.get(i));
                        rowsAffected = pstmtInsertInfoFileDetail.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            count += "1";
                        }else{
                            count += "0";
                        }
                    }
                   
                    resultArray.add("FileGeneratedId", idPalletInfoFile); // << Esto va al JSP
                }
                count += "1";
            } else {
                count += "0";
            }
            
            if (!count.contains("0")) {
                conn.commit();
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "Embarque registrado exitosamente.");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.rollback();
                resultArray.add("error", "El Embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El Embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("<PrintPalletInfoTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("<PrintPalletInfoTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            if (rset != null) {
                rset.close();
                rset = null;
            }
            if (fis != null) {
                fis.close();
            }
            CloseStatements();

        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Packing ID");
        document.addSubject("Packing ID");
        document.addKeywords("Agrocosa");
        document.addAuthor("SID");
        document.addCreator("SID");
    }

    private static void addContentPage(Document document, PdfWriter w, HashMap contentMap)
            throws DocumentException {
        String space = "          ";
        Barcode39 barcode39;
        String palletCode;

        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Packing ID", titleFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Agrocosa", subFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("A Gomez Farm Company", smallBold));
        addEmptyLine(preface, 3);
        document.add(preface);

        //PdfPTable table = new PdfPTable(2);
        //table.addCell(new Phrase(space + "Packing ID:", tableFont));
        
        barcode39 = new Barcode39();
        
        palletCode = contentMap.get("PalletId").toString().trim().toUpperCase();

        // Validar solo si tiene caracteres válidos para Code 39
        if (!palletCode.matches("[A-Z0-9\\-\\. $/+%]+")) {
            throw new IllegalArgumentException("PalletId contiene caracteres inválidos para Code 39: [" + palletCode + "]");
        }
        
        //barcode39.setCode(contentMap.get("PalletId").toString());
        barcode39.setCode(palletCode);
        barcode39.setBarHeight(70); // 50
        
        PdfContentByte cb = w.getDirectContent();
        Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
        //PdfPCell cell = new PdfPCell(code39Image);
        /*cell.setPaddingLeft(40);
        cell.setPaddingTop(5);
        table.addCell(cell);*/
        code39Image.setAlignment(Element.ALIGN_CENTER); // Centrarlo en la página
        code39Image.scalePercent(150); // Ajusta tamaño si es necesario
        document.add(code39Image);

        /*table.addCell(new Phrase(space + "Producto:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("ProductName").toString(), tableFont));
        table.addCell(cell);

        table.addCell(new Phrase(space + "Tamaño:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("Size").toString(), tableFont));
        table.addCell(cell);

        table.addCell(new Phrase(space + "Color:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("Color").toString(), tableFont));
        table.addCell(cell);
        
        table.addCell(new Phrase(space + "Peso por Arpilla:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("Quantity").toString()+" lb", tableFont));
        table.addCell(cell);
        
        table.addCell(new Phrase(space + "Campo:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("CampName").toString(), tableFont));
        table.addCell(cell);

        table.addCell(new Phrase(space + "Ubicación:", tableFont));
        cell = new PdfPCell();
        cell.addElement(new Phrase(space + contentMap.get("StorageName").toString() + " / " + contentMap.get("SubstorageName").toString(), tableFont));
        table.addCell(cell);

        table.addCell(new Phrase(space + "Fecha:", tableFont));
        cell = new PdfPCell();
        Locale esLocale = new Locale("es", "ES");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy", esLocale);//
        cell.addElement(new Phrase(space + formatter.format(new java.util.Date()), tableFont));
        table.addCell(cell);

        document.add(table);*/
        
        // Espacio debajo del código de barras
        document.add(new Paragraph(" "));

        // Producto (centrado)
        Paragraph product = new Paragraph(contentMap.get("ProductName").toString(), tableFont);
        product.setAlignment(Element.ALIGN_CENTER);
        document.add(product);

        // Fecha (alineada a la derecha)
        Locale esLocale = new Locale("es", "ES");
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", esLocale);
        String fecha = formatter.format(new java.util.Date());

        Paragraph date = new Paragraph(fecha, tableFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        // Start a new page
        //if(counter > 1){
        //document.newPage();
        //}
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * Generates an xmlNodeArray containing parameters for this transaction
     *
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.PrintPalletInfoTransaction");
        nodeArr.add("idUser", "1");
        return nodeArr;
    }

    /**
     * The main method for the transaction. Creates a database connection and an
     * error Array, then executes the transaction and reports any errors
     *
     * @param argv argv[0] is an optional configuration file name
     * @exception (none)
     */
    public static void main(String[] argv) {
        try {
            PrintPalletInfoTransaction transaction = new PrintPalletInfoTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.PrintPalletInfoTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" PrintPalletInfoTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" PrintPalletInfoTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.PrintPalletInfoTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("PrintPalletInfoTransaction - No results were returned.");
                            } else {
                                xmlNodeArray array = resultTransaction.GetResultArray();
                                String str = xmlNodeArray.xmlNodeArray2String(array);
                                array = xmlNodeArray.string2xmlNodeArray(str);
                                System.out.println(str);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("PrintPalletInfoTransaction::main> caught exception " + e.getMessage());
        }
    }
}
