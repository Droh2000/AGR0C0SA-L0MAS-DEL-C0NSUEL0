/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
//import java.sql.*;
import java.util.ArrayList;
import xmlNodeArray.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.mail.*;
import javax.mail.internet.*;

public class GenerateDriverAgreementPDFTransaction extends SIDWebTransaction {
    
    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public GenerateDriverAgreementPDFTransaction() {
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
        String[] mandatoryTags = {
            "firmaBase64"
        };
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<GenerateDriverAgreementPDFTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<GenerateDriverAgreementPDFTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<GenerateDriverAgreementPDFTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<GenerateDriverAgreementPDFTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
                errArray.add("OPTIONAL_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        return true;
    }
    
    /**
     * executes sql statements using input arguments and returns result
     *
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    @Override
    public synchronized xmlNodeArray Execute() throws Exception {
        xmlNodeArray result = new xmlNodeArray();
        try {
            String firmaBase64 = GetNodeArray().find("firmaBase64").getStringValue();

            if (firmaBase64 != null && !firmaBase64.isEmpty()) {
                String base64Image = firmaBase64.split(",")[1];
                byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                
                // Aqui se almacena en Tomcat (Verificar en produccion si se logra hacer o Dara ERROR)
                Path dir = Paths.get("/tmp/pdfs/driveragreement/");
                Files.createDirectories(dir);
                String fileName = "DriverAgreement_" + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
                Path pdfPath = dir.resolve(fileName);

                generarPDF(pdfPath.toString(), decodedBytes);
                enviarCorreo("droh2000@gmail.com", pdfPath.toString());
                
                // === Intentar eliminar el archivo PDF temporal ===
                try {
                    Files.deleteIfExists(pdfPath);
                    System.out.println("!!!!! Archivo temporal eliminado: " + pdfPath.toString());
                } catch (IOException e) {
                    System.err.println("No se pudo eliminar el PDF temporal: " + pdfPath.toString());
                    e.printStackTrace();
                }

                result.add("RESPONSE_MESSAGE", "✅ PDF generado y enviado correctamente: " + fileName);
            } else {
                result.add("RESPONSE_MESSAGE", "⚠ No se recibió firma Base64 válida");
            }
        } catch (Exception e) {
            result.add("RESPONSE_MESSAGE", "❌ Error al generar/enviar PDF: " + e.getMessage());
            e.printStackTrace();
            // Si el envío falla, no se borra el archivo: puede servir para reintento
        }
        return result;
    }
    
    private void generarPDF(String outputPath, byte[] firmaBytes) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
        doc.open();

        doc.add(new Paragraph("Driver Agreement Warehouse"));
        doc.add(new Paragraph("Fecha: " + new Date()));
        doc.add(new Paragraph(" "));

        Image firma = Image.getInstance(firmaBytes);
        firma.scaleToFit(250, 100);
        doc.add(firma);
        doc.close();
    }

    private void enviarCorreo(String destinatario, String rutaPDF) throws Exception {
        String remitente = "ortega.diego5@gmail.com";
        String password = "nhxk pdld whfx awod"; // Usa App Password de Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(remitente));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        msg.setSubject("Nuevo registro de transportista");

        Multipart multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Se adjunta el PDF del registro con la firma digital.");
        multipart.addBodyPart(textPart);

        MimeBodyPart attachPart = new MimeBodyPart();
        attachPart.attachFile(new File(rutaPDF));
        multipart.addBodyPart(attachPart);

        msg.setContent(multipart);
        Transport.send(msg);
    }

    @Override
    public xmlNodeArray GenerateTestParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

