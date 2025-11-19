package AgrocosaSecurity;

import SIDWebEngine.SIDServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import xmlNodeArray.xmlNodeArray;

/**
 *
 * @author fga005
 */
public class SIDLogOutServlet extends SIDServlet
{
   @Override
   protected void processRequest(HttpServletRequest request,
                                 HttpServletResponse response,
                                 xmlNodeArray nodeArray) throws ServletException, IOException
   {
      HttpSession session = null;

      session = request.getSession();
      session.invalidate();
      
      //response.sendRedirect(request.getContextPath() + "/public/main.do");
      response.sendRedirect(request.getContextPath());
   }
}
