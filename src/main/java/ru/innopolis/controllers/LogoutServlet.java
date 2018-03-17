package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(LogoutServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("LogoutServlet doGet is executing");

        req.getSession().removeAttribute("userLogin");
        req.getSession().removeAttribute("userId");

        try {
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }
}
