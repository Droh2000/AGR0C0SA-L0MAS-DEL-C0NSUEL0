package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;
import xmlNodeArray.xmlTable;

public class StockTransferOrderReportServlet extends SIDServlet {

    String transportCompanyTxn = "AgrocosaTransactions.TransportCompanyMenuTransaction";
    String reportTxn = "AgrocosaTransactions.RetrieveTruckTransferOrderTransaction";
    String retrieveEvaluationTxn = "AgrocosaTransactions.RetrieveTransportEvaluationTransaction";

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
        xmlTable tab = null;

        try {
            session = request.getSession();
            callTxnNA = new xmlNodeArray();
            redirectNA = new xmlNodeArray();

            session.setAttribute("acordion", "Report");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", transportCompanyTxn);
            callTxnNA.append(nodeArray);
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
                        && nodeArray.find("Action").getStringValue().equals("Search")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(reportTxn);

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

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/stockTransferOrderReport.page");
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/stockTransferOrderReport.page");
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
