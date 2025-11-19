package Agrocosa;

import SIDWebEngine.*;
import xmlNodeArray.xmlNodeArray;
import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadPalletIDToPrintServlet extends SIDServlet {

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
        ServletOutputStream out = null;
        String query;
        PreparedStatement pstmt;

        try {
            SIDDataBase database = new SIDDataBase();

            
            if (nodeArray != null) {
                if (nodeArray.getSize() > 0
                        && nodeArray.exist("idDoc")
                        && !nodeArray.find("idDoc").isValueEmpty()) {

                    response.setContentType("application/pdf");
                    response.setHeader("Content-disposition", "inline; filename=AgrocosaPalletID.pdf");
                    
                    out = response.getOutputStream();

                    con = database.GetConnection();
                    con.setAutoCommit(false);

                    query = "SELECT File FROM palletinfofile WHERE idPalletInfoFile = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, Integer.parseInt(nodeArray.find("idDoc").getStringValue()));
                    rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        out.write(rs.getBytes("File"));
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
