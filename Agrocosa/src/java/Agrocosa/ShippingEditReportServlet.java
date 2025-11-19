package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class ShippingEditReportServlet extends SIDServlet {

    String reportTxn = "AgrocosaTransactions.RetrieveShippingEditReportTransaction";
    String modifyTxn = "AgrocosaTransactions.CloseShippingEditTransaction";
    String proformaTxn = "AgrocosaTransactions.ProformaMenuTransaction";

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

//            response.setIntHeader("Refresh", 1);
            callTxnNA = new xmlNodeArray();
            callTxnNA.append(nodeArray);
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", proformaTxn);

            //Proforma
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_DETAIL");
                txnNA.remove("RESPONSE_MESSAGE");
                request = this.parseTxnResponse(request, txnNA);
            }

            //Modify ship
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("ModifyShipping")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(modifyTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }

            //search ship
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Search")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        request = this.parseTxnResponse(request, txnNA);
                    }
                }
            }

//            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);
//            txnNA = executeTransaction(callTxnNA);
//            if (txnNA != null) {
//                txnNA.remove("RESPONSE_CODE");
//                txnNA.remove("RESPONSE_MESSAGE");
//                txnNA.remove("RESPONSE_DETAIL");
//                request = this.parseTxnResponse(request, txnNA);
//            }
            rd = request.getRequestDispatcher("/shippingEditReport.page");

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
