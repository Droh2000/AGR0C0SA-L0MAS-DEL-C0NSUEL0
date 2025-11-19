/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xmlNodeArray.xmlNodeArray;
import xmlNodeArray.xmlTable;

/**
 *
 * @author Sly
 */
public class AjaxGetTaskCropNameServlet extends SIDServlet {

    String cropNameTxn = "AgrocosaTransactions.TaskCropNameMenuTransaction";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) throws IOException {

        String text = "";

        response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8"); // You want world domination, huh?

        try {
            response.setContentType("text/html;charset=UTF-8");
            xmlNodeArray callTxnNA = null;
            xmlNodeArray txnNA = null;

            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("idTask")
                        && !nodeArray.find("idTask").isValueEmpty()) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", cropNameTxn);
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null && txnNA.exist("CropName_HashMap")) {
                        xmlTable tab = txnNA.find("CropName_HashMap").getTable();

                        ArrayList<String> resLst = tab.getColumnValues("CropName");
                        if (resLst != null) {
                            if (resLst.size() > 0) {
                                resLst.remove(0);
                            }
//                            text += ("<option value=\"\"></option> ");
                            for (String res : resLst) {
                                text += ("<option value=\"" + res + "\">" + res + "</option> ");
                            }
                        } else {
                            text = "";
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("<Exception::>" + e);
        } finally {
            response.getWriter().write(text);       // Write response body.
        }
    }
}
