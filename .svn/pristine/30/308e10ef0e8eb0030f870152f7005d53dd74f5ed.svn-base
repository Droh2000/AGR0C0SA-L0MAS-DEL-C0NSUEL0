package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class CampReportServlet extends SIDServlet {
    String campTxn = "AgrocosaTransactions.CampMenuTransaction";
    String campSectionTxn = "AgrocosaTransactions.CampSectionMenuTransaction";
    String supervisorTxn = "AgrocosaTransactions.SupervisorMenuTransaction";
    String bombTxn = "AgrocosaTransactions.BombMenuTransaction";
    String landTypeTxn = "AgrocosaTransactions.LandTypeMenuTransaction";
    String pipeTypeTxn = "AgrocosaTransactions.PipeTypeMenuTransaction";
    String cropTypeTxn = "AgrocosaTransactions.CropTypeMenuTransaction";
    String cropTxn = "AgrocosaTransactions.CropMenuTransaction";
    String reportTxn = "AgrocosaTransactions.RetrieveCampReportTransaction";
    
    
    
    
    

    
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

            //camp
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
            
            //camp Section
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campSectionTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            //Supervisor
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(supervisorTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            
            //Bomb	
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(bombTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            
            //Land Type Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(landTypeTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            
            //Pipe Type Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(pipeTypeTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            
            //Crop Type Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(cropTypeTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            
            
            //Crop Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(cropTxn);
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
                    callTxnNA.append(nodeArray);

                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/campReport.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/campReport.page");
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
