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

public class AgrochemicalManagementServlet extends SIDServlet {

    String managementTxn = "AgrocosaTransactions.AgrochemicalManagementTransaction";
    String uomTxn = "AgrocosaTransactions.UOMMenuTransaction";
    String agrochemicalTypeTxn = "AgrocosaTransactions.AgrochemicalTypeMenuTransaction";

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray uomNA = null;
        xmlNodeArray agrochemicalTypeNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            callTxnNA = new xmlNodeArray();
            uomNA = new xmlNodeArray();
            agrochemicalTypeNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", uomTxn);
            uomNA = executeTransaction(callTxnNA);
                        
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(agrochemicalTypeTxn);
            agrochemicalTypeNA = executeTransaction(callTxnNA);
            

            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(managementTxn);            
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
            
            if (uomNA != null) {
                uomNA.remove("RESPONSE_CODE");
                uomNA.remove("RESPONSE_MESSAGE");
                uomNA.remove("RESPONSE_DETAIL");
                txnNA.append(uomNA);
            }
            if (agrochemicalTypeNA != null) {
                agrochemicalTypeNA.remove("RESPONSE_CODE");
                agrochemicalTypeNA.remove("RESPONSE_MESSAGE");
                agrochemicalTypeNA.remove("RESPONSE_DETAIL");
                txnNA.append(agrochemicalTypeNA);
            }
                        
            if (txnNA != null) {
                txnNA.add("JspPage", "/agrochemicalManagement.page");
                session.setAttribute("TxData", txnNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/agrochemicalManagement.page");
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
