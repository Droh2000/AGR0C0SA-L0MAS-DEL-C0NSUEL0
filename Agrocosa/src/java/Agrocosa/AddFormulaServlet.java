/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class AddFormulaServlet extends SIDServlet {

    String uomTxn = "AgrocosaTransactions.UOMMenuTransaction";
    String vegetalNutritionTxn = "AgrocosaTransactions.VegetalNutritionMenuTransaction";
    String formulaDetailTxn = "AgrocosaTransactions.RetrieveFormulaDetailTransaction";
    String saveTxn = "AgrocosaTransactions.SaveFormulaDetailTransaction";
    String saveVegetalNutritionTxn = "AgrocosaTransactions.SaveVegetalNutritionTransaction";

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray redirectNA = null;
        xmlNodeArray txnNA = null;
        String controller = this.getClass().getName();

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            redirectNA = new xmlNodeArray();

            session.setAttribute("acordion", "Admin");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", uomTxn);
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //SaveTableData
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("SaveTableData")) {
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            //SaveVegetableNutrition 
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("saveVegetalNutrition")) {
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveVegetalNutritionTxn);
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", formulaDetailTxn);
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            callTxnNA = new xmlNodeArray();
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", vegetalNutritionTxn);
            callTxnNA.append(nodeArray);

            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/addFormula.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/addFormula.page");
                rd.forward(request, response);
            }

        } catch (IOException ioe) {
            session.setAttribute("error",
                    "IO_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                    + ": processRequest> IOException: "
                    + ioe.getMessage());
        } catch (ServletException se) {
            session.setAttribute("error",
                    "SERVLET_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                    + ": processRequest> ServletException: "
                    + se.getMessage());

        }
    }
}
