package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AddTaskTypeUserServlet extends SIDServlet {

    String supervisorTxn = "AgrocosaTransactions.RetrieveUserTransaction";
    String saveSupervisorTxn = "AgrocosaTransactions.SaveSelectedTaskTypeUserTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        String controller = this.getClass().getName();
        xmlNodeArray redirectNA = null;

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            redirectNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);


            //
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", supervisorTxn);
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                redirectNA.append(txnNA);
            }

            //Save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("SaveSelectedUser")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveSupervisorTxn);

                    xmlNodeArray tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        redirectNA.append(tmp);
                    }

                    //retrieve after  
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(supervisorTxn);
                    tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        tmp.remove("RESPONSE_CODE");
                        tmp.remove("RESPONSE_MESSAGE");
                        tmp.remove("RESPONSE_DETAIL");
                        redirectNA.append(tmp);
                    }
                }
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/addTaskTypeUser.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/addTaskTypeUser.page");
                rd.forward(request, response);
            }

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
