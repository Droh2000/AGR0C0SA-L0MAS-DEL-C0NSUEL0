package Agrocosa;

import SIDWebEngine.*;
import xmlNodeArray.xmlNodeArray;
import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReadCampDocumentServlet extends SIDServlet {

    // Servlet configuration variables
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param nodeArray
     */
    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        Connection con = null;
        ResultSet rs = null;
        boolean empty = false;
        HttpSession session = null;
        ServletOutputStream out = null;

        try {
            session = request.getSession();

            SIDDataBase database = new SIDDataBase();


            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("idCamp")
                        && !nodeArray.find("idCamp").isValueEmpty()) {
                    String idCamp = nodeArray.find("idCamp").getStringValue();

                    response.setContentType("application/pdf");
                    response.setHeader("Content-disposition", "inline; filename=" + idCamp + ".pdf");
                    
                    out = response.getOutputStream();

                    con = database.GetConnection();
                    con.setAutoCommit(false);

                    String query = "Select Picture From camp Where idCamp = " + idCamp + " ";
                    rs = con.createStatement().executeQuery(query);
                    if (rs.next()) {
                        out.write(rs.getBytes("Picture"));
                    }
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                    out.flush();
                    out.close();
                }
            }

        } catch (SQLException sqle) {
            request.setAttribute("RESPONSE_CODE", "FAIL");
            request.setAttribute("RESPONSE_MESSAGE", "SQL_EXCEPTION_ERROR" + this.getClass().getName());
            request.setAttribute("RESPONSE_DETAIL", sqle.getMessage());
        } catch (IOException ioe) {
            try {
                con.rollback();
            } catch (SQLException sqle) {
                request.setAttribute("RESPONSE_CODE", "FAIL");
                request.setAttribute("RESPONSE_MESSAGE", "SQL_EXCEPTION_ERROR" + this.getClass().getName());
                request.setAttribute("RESPONSE_DETAIL", sqle.getMessage());
            }
            request.setAttribute("RESPONSE_CODE", "FAIL");
            request.setAttribute("RESPONSE_MESSAGE", "IO_EXCEPTION_ERROR" + ". " + "<" + this.getClass().getName());
            request.setAttribute("RESPONSE_DETAIL", ioe.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (con != null && !con.isClosed()) {
                    con.close();
                    con = null;
                }
            } catch (SQLException sqle) {
                request.setAttribute("RESPONSE_CODE", "FAIL");
                request.setAttribute("RESPONSE_MESSAGE", "SQL_EXCEPTION_ERROR" + this.getClass().getName());
                request.setAttribute("RESPONSE_DETAIL", sqle.getMessage());
            }
        }
    }
}
