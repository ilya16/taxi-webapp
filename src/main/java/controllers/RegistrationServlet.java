package controllers;

import model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ServiceException;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "sign-up", urlPatterns = {"/sign-up"})
public class RegistrationServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(LoginServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(RegistrationServlet.class);

    private static UserServiceImpl userService = new UserServiceImpl() ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("RegistrationServlet doGet is executing");

        req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("RegistrationServlet doPost is executing");

        String login = req.getParameter("login");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");

        req.setAttribute("login", login);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);

        if (password.length() < 6) {
            req.getSession().setAttribute("responseMessage",
                    "Password should be at least 6 symbols long");
            req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
        }
        else if (!password.equals(passwordConfirm)) {
            req.getSession().setAttribute("responseMessage",
                    "Passwords do not match");
            req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
        }
        else {
            try {
                User user = userService.register(login, firstName, lastName, password);

                LOGGER.info(String.format("User with login=%s has successfully registered in the system",
                        user.getLogin()));

                req.getSession().setAttribute("responseMessage",
                        "Registration was successful! Sign into the System below:");
                resp.sendRedirect("/login");
            } catch (ServiceException e) {
                LOGGER.error(e);

                req.getSession().setAttribute("responseMessage",
                        String.format("Login \"%s\" is already taken, enter another one", login));
                req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
            }
        }
    }
}
