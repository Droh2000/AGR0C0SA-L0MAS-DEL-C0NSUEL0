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

public class ScanShippingOrderServlet extends SIDServlet {

    String customerTxn = "AgrocosaTransactions.CustomerMenuTransaction";
    String searchTxn = "AgrocosaTransactions.RetrieveShippingOrderReportTransaction";
    String updateTxn = "AgrocosaTransactions.UpdateShippingOrderStatusTransaction";
    String saveTxn = "AgrocosaTransactions.SaveScanShippingOrderTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray txnFilterNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            session.setAttribute("acordion", "Crossdock");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA = new xmlNodeArray();
            txnFilterNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", customerTxn);
            callTxnNA.append(nodeArray);

            txnFilterNA = executeTransaction(callTxnNA);
            if (txnFilterNA != null) {
                request = this.parseTxnResponse(request, txnFilterNA);
            }

            //update
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action2")
                        && !nodeArray.find("Action2").isValueEmpty()
                        && nodeArray.find("Action2").getStringValue().equals("UpdateShippingOrder")) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", updateTxn);
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }
            
            //save scanned order
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action3")
                        && !nodeArray.find("Action3").isValueEmpty()
                        && nodeArray.find("Action3").getStringValue().equals("SaveOrder")) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", saveTxn);
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }

            //Search
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Search")) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", searchTxn);
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        txnNA.remove("RESPONSE_CODE");
                        txnNA.remove("RESPONSE_DETAIL");
                        txnNA.remove("RESPONSE_MESSAGE");
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }

            rd = request.getRequestDispatcher("/scanShippingOrder.page");
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
