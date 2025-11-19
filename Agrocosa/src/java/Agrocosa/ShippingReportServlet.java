package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class ShippingReportServlet extends SIDServlet {

    String reportTxn = "AgrocosaTransactions.RetrieveShippingReportTransaction";
    String closeTxn = "AgrocosaTransactions.CloseShippingTransaction";

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
            
//            response.setIntHeader("Refresh", 60);

            callTxnNA = new xmlNodeArray();
            callTxnNA.append(nodeArray);
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE",reportTxn);
            
            //receive ship
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("CloseShipping")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(closeTxn);                    
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }
            
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);
            txnNA = executeTransaction(callTxnNA);            
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                request = this.parseTxnResponse(request, txnNA);
            }

            rd = request.getRequestDispatcher("/shippingReport.page");

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
