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

public class BombManagementServlet extends SIDServlet {

    String engineTxn = "AgrocosaTransactions.EngineMenuTransaction";
    String waterPumpTxn = "AgrocosaTransactions.WaterPumpMenuTransaction";
    String managementTxn = "AgrocosaTransactions.BombManagementTransaction";

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray engineNA = null;
        xmlNodeArray waterPumpNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            engineNA = new xmlNodeArray();
            waterPumpNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", engineTxn);
            callTxnNA.append(nodeArray);
            engineNA = executeTransaction(callTxnNA);

            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", waterPumpTxn);
            callTxnNA.append(nodeArray);
            waterPumpNA = executeTransaction(callTxnNA);
            
            
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

            if (engineNA != null) {
                engineNA.remove("RESPONSE_CODE");
                engineNA.remove("RESPONSE_MESSAGE");
                engineNA.remove("RESPONSE_DETAIL");
                txnNA.append(engineNA);
            }
            if (waterPumpNA != null) {
                waterPumpNA.remove("RESPONSE_CODE");
                waterPumpNA.remove("RESPONSE_MESSAGE");
                waterPumpNA.remove("RESPONSE_DETAIL");
                txnNA.append(waterPumpNA);
            }

            if (txnNA != null) {
                txnNA.add("JspPage", "/bombManagement.page");
                session.setAttribute("TxData", txnNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/bombManagement.page");
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
