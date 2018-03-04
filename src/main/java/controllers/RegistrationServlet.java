package controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "sign-up", urlPatterns = {"/sign-up"})
public class RegistrationServlet extends HttpServlet {
    //    static {
//        PropertyConfigurator.configure(LoginServlet.class.getClassLoader().getResource("log4j.properties"));
//    }

    private static final Logger LOGGER = LogManager.getLogger(RegistrationServlet.class);

    private static UserServiceImpl userService = new UserServiceImpl() ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("RegistrationServlet doGet");
        req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("RegistrationServlet doPost");

        String login = req.getParameter("login");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");

        if (!password.equals(passwordConfirm)) {
            req.setAttribute("responseMessage", "Passwords do not match");
            req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
        } else if (userService.register(login, firstName, lastName, password) != null) {
            req.getSession().setAttribute("userLogin", login);
            req.setAttribute("responseMessage", "Registration was successful! Sign inn to the System below:");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
//
//            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("responseMessage", String.format("Login \"%s\" is already taken, enter another one", login));
            req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
        }
    }
}
