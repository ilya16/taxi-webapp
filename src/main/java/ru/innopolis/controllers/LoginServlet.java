package ru.innopolis.controllers;

import ru.innopolis.model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.UserService;
import ru.innopolis.services.impl.UserServiceImpl;

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

        try {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            LOGGER.error(e);
            throw e;
        }
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

            try {
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            } catch (ServletException | IOException e1) {
                LOGGER.error(e1);
                throw e1;
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }
}
