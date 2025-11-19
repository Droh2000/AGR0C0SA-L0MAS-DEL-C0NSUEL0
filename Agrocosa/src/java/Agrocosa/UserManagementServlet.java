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

public class UserManagementServlet extends SIDServlet {

    String roleTxn = "AgrocosaTransactions.RoleMenuTransaction";
    String managementTxn = "AgrocosaTransactions.UserManagementTransaction";
    String delPicUserTxn = "AgrocosaTransactions.DeleteUserPictureTransaction";

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        xmlNodeArray roleNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            roleNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", roleTxn);
            callTxnNA.append(nodeArray);

            roleNA = executeTransaction(callTxnNA);
            if (roleNA != null) {
                roleNA.remove("RESPONSE_CODE");
                roleNA.remove("RESPONSE_DETAIL");
                roleNA.remove("RESPONSE_MESSAGE");
            }

            //delete user picture
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("DeletePicture")
                        && nodeArray.exist("idUser")
                        && !nodeArray.find("idUser").isValueEmpty()) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(delPicUserTxn);

                    xmlNodeArray tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        tmp.remove("RESPONSE_CODE");
                        tmp.remove("RESPONSE_DETAIL");
                        tmp.remove("RESPONSE_MESSAGE");
                        request = this.parseTxnResponse(request, tmp);
                    }
                }
            }

            //user
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", managementTxn);
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

            if (roleNA != null) {
                txnNA.append(roleNA);
            }

            if (txnNA != null) {
                txnNA.add("JspPage", "/userManagement.page");
                session.setAttribute("TxData", txnNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/userManagement.page");
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
