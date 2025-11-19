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

public class DriverAgreementWarehouseServlet extends SIDServlet {
    String saveTxn = "AgrocosaTransactions.SaveDriverAgreementWarehouseTransaction";
    String generatePDFsendMail = "AgrocosaTransactions.GenerateDriverAgreementPDFTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        //String controller = this.getClass().getName();
        xmlNodeArray redirectNA = null;
        boolean isLogged = false, sendTanks = false;

            // Asegúrase de tener session (pública o con login)
            if (session == null) {
                session = request.getSession(true);
            }

            // Evalúa login con el atributo
            isLogged = (session.getAttribute("idUser") != null);

            // Arma redirectNA para ManagementServlet
            redirectNA = new xmlNodeArray();

            try {
                // Menús/catálogos que debe ver cualquiera (público)
                callTxnNA = new xmlNodeArray();
                callTxnNA.append(nodeArray);
                
                callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", "");
                
                if (nodeArray != null) {
                    if (nodeArray.getSize() > 0
                            && nodeArray.exist("Action")
                            && !nodeArray.find("Action").isValueEmpty()
                            && nodeArray.find("Action").getStringValue().equals("Save")) {
                        
                        callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);

                        txnNA = executeTransaction(callTxnNA);
                        if (txnNA != null) {
                            redirectNA.append(txnNA);
                            try {
                                callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(generatePDFsendMail);
                                txnNA = executeTransaction(callTxnNA);
                                
                                if (txnNA != null && txnNA.exist("RESPONSE_MESSAGE")) {
                                    System.out.println(txnNA.find("RESPONSE_MESSAGE").getStringValue());
                                }
                                sendTanks = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                
                // Página a renderizar
                if(sendTanks){
                    redirectNA.add("JspPage", "/thanks.page");
                }else{
                    redirectNA.add("JspPage", "/driverAgreementWarehouse.page");
                }
                
                // PRG → ManagementServlet
                session.setAttribute("TxData", redirectNA);

                // Usar SIEMPRE el contextPath para no romper rutas
                String ctx = request.getContextPath();
                if (isLogged) {
                    response.sendRedirect(ctx + "/protected/management.do");
                } else {
                    response.sendRedirect(ctx + "/public/management.do");
                }
                
                return;
        } catch (IOException ioe) {
            if (session != null) {
                session.setAttribute("error",
                        "IO_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                        + ": processRequest> IOException: "
                        + ioe.getMessage());
            }
        } catch (Exception e) {
            if (session != null) {
                session.setAttribute("error",
                        "SERVLET_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName()
                        + ": processRequest> ServletException: "
                        + e.getMessage());
            }
        }
    }
} 