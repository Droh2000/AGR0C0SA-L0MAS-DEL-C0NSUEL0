package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;
import xmlNodeArray.xmlTable;

public class AssignWeightRampServlet extends SIDServlet {
    
    String transportCompanyTxn = "AgrocosaTransactions.TransportCompanyMenuTransaction";
    String saveWeightTareTxn = "AgrocosaTransactions.SaveTransportWeightTareTransaction";
    String saveRampTxn = "AgrocosaTransactions.SaveTransportRampTransaction";
    String saveWeightNetTxn = "AgrocosaTransactions.SaveTransportWeightNetTransaction";
    String retrieveWeightedTxn = "AgrocosaTransactions.RetrieveTransportWeightedTransaction";
    String retrieveEvaluationTxn = "AgrocosaTransactions.RetrieveTransportEvaluationTransaction";
    
    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        HttpSession session = null;
        xmlNodeArray callTxnNA = null;
        xmlNodeArray txnNA = null;
        String controller = this.getClass().getName();
        xmlNodeArray redirectNA = null;
        xmlTable tab = null;
        
        try {
            // Menús/catálogos
            session = request.getSession();
            redirectNA = new xmlNodeArray();
            callTxnNA = new xmlNodeArray();
            
            // Esto hace que quede marcada la opcion en el menu y ademas para URL dinamico en el form
            session.setAttribute("acordion", "Shipping");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);
            
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            
            // Siempre agregar con ADD el "TRANSACTION_CLASS_TO_EXECUTE"
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", "");
            callTxnNA.append(nodeArray); // Le pasamos todos los elementos del HTML
            
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(transportCompanyTxn);
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
                        && nodeArray.find("Action").getStringValue().equals("WeightTare")) {

                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveWeightTareTxn);

                    txnNA = executeTransaction(callTxnNA);
                    
                    // Responder JSON (sin PRG) SOLO para inline-edit
                    response.setContentType("application/json;charset=UTF-8");
                    String code = (txnNA != null && txnNA.exist("RESPONSE_CODE"))
                                  ? txnNA.find("RESPONSE_CODE").getStringValue() : "FAIL";
                    String msg  = (txnNA != null && txnNA.exist("RESPONSE_MESSAGE"))
                                  ? txnNA.find("RESPONSE_MESSAGE").getStringValue() : "No se pudo guardar";

                    response.getWriter().write("{\"code\":\""+code+"\",\"message\":\""+msg+"\"}");
                    return; // <- importante: no sigas al flujo PRG en este request
                }
            }
            
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Ramp")) {

                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveRampTxn);

                    txnNA = executeTransaction(callTxnNA);
                   
                    // Responder JSON (sin PRG) SOLO para inline-edit
                    response.setContentType("application/json;charset=UTF-8");
                    String code = (txnNA != null && txnNA.exist("RESPONSE_CODE"))
                                  ? txnNA.find("RESPONSE_CODE").getStringValue() : "FAIL";
                    String msg  = (txnNA != null && txnNA.exist("RESPONSE_MESSAGE"))
                                  ? txnNA.find("RESPONSE_MESSAGE").getStringValue() : "No se pudo guardar";

                    response.getWriter().write("{\"code\":\""+code+"\",\"message\":\""+msg+"\"}");
                    return; // <- importante: no sigas al flujo PRG en este request
                }
            }

            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && (nodeArray.find("Action").getStringValue().equals("WeightNetNot")
                        || nodeArray.find("Action").getStringValue().equals("WeightNetYes"))) {

                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveWeightNetTxn);; 

                    // aquí debe venir idStockTransferOrder, action
                    callTxnNA.add("idStockTransferOrder", nodeArray.findNodeArray("idStockTransferOrder").toString());
                    callTxnNA.add("typeButton", "No");
                    
                    // Si es SI insertar HTML al Modal
                    if(nodeArray.find("Action").getStringValue().equals("WeightNetYes")){
                        callTxnNA.add("gross", nodeArray.findNodeArray("gross").toString());
                        callTxnNA.find("typeButton").setValue("Si");
                    }
                    
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);
                    }
                }
            }

            // Evaluate Truck
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Evaluation")) {
                    
                    response.setContentType("text/html;charset=UTF-8");
                    
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(retrieveEvaluationTxn);
                    
                    callTxnNA.append(nodeArray); // aquí debe venir idStockTransferOrder

                    txnNA = executeTransaction(callTxnNA);

                    String html = "";
                    if (txnNA != null && txnNA.exist("TransportEvaluationInfo_Table")) {
                        tab = txnNA.find("TransportEvaluationInfo_Table").getTable();
                        // Genera un HTML simple para el modal (puede ser una mini tabla o form)
                        // Ejemplo básico:
                        if (tab.getRowsQty() > 0) {
                            html += "<div><b>Limpieza de Caja:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "TruckBoxCleaning")) + "</div>";
                            html += "<div><b>Condiciones de Mantenimiento:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "MaintenanceConditions")) + "</div>";
                            html += "<div><b>Ventilas Abiertas:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "OpenWindows")) + "</div>";
                            html += "<div><b>Tarimas Selladas:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "SealedPallets")) + "</div>";
                            html += "<div><b>Libre de Plagas:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "PestFree")) + "</div>";
                            html += "<div><b>Libre Aromas Extraños:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "SmellStrangeFree")) + "</div>";
                            html += "<div><b>Etiqueta:</b> " + "&nbsp;&nbsp;" + showIconFromMessage(tab.getStringValue(0, "Label")) + "</div>";
                        } else {
                            html = "<div>No se encontraron datos de evaluación.</div>";
                        }
                    } else {
                        html = "<div class='text-danger'>Sin datos o error en la transacción.</div>";
                    }

                    response.getWriter().write(html);
                    return; // No seguir al flujo PRG
                }
            }

            // Va despues para que se actualize la tabla y ya no salgan los elementos que ya fueron evaluados
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(retrieveWeightedTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            // Página a renderizar
            redirectNA.add("JspPage", "/assignWeightRamp.page");
            
            // PRG → ManagementServlet
            session.setAttribute("TxData", redirectNA);

            // Usar SIEMPRE el contextPath para no romper rutas
            String ctx = request.getContextPath();
            response.sendRedirect(ctx + "/protected/management.do");

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
    
    // Cambiar de icono segun el mensaje de la evaluacion
    public String showIconFromMessage(String message){
        if(message.equals("Aceptable") || message.equals("Si")){
            return "<i class=\"fa fa-check-square\" style=\"color: green;\"></i>";
        }
        
        if(message.equals("No Aceptable") || message.equals("No")){
            return "<i class=\"fa fa-times-circle\" style=\"color: red;\"></i>";
        }
        
        return "Icon Not Implemented";
    }
}