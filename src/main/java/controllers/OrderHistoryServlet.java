package controllers;

import model.beans.City;
import model.beans.Ride;
import model.beans.TaxiService;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.api.TaxiOrderingService;
import services.api.UserService;
import services.impl.TaxiOrderingServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "order-history", urlPatterns = {"/history"})
public class OrderHistoryServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(OrderHistoryServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(OrderHistoryServlet.class);

    private static TaxiOrderingService taxiOrderingService = new TaxiOrderingServiceImpl();
    private static UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("OrderHistoryServlet doGet");

        Integer userId = (Integer)req.getSession().getAttribute("userId");
        List<Ride> rides = taxiOrderingService.getAllUserRides(userId);

        req.setAttribute("rides", rides);
        req.setAttribute("user", userService.getUser(userId));

        req.getRequestDispatcher("/history.jsp").forward(req, resp);
    }
}
