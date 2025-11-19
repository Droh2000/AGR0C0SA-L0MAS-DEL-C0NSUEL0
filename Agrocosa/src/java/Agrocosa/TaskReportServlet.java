package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;


public class TaskReportServlet extends SIDServlet {

    String taskTypeTxn = "AgrocosaTransactions.TaskTypeMenuTransaction";
    String campTxn = "AgrocosaTransactions.CampMenuTransaction";
    String laborTypeTxn = "AgrocosaTransactions.LaborTypeMenuTransaction";
    String supervisorTxn = "AgrocosaTransactions.SupervisorMenuTransaction";
    String reportTxn = "AgrocosaTransactions.RetrieveTaskReportTransaction";

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

            session.setAttribute("acordion", "Report");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", taskTypeTxn);
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //Camp Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //labor type
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(laborTypeTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //supervisor
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(supervisorTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //search
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Search")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                } else {
                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    callTxnNA.add("status","En Proceso");
                    callTxnNA.add("startDate",sdf.format(date));
                    callTxnNA.add("endDate",sdf.format(date));
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/taskReport.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/taskReport.page");
                rd.forward(request, response);
            }
//            request = this.parseTxnResponse(request, redirectNA);
//
//            rd = request.getRequestDispatcher("/taskReport.page");
//            rd.forward(request, response);

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
