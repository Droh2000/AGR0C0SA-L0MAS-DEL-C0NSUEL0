package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class NewStockTransferOrderServlet extends SIDServlet {
    String transportCompanyTxn = "AgrocosaTransactions.TransportCompanyMenuTransaction";
    String saveTxn = "AgrocosaTransactions.SaveNewTruckerRegistrationTransaction";
    String transferOrderTxn = "AgrocosaTransactions.RetrieveStockTransferOrderTransaction";
    String saveEvaluate = "AgrocosaTransactions.SaveEvaluateTruckTransaction";

    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        String controller = this.getClass().getName();
        xmlNodeArray redirectNA = null;
        boolean isLogged = false;

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
                
                callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", transportCompanyTxn);
                txnNA = executeTransaction(callTxnNA);
                if (txnNA != null) {
                    txnNA.remove("RESPONSE_CODE");
                    txnNA.remove("RESPONSE_MESSAGE");
                    txnNA.remove("RESPONSE_DETAIL");
                    redirectNA.append(txnNA);
                }
                
                if (nodeArray != null) {
                    if (nodeArray.getSize() > 0
                            && nodeArray.exist("Action")
                            && !nodeArray.find("Action").isValueEmpty()
                            && nodeArray.find("Action").getStringValue().equals("Save")) {
                        
                        callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);

                        txnNA = executeTransaction(callTxnNA);
                        if (txnNA != null) {
                            redirectNA.append(txnNA);
                        }
                    }
                }
                
                // Solo si hay sesión, cargar la tabla de “Embarques creados”
                if (isLogged) {
                    // Esto hace que quede marcada la opcion en el menu y ademas para URL dinamico en el form
                    session.setAttribute("acordion", "Shipping");
                    controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
                    controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
                    session.setAttribute("Controller", controller);
                    
                    callTxnNA.add("idUser", session.getAttribute("idUser").toString());
                    
                    // Evaluate Truck
                    if (nodeArray != null) {
                        if (nodeArray.getSize() > 0
                                && nodeArray.exist("Action")
                                && !nodeArray.find("Action").isValueEmpty()
                                && nodeArray.find("Action").getStringValue().equals("Release")) {
                            
                            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveEvaluate);

                            txnNA = executeTransaction(callTxnNA);
                            if (txnNA != null) {
                                redirectNA.append(txnNA);
                            }
                        }
                    }
                    
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(transferOrderTxn);
                    callTxnNA.add("status", "Nueva");
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        txnNA.remove("RESPONSE_CODE");
                        txnNA.remove("RESPONSE_MESSAGE");
                        txnNA.remove("RESPONSE_DETAIL");
                        redirectNA.append(txnNA);
                    }
                }

                // Página a renderizar
                redirectNA.add("JspPage", "/newStockTransferOrder.page");
                
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