package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AddAccessServlet extends SIDServlet {

    String rootMenuTxn = "AgrocosaTransactions.RootMenuTransaction";
    String menuTxn = "AgrocosaTransactions.RetrieveMenuTransaction";
    String saveTxn = "AgrocosaTransactions.SaveSelectedMenuTransaction";
    String userTxn = "AgrocosaTransactions.RetrieveUserInfoTransaction";

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
            controller = controller.replaceAll("PER.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            //Service Detail
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", rootMenuTxn);
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
                        && nodeArray.find("Action").getStringValue().equals("SaveSelectedMenus")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);

                    xmlNodeArray tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        redirectNA.append(tmp);
                    }
                }
            }

            //user info
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", userTxn);
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_DETAIL");
                txnNA.remove("RESPONSE_MESSAGE");
                redirectNA.append(txnNA);
            }
                        
            //Menus
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(menuTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_DETAIL");
                txnNA.remove("RESPONSE_MESSAGE");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/addAccess.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/addAccess.page");
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
