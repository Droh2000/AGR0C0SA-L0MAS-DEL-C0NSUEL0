package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import xmlNodeArray.xmlNodeArray;

public class NewPalletServlet extends SIDServlet {

    String palletDefinitionTxn = "AgrocosaTransactions.PalletDefinitionMenuTransaction";
    String palletProductTxn = "AgrocosaTransactions.PalletProductMenuTransaction";
    String storageTxn = "AgrocosaTransactions.StorageMenuTransaction";
    String saveTxn = "AgrocosaTransactions.SaveNewPalletTransaction";
    String palletInfoTxn = "AgrocosaTransactions.RetrievePalletInfoTransaction";
    String printTxn = "AgrocosaTransactions.PrintPalletInfoTransaction";
    String campTxn = "AgrocosaTransactions.CampMenuTransaction";
    String printZebra = "AgrocosaTransactions.PrintPalletZPLTransaction";
    String cancelPallet = "AgrocosaTransactions.CancelPalletTransaction";
    String printerDefinitionTxn = "AgrocosaTransactions.PrinterDefinitionMenuTransaction";
    
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

            session.setAttribute("acordion", "Shipping");
            controller = controller.replaceAll("Agrocosa.", "").replaceAll("Servlet", ".do");
            controller = controller.replaceFirst(controller.substring(0, 1), controller.substring(0, 1).toLowerCase());
            session.setAttribute("Controller", controller);

            //Pallet Definition
            callTxnNA.add("idUser", session.getAttribute("idUser").toString());
            callTxnNA.add("TRANSACTION_CLASS_TO_EXECUTE", palletDefinitionTxn);
            callTxnNA.append(nodeArray);
            txnNA = executeTransaction(callTxnNA);
            
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            // Get Printers Name
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(printerDefinitionTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //Pallet Product
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(palletProductTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            //Storage
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(storageTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            // Camp
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(campTxn);
            txnNA = executeTransaction(callTxnNA);

            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }
            
            /*if (nodeArray.exist("CampName") && !nodeArray.find("CampName").isValueEmpty()) {
                callTxnNA.add("CampName", nodeArray.find("CampName").getStringValue());
            }*/

            //save
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("Action")
                        && !nodeArray.find("Action").isValueEmpty()
                        && nodeArray.find("Action").getStringValue().equals("Save")) {
                    callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(saveTxn);
                    
                    txnNA = executeTransaction(callTxnNA);
                    if (txnNA != null) {
                        redirectNA.append(txnNA);

                        //print
                        callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(printTxn);
                        txnNA = executeTransaction(callTxnNA);
                        if (txnNA != null) {
                            if (txnNA.exist("FileGeneratedId")) {
                                //redirectNA.add("FileGeneratedId", txnNA.find("FileGeneratedId").getIntValue());
                                int fileId = txnNA.find("FileGeneratedId").getIntValue();
                                redirectNA.add("FileGeneratedId", fileId);  // ya lo haces
                                //request.setAttribute("FileGeneratedId", fileId); 
                            }
                            txnNA.remove("RESPONSE_CODE");
                            txnNA.remove("RESPONSE_MESSAGE");
                            txnNA.remove("RESPONSE_DETAIL");
                            redirectNA.append(txnNA);
                        }
                    }
                }
            }
            
            if (nodeArray != null) {
                        if (nodeArray.getSize() > 0
                                && nodeArray.exist("Action")
                                && !nodeArray.find("Action").isValueEmpty()
                                && nodeArray.find("Action").getStringValue().equals("PrintZebra")) {
                           callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(printZebra);
                           callTxnNA.append(nodeArray);

                           txnNA = executeTransaction(callTxnNA);
                           
                           if (txnNA != null) {
                                response.setContentType("application/json");
                                String key = "ZPL_DATA";
                                String content = txnNA.getNodeArray().get(0).getStringValue();
                                
                                // Escapar caracteres especiales para que sea válido JSON (Aqui generamos un JSON)
                                String escapedContent = content
                                        .replace("\\", "\\\\")   // primero escapamos backslashes
                                        .replace("\"", "\\\"")   // luego escapamos comillas
                                        .replace("\n", "\\n");   // luego saltos de línea

                                StringBuilder jsonBuilder = new StringBuilder();
                                jsonBuilder.append("{");
                                jsonBuilder.append("\"").append(key).append("\":");

                                // Lote de etiqueta generado
                                jsonBuilder.append("\"").append(escapedContent).append("\"");
                                
                                jsonBuilder.append("}");
                                String jsonString = jsonBuilder.toString();
                                
                                response.getWriter().write(jsonString);
                                redirectNA.append(txnNA);
                           }
                           return;
                }
            }
            
            if (nodeArray != null) {
                        if (nodeArray.getSize() > 0
                                && nodeArray.exist("Action")
                                && !nodeArray.find("Action").isValueEmpty()
                                && nodeArray.find("Action").getStringValue().equals("CancelPalletID")) {
                           callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(cancelPallet);

                           txnNA = executeTransaction(callTxnNA);
                           
                           if (txnNA != null) {
                                redirectNA.append(txnNA);
                           }
                }
            }
            
            //new saved
            callTxnNA.find("TRANSACTION_CLASS_TO_EXECUTE").setValue(palletInfoTxn);
            txnNA = executeTransaction(callTxnNA);
            if (txnNA != null) {
                txnNA.remove("RESPONSE_CODE");
                txnNA.remove("RESPONSE_MESSAGE");
                txnNA.remove("RESPONSE_DETAIL");
                redirectNA.append(txnNA);
            }

            if (redirectNA != null) {
                redirectNA.add("JspPage", "/newPallet.page");
                
                session.setAttribute("TxData", redirectNA);
                response.sendRedirect("management.do");
            } else {
                rd = request.getRequestDispatcher("/newPallet.page");
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
