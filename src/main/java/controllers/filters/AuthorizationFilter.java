package controllers.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters the user requests depending on user authorization in the system.
 */
@WebFilter(urlPatterns = {"/order-taxi", "/history"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String userLogin = (String) ((HttpServletRequest) servletRequest)
                .getSession().getAttribute("userLogin");

        if (userLogin != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletRequest) servletRequest).getSession().setAttribute(
                    "responseMessage",
                    "You should authorize to get access to the system"
            );
            ((HttpServletResponse) servletResponse).sendRedirect(
                    ((HttpServletRequest) servletRequest).getContextPath() + "/login"
            );
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
