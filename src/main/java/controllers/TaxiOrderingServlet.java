package controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "taxi-ordering", urlPatterns = {"/taxi-ordering"})
public class TaxiOrderingServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(TaxiOrderingServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("TaxiOrderingServlet doGet");
        req.getRequestDispatcher("/taxi-ordering.jsp").forward(req, resp);
    }
}
