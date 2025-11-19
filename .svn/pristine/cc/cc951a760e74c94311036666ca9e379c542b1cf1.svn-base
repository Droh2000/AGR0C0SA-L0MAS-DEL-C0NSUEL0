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

public class SubstorageManagementServlet extends SIDServlet {

    String storageTxn = "AgrocosaTransactions.StorageMenuTransaction";
    String managementTxn = "AgrocosaTransactions.SubstorageManagementTransaction";

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray areaNA = null;
        xmlNodeArray storageNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            //storage
            callTxnNA = new xmlNodeArray();
            storageNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", storageTxn);
            callTxnNA.append(nodeArray);
            storageNA = executeTransaction(callTxnNA);

            //management
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", managementTxn);
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);

            if (nodeArray.existValue("Method")) {
                if (nodeArray.find("Method").getStringValue().equals("Add")) {
                    txnNA.add("Method", "Edit");
                    txnNA.add("ID", "new");
                } else {
                    txnNA.add("Method", nodeArray.find("Method").getStringValue());
                    if (nodeArray.exist("ID")) {
                        txnNA.add("ID", nodeArray.find("ID").getStringValue());
                    }
                }
            }

            if (areaNA != null) {
                areaNA.remove("RESPONSE_CODE");
                areaNA.remove("RESPONSE_DETAIL");
                areaNA.remove("RESPONSE_MESSAGE");
                txnNA.append(areaNA);
            }

            if (storageNA != null) {
                storageNA.remove("RESPONSE_CODE");
                storageNA.remove("RESPONSE_DETAIL");
                storageNA.remove("RESPONSE_MESSAGE");
                txnNA.append(storageNA);
            }

            if (txnNA != null) {
                txnNA.add("JspPage", "/substorageManagement.page");
                session.setAttribute("TxData", txnNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/substorageManagement.page");
                rd.forward(request, response);
            }

        } catch (IOException ioe) {
            session.setAttribute("error",
                    "IO_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                    + ": processRequest> IOException: "
                    + ioe.getMessage());
        } catch (ServletException se) {
            session.setAttribute("error",
                    "SERVLET_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                    + ": processRequest> ServletException: "
                    + se.getMessage());

        }
    }
}
