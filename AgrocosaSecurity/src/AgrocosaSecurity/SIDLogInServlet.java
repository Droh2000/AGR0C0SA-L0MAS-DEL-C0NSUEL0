package AgrocosaSecurity;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import xmlNodeArray.xmlNodeArray;



public class SIDLogInServlet extends SIDServlet {

    /** Redirect to login page
     * @param request nothing
     * @param response reference to response to browser
     * @param nodeArray
     * @throws ServletException
     * @throws IOException
     */
     @Override
     protected void processRequest(HttpServletRequest request,
                                   HttpServletResponse response,
                                   xmlNodeArray nodeArray) throws ServletException,
                                                             IOException
     {
         HttpSession session = request.getSession();
         RequestDispatcher rd = context.getRequestDispatcher("/public/siteLogin.do");

         if (session.getAttribute("error") != null)
         {
            request.setAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
         }

         rd.forward(request, response);
     }
}
