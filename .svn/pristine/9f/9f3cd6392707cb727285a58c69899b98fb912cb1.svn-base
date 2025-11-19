package AgrocosaSecurity;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SIDSecurityFilter implements Filter
{
    private FilterConfig filterConfig = null;

    /** Filter initialization
     * @param filterConfig reference to filter configuration
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    /** Filter finalization
     */
    @Override
    public void destroy()
    {
        this.filterConfig = null;
    }

  /** Verify if user has a valid session when attempting to access a page.
    * If user has a valid session then grant access to the target
    * page, otherwise, redirect the user to login page
    *
    * @param request
    * @param response
    * @param chain
    * @throws IOException
    * @throws ServletException
    */
    @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      HttpSession session = null;
      session = ((HttpServletRequest) request).getSession(false);


      if (session == null || session.getAttribute("username") == null)
      {
         //session.setAttribute("requestAddress", ((HttpServletRequest) request).getServletPath());
         //((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/public/login");
         ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath());
      }
      else
      {
        ((HttpServletResponse) response).setHeader("Cache-Control","no-cache, no-store, must-revalidate");
        ((HttpServletResponse) response).setHeader("Pragma","no-cache");
        ((HttpServletResponse) response).setDateHeader ("Expires", 0);

          chain.doFilter(request, response);
      }
    }
   
}
