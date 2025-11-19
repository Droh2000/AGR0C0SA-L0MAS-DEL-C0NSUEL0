package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class NewFarmingTaskServlet extends SIDServlet {

    String campTxn = "AgrocosaTransactions.CampMenuTransaction";
    String campSectionTxn = "AgrocosaTransactions.RetrieveCampSectionTransaction";
    String saveTxn = "AgrocosaTransactions.SaveNewTaskTransaction";
    String newSavedTxn = "AgrocosaTransactions.RetrieveNewTaskTransaction";
    String deleteTxn = "AgrocosaTransactions.DeleteTaskTransaction";
    String tractorTxn = "AgrocosaTransactions.TractorMenuTransaction";
    String toolTxn = "AgrocosaTransactions.ToolMenuTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        String controller = this.getClass().getName();
        xmlNodeArray callTxnNA = null;
        xmlNodeArray redirectNA = null;
        xmlNodeArray txnNA = null;

        try {
            session = request.getSession();
            session.setAttribute("acordion", "Task");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);
            callTxnNA = new xmlNodeArray();
            redirectNA = new xmlNodeArray();

            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", campTxn);
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(tractorTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(toolTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //Search
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("searchCampSection")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campSectionTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            //save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("saveTask")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            //delete
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("DeleteTask")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(deleteTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            //new saved
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(newSavedTxn);
            callTxnNA.add("taskTypeFilter", "Labranza");
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/newFarmingTask.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/newFarmingTask.page");
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
