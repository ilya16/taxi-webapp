package ru.innopolis.controllers.filters;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    static {
        PropertyConfigurator.configure(AuthorizationFilter.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(AuthorizationFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        LOGGER.debug("AuthorizationFilter is executing");

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
        // nothing additional is done on filter initialization
    }

    @Override
    public void destroy() {
        // nothing additional is done on filter destroy
    }
}
