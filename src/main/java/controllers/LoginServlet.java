package controllers;

import model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ServiceException;
import services.api.UserService;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(LoginServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    private static UserService userService = new UserServiceImpl() ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("LoginServlet doGet is executing");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("LoginServlet doPost is executing");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = userService.auth(login, password);

            LOGGER.info(String.format("User with login=%s has successfully authorized in the system", login));

            req.getSession().setAttribute("userLogin", login);
            req.getSession().setAttribute("userId", user.getId());
            resp.sendRedirect(req.getContextPath() + "/order-taxi");
        } catch (ServiceException e) {
            LOGGER.error(e);
            req.getSession().setAttribute("responseMessage",
                    "Incorrect login/password pair. Please, try again.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
