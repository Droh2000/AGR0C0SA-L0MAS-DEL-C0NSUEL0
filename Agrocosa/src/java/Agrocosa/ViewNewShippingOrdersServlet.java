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

public class ViewNewShippingOrdersServlet extends SIDServlet {

    String viewAllDataTxn = "AgrocosaTransactions.RetrieveNewShippingOrdersTransaction";
    String cancelTxn = "AgrocosaTransactions.CancelNewShippingOrderTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray txnCancelNA = null;
        xmlNodeArray saveTxnNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            session.setAttribute("acordion", "Crossdock");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);
            callTxnNA = new xmlNodeArray();
            txnNA = new xmlNodeArray();
            txnCancelNA = new xmlNodeArray();

            //cancel
            //delete
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("CancelShippingOrder")) {
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE",cancelTxn);
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.append(nodeArray);

                    saveTxnNA = executeTransaction(callTxnNA);
                    if (saveTxnNA != null) {
                        txnCancelNA.append(saveTxnNA);
                    }
                }
            }

            
            callTxnNA = new xmlNodeArray();
            //Search
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("viewAllShippingOrder")) {
                    callTxnNA.add("ShowBackButton", "YES");
                }
            }

            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", viewAllDataTxn);
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_DETAIL");
                txnNA.remove("RESPONSE_MESSAGE");
                
                if (txnCancelNA != null) {
                    txnNA.append(txnCancelNA);
                }
                
                request = this.parseTxnResponse(request, txnNA);
            }

            rd = request.getRequestDispatcher("/viewNewShippingOrders.page");
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
