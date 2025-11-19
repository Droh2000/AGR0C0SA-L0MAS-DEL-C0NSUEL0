package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AddCampSupervisorServlet extends SIDServlet {

    String campTxn = "AgrocosaTransactions.RetrieveCampDetailTransaction";
    String supervisorTxn = "AgrocosaTransactions.RetrieveSupervisorTransaction";
    String saveSupervisorTxn = "AgrocosaTransactions.SaveSelectedSupervisorTransaction";
    String saveBombTxn = "AgrocosaTransactions.SaveSelectedBombTransaction";
    String bombTxn = "AgrocosaTransactions.BombMenuTransaction";

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

            session.setAttribute("acordion", "Camp");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            //Camp Detail
            callTxnNA = new xmlNodeArray();
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", campTxn);
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                redirectNA.append(txnNA);
            }

            //Bomb Menu
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(bombTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                redirectNA.append(txnNA);
            }

            //Save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("SaveSelectedSupervisor")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveSupervisorTxn);

                    xmlNodeArray tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        redirectNA.append(tmp);
                    }

                    //Camp Detail   again after  
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campTxn);
                    tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        tmp.remove("RESPONSE_CODE");
                        tmp.remove("RESPONSE_MESSAGE");
                        tmp.remove("RESPONSE_DETAIL");
                        redirectNA.append(tmp);
                    }
                }
            }

            //Save BOMB
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("SaveSelectedBomb")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveBombTxn);

                    xmlNodeArray tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        redirectNA.append(tmp);
                    }

                    //Camp Detail   again after  
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campTxn);
                    tmp = executeTransaction(callTxnNA);
                    if (tmp != null) {
                        tmp.remove("RESPONSE_CODE");
                        tmp.remove("RESPONSE_MESSAGE");
                        tmp.remove("RESPONSE_DETAIL");
                        redirectNA.append(tmp);
                    }
                }
            }

            //Retrieve Supervisor Info
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(supervisorTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/addCampSupervisor.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/addCampSupervisor.page");
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
