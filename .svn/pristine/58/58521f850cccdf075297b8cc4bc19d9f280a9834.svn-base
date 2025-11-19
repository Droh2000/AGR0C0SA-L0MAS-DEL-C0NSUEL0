/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AjaxShippingOrderDetailServlet extends SIDServlet {

    String txn = "AgrocosaTransactions.RetrieveAjaxShippingOrderDetailTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) throws IOException {

        HttpSession session = null;
        String text = "";
        response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8"); // You want world domination, huh?

        try {
            session = request.getSession();
            response.setContentType("text/html;charset=UTF-8");
            xmlNodeArray callTxnNA = null;
            xmlNodeArray txnNA = null;
            
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("idShippingOrder")
                        && !nodeArray.find("idShippingOrder").isValueEmpty()) {
                    callTxnNA = new xmlNodeArray();
                    callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", txn);
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null && txnNA.exist("Result")
                            && !txnNA.find("Result").isValueEmpty()) {
                        txnNA.remove("RESPONSE_CODE");
                        txnNA.remove("RESPONSE_DETAIL");
                        txnNA.remove("RESPONSE_MESSAGE");

                        text += txnNA.find("Result").getStringValue();

                    } 
                }
            }
        } finally {
            response.getWriter().write(text);
        }
    }
}
