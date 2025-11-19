package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class SavePasswordServlet extends SIDServlet {

    String getSaveTxn = "AgrocosaTransactions.SaveUserPasswordTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;

        try {
            session = request.getSession();

            // Call Txn 
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("currentPassword")
                        && !nodeArray.find("currentPassword").isValueEmpty()
                        && nodeArray.exist("newPassword")
                        && !nodeArray.find("newPassword").isValueEmpty()) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", getSaveTxn);
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }

            rd = request.getRequestDispatcher("/profile.page");

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
