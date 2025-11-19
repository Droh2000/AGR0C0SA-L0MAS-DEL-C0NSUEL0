/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xmlNodeArray.xmlNode;
import xmlNodeArray.xmlNodeArray;
import xmlNodeArray.xmlTable;

/**
 *
 * @author Sly
 */
public class ManagementServlet extends SIDServlet {

    //Implementing Post/Redirect/Get (PRG) Pattern:
    //      http://www.skywayperspectives.org/documentation/6.3/chunk/recipes/ch02s14.html
    //      http://theopentutorials.com/tutorials/java/design-patterns/post-redirect-get-prg-pattern-in-servlet-jsp/
    
    

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        
        xmlNodeArray txnNA = (xmlNodeArray) request.getSession().getAttribute("TxData");
        
        request = this.parseTxn(request, txnNA);
        
        String redirect = request.getAttribute("JspPage").toString();
        RequestDispatcher view = request.getRequestDispatcher(redirect);
        view.forward(request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, xmlNodeArray inputParameter) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected HttpServletRequest parseTxn(HttpServletRequest request, xmlNodeArray inputArr) {

        if (inputArr != null) {
            for (int x = 0; x < inputArr.getSize(); x++) {
                xmlNode node = inputArr.getNode(x);
                if (node.getTable() == null && !node.isValueNull()) {
                    request.setAttribute(node.getName(), node.getStringValue());
                }
                if (node.getTable() != null) {
                    if (node.getName().endsWith("_Table")) {
                        request.setAttribute(node.getName(), formatNodeArrayToTable(node.getName(), inputArr));
                    }
                    else if (node.getName().endsWith("_HashMap")
                            && node.getTable().getFieldsQty() == 2) {
                        request.setAttribute(node.getName(), formatNodeArray(node.getName(), inputArr));                   
                    } else {
                        request.setAttribute("RESPONSE_CODE", "FAIL");
                        request.setAttribute("RESPONSE_MESSAGE", "Undefined xmlTable object");
                        request.setAttribute("RESPONSE_DETAIL", "SIDServlet.parseTxnResponse");
                    }
                }
            }
        }

        return request;
    }
    
    protected HashMap formatNodeArray(String tabName, xmlNodeArray inputArr) {
        xmlTable dataTab = null;
        HashMap map = new HashMap();

        if (inputArr != null) {
            if (inputArr.find(tabName) != null
                    && inputArr.find(tabName).getTable() != null) {
                dataTab = inputArr.find(tabName).getTable();
                for (int x = 0; x < dataTab.getRowsQty(); x++) {
                    map.put(dataTab.getStringValue(x, 0), dataTab.getStringValue(x, 1));
                }
            } else {
                map.put("RESPONSE_CODE", "FAIL");
                map.put("RESPONSE_MESSAGE", "Undefined xmlTable object");
                map.put("RESPONSE_DETAIL", "SIDServlet.formatNodeArrayToHashMap method");
            }
        } else {
            map.put("RESPONSE_CODE", "FAIL");
            map.put("RESPONSE_MESSAGE", "Undefined xmlNodeArray - null object");
            map.put("RESPONSE_DETAIL", "SIDServlet.formatNodeArrayToHashMap method");
        }
        map = sortHasMapValues(map);
        return map;
    }      
}
