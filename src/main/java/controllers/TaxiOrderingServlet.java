package controllers;

import model.pojo.City;
import model.pojo.Ride;
import model.pojo.TaxiService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "taxi-ordering", urlPatterns = {"/order-taxi"})
public class TaxiOrderingServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(TaxiOrderingServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServlet.class);

    private static TaxiOrderingService taxiOrderingService = new TaxiOrderingServiceImpl();
    private static UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("TaxiOrderingServlet doGet is executing");

        /* Getting only supported cities and and active taxi services */
        List<City> cities;
        try {
            cities = taxiOrderingService.getAllCities(true);
        } catch (ServiceException e) {
            LOGGER.error(e);
            cities = new ArrayList<>();
        }

        Map<City, List<TaxiService>> cityTaxiServices;
        try {
            cityTaxiServices = taxiOrderingService.getAllCityTaxiServices(cities, true);
        } catch (ServiceException e) {
            LOGGER.error(e);
            cityTaxiServices = new HashMap<>();
        }

        req.setAttribute("cityServices", cityTaxiServices);

        try {
            User user = userService.getUser((Integer)req.getSession().getAttribute("userId"));
            req.setAttribute("user", user);
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        try {
            req.getRequestDispatcher("/order-taxi.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            LOGGER.error(e);
            throw e;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("TaxiOrderingServlet doPost is executing");

        boolean validationSuccess = true;

        Integer cityId = 0;
        String saveCity = req.getParameter("saveCity");
        String locationFrom = req.getParameter("locationFrom");
        String locationTo = req.getParameter("locationTo");
        String phoneNumber = req.getParameter("phoneNumber");
        String savePhoneNumber = req.getParameter("savePhoneNumber");
        Integer serviceId = 0;
        String childSeat = req.getParameter("childSeat");
        String orderComments = req.getParameter("orderComments");

        /* Parameter validation */
        try {
            cityId = Integer.parseInt(req.getParameter("city"));
        } catch (NumberFormatException e) {
            LOGGER.error(e);
            LOGGER.debug(String.format("Invalid cityId - %s", req.getParameter("city")));
            validationSuccess = false;
        }

        try {
            serviceId = Integer.parseInt(req.getParameter("service"));
        } catch (NumberFormatException e) {
            LOGGER.error(e);
            LOGGER.debug(String.format("Invalid serviceId - %s", req.getParameter("service")));
            validationSuccess = false;
        }

        LOGGER.debug("Placing the order, sending all parameters to TaxiOrderingService");
        Ride ride;
        if (validationSuccess) {
            try {
                ride = taxiOrderingService.placeOrder(
                        (Integer)req.getSession().getAttribute("userId"), serviceId,
                        cityId, saveCity != null, locationFrom, locationTo,
                        phoneNumber, savePhoneNumber != null,
                        childSeat != null, orderComments
                );

                LOGGER.info("Order was successfully placed");

                req.getSession().setAttribute("orderSuccess", true);
                req.getSession().setAttribute("responseMessage", "Order was successfully placed!");
                req.setAttribute("ride", ride);
            } catch (ServiceException e) {
                LOGGER.error(e);

                LOGGER.info("Order was not placed");

                req.getSession().setAttribute("orderSuccess", false);
                req.getSession().setAttribute(
                        "responseMessage",
                        "An error occurred while placing the order :(\n" +
                                "We are sorry, probably, no drivers are busy right now. Please, try again later."
                );
            }

            try {
                req.getRequestDispatcher("/order-taxi.jsp").forward(req, resp);
            } catch (ServletException | IOException e) {
                LOGGER.error(e);
                throw e;
            }
        }
    }
}
