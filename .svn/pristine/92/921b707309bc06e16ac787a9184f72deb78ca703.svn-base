/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.*;
import xmlNodeArray.xmlNodeArray;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class SaveCampDocumentServlet extends SIDServlet {

    String managementTxn = "AgrocosaTransactions.CampManagementTransaction";

    // Servlet configuration variables
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param nodeArray
     */
    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        HttpSession session = null;
        String controller = this.getClass().getName();

        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;

        InputStream pdfFileBytes = null;
        PrintWriter writer = null;

        boolean boolContinue = false;

        try {
            session = request.getSession();

            writer = response.getWriter();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            String idUser = session.getAttribute("idUser").toString();
            final Part filePart = request.getPart("fileName");
            String idCamp = request.getParameter("idCamp");

            if (filePart.getContentType().equals("application/pdf")) {
                boolContinue = true;
            } else {
                request.setAttribute("RESPONSE_CODE", "FAIL");
                request.setAttribute("RESPONSE_MESSAGE", "Archivo invalido, Por favor seleccione un archivo en formato pdf");
                request.setAttribute("RESPONSE_DETAIL", "");
            }

            if (filePart.getSize() > 1048576) { //2mb
                {
                    request.setAttribute("RESPONSE_CODE", "FAIL");
                    request.setAttribute("RESPONSE_MESSAGE", "Tamaño de archivo invalido, Por favor seleccione un archivo menor a 2 Mb");
                    request.setAttribute("RESPONSE_DETAIL", "");

                }
            }

            if (boolContinue) {

                pdfFileBytes = filePart.getInputStream();  // to get the body of the request as binary data

                final byte[] bytes = new byte[pdfFileBytes.available()];
                pdfFileBytes.read(bytes);  //Storing the binary data in bytes array.

                //------------------------------------------
                //  Update Picture on DB
                //------------------------------------------
                SIDDataBase database = new SIDDataBase();
                con = database.GetConnection();
                //update
                pstmt = con.prepareStatement("Update camp set Picture = ?, idUser = ?, ModifiedDate = Now() where idCamp = ? ");
                pstmt.setBytes(1, bytes);
                pstmt.setString(2, idUser);
                pstmt.setString(3, idCamp);
                int i = pstmt.executeUpdate();
                if (i > 0) {
                    request.setAttribute("RESPONSE_CODE", "PASS");
                    request.setAttribute("RESPONSE_MESSAGE", "El documento de ubicacion ha sido registrado exitosamente.");
                    request.setAttribute("RESPONSE_DETAIL", "");

                    con.close();

                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", managementTxn);
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        txnNA.remove("RESPONSE_CODE");
                        txnNA.remove("RESPONSE_MESSAGE");
                        txnNA.remove("RESPONSE_DETAIL");
                        txnNA.add("Method", "Edit");
                        txnNA.add("ID", "new");
                        request = this.parseTxnResponse(request, txnNA);
                    }

                } else {
                    request.setAttribute("error", "El archivo no pudo ser registrado.");
                    request.setAttribute("RESPONSE_CODE", "FAIL");
                    request.setAttribute("RESPONSE_MESSAGE", "Archivo no registrado");
                    request.setAttribute("RESPONSE_DETAIL", "");
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }

            }

            rd = request.getRequestDispatcher("/campManagement.page");
            rd.forward(request, response);

        } catch (SQLException sqle) {
            request.setAttribute("error", "SQL_EXCEPTION_ERROR" + sqle.getMessage());
        } catch (IOException ioe) {
            request.setAttribute("error", "IO_EXCEPTION_ERROR" + ioe.getMessage());
        } catch (ServletException se) {
            request.setAttribute("error", "SERVLET_EXCEPTION_ERROR" + se.getMessage());
        } finally {

            try {
                if (pdfFileBytes != null) {
                    pdfFileBytes.close();
                }
                if (writer != null) {
                    writer.close();
                }

                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (con != null && !con.isClosed()) {
                    con.close();
                    con = null;
                }
            } catch (SQLException sqle) {
                request.setAttribute("error", "SQL_EXCEPTION_ERROR" + sqle.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(SaveCampDocumentServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
