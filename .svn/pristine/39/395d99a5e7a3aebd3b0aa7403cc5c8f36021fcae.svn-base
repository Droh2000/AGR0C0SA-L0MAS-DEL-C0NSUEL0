/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AddAgrochemicalCropTypeServlet extends SIDServlet {

    String retrieveTxn = "AgrocosaTransactions.RetrieveAgrochemicalCropTypeTransaction";
    String saveTxn = "AgrocosaTransactions.SaveAgrochemicalCropTypeTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray saveTxnNA = null;
        xmlNodeArray txnNA = null;
        String controller = this.getClass().getName();
        boolean boolSave = false;

        try {
            session = request.getSession();
            saveTxnNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);


            //Save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("saveAgrochemicalCropType")) {
                    saveTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    saveTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", saveTxn);
                    saveTxnNA.append(nodeArray);

                    xmlNodeArray tmp = executeTransaction(saveTxnNA);
                    if (tmp != null) {
                        boolSave = true;
                        request = this.parseTxnResponse(request, tmp);
                    }
                }
            }

            //retrieve
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", retrieveTxn);                        
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                if (boolSave) {
                    txnNA.remove("RESPONSE_CODE");
                    txnNA.remove("RESPONSE_DETAIL");
                    txnNA.remove("RESPONSE_MESSAGE");
                }
                request = this.parseTxnResponse(request, txnNA);
            }

            rd = request.getRequestDispatcher("/addAgrochemicalCropType.page");

            rd.forward(request, response);
        } catch (IOException ioe) {
            if (session != null) {
                session.setAttribute("error",
                        "IO_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                        + ": processRequest> IOException: "
                        + ioe.getMessage());
            }
        } catch (ServletException se) {
            if (session != null) {
                session.setAttribute("error",
                        "SERVLET_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                        + ": processRequest> ServletException: "
                        + se.getMessage());
            }
        }
    }
}
