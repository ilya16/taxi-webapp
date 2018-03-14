package controllers;

import model.pojo.Ride;
import model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.ServiceException;
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
import java.util.ArrayList;
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
        LOGGER.info("OrderHistoryServlet doGet is executing");

        Integer userId = (Integer)req.getSession().getAttribute("userId");
        List<Ride> userRides = getAllUserRides(userId);

        req.setAttribute("rides", userRides);

        try {
            User user = userService.getUser(userId);
            req.setAttribute("user", user);
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        try {
            req.getRequestDispatcher("/history.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("TaxiOrderingServlet doPost is executing");

        String idToCancel = req.getParameter("idToCancel");

        if (idToCancel != null) {
            Integer rideId;
            try {
                rideId = Integer.parseInt(idToCancel);
                taxiOrderingService.cancelOrder(rideId);

                req.getSession().setAttribute("responseMessage", "Status was successfully updated!");
            } catch (NumberFormatException | ServiceException e) {
                LOGGER.error(e);
                req.getSession().setAttribute("responseMessage",
                        "An error occurred while updating the status. Please, try again!");
            }

            Integer userId = (Integer)req.getSession().getAttribute("userId");
            List<Ride> userRides = getAllUserRides(userId);

            req.setAttribute("rides", userRides);

            try {
                req.getRequestDispatcher("/history.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                LOGGER.error(e);
                throw e;
            }
        }
    }

    private List<Ride> getAllUserRides(Integer userId) {
        List<Ride> userRides;
        try {
            userRides = taxiOrderingService.getAllUserRides(userId);
        } catch (ServiceException e) {
            LOGGER.error(e);
            userRides = new ArrayList<>();
        }
        return userRides;
    }
}
