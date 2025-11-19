package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class BoxFillingServlet extends SIDServlet {
    String saveTxn = "AgrocosaTransactions.SaveNewStockTransferOrderTransaction";
    String transferOrderTxn = "AgrocosaTransactions.RetrieveTruckTransferWithoutPalletTransaction";

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

            session.setAttribute("acordion", "Shipping");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", "");
            callTxnNA.append(nodeArray);
       
            //save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Save")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);

                    txnNA = executeTransaction(callTxnNA);
                    /*if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }*/
                    
                    String palletInfoStr = "";
                    if (nodeArray != null && nodeArray.exist("palletInfo") && !nodeArray.find("palletInfo").isValueEmpty()) {
                        palletInfoStr = nodeArray.find("palletInfo").getStringValue();
                    }

                    // Anexa el resultado del txn como siempre
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }

                    // Decide si preservas o limpias el textarea según el resultado
                    boolean ok = (txnNA != null
                            && txnNA.exist("RESPONSE_CODE")
                            && "PASS".equalsIgnoreCase(txnNA.find("RESPONSE_CODE").getStringValue()));

                    if (ok) {
                        // Guardado exitoso → limpiar el textarea
                        redirectNA.add("PalletInfo_Temp", "");
                    } else {
                        // Error o validación fallida → preservar lo que escribió el usuario
                        redirectNA.add("PalletInfo_Temp", palletInfoStr);
                    }
                }
            }

            //retieve saved stos
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(transferOrderTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/boxFilling.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect(request.getContextPath() + "/protected/management.do");
            } else {
                rd = request.getRequestDispatcher("/boxFilling.page");
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
