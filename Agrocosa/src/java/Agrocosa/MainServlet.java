package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;
import xmlNodeArray.xmlTable;

public class MainServlet extends SIDServlet {

    String taskComentsTxn = "AgrocosaTransactions.RetrieveTaskCommentsTransaction";
    String menuTxn = "AgrocosaTransactions.RetrieveUserMenuTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        String tableName = "";
        boolean isLogged = false;
        
        try {
            //session = request.getSession();
            // Asegúrase de tener session (pública o con login)
            if (session == null) {
                session = request.getSession(true);
            }

            // Evalúa login con el atributo
            isLogged = (session.getAttribute("idUser") != null);

            if (isLogged) {
                callTxnNA = new xmlNodeArray();
                callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", menuTxn);
                callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                callTxnNA.append(nodeArray);

                txnNA = executeTransaction(callTxnNA);
                if (txnNA != null) {
                    request = this.parseTxnResponse(request, txnNA);
                    session.setAttribute("Menu_Table", request.getAttribute("Menu_Table"));
                    //submenu                 
                    if (txnNA.find("Menu_Table") != null
                            && txnNA.find("Menu_Table").getTable() != null) {
                        xmlTable resultTab = txnNA.find("Menu_Table").getTable();
                        for (int i = 0; i < resultTab.getRowsQty(); i++) {
                            tableName = resultTab.getStringValue(i, 0) + "_Table";
                            session.setAttribute(tableName, request.getAttribute(tableName));
                        }
                    }
                }


                //TaskComments
                callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(taskComentsTxn);
                txnNA = executeTransaction(callTxnNA);
                if (txnNA != null) {
                      request = this.parseTxnResponse(request, txnNA);
                }


                rd = request.getRequestDispatcher("/main.page");

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
