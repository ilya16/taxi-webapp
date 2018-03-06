package controllers;

import model.beans.City;
import model.beans.Ride;
import model.beans.TaxiService;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.api.TaxiOrderingService;
import services.impl.TaxiOrderingServiceImpl;

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

@WebServlet(name = "taxi-ordering", urlPatterns = {"/taxi-ordering"})
public class TaxiOrderingServlet extends HttpServlet {
    static {
        PropertyConfigurator.configure(TaxiOrderingServlet.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServlet.class);

    private static TaxiOrderingService taxiOrderingService = new TaxiOrderingServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("TaxiOrderingServlet doGet");

        /* Getting only supported cities and and active taxi services */
        List<City> cities = taxiOrderingService.getAllCities(true);
        Map<City, List<TaxiService>> cityTaxiServices = taxiOrderingService.getAllCityTaxiServices(cities, true);

        req.setAttribute("cityServices", cityTaxiServices);

        req.getRequestDispatcher("/taxi-ordering.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("TaxiOrderingServlet doPost");

        String cityId = req.getParameter("city");
        String saveCity = req.getParameter("saveCity");
        String locationFrom = req.getParameter("locationFrom");
        String locationTo = req.getParameter("locationTo");
        String phoneNumber = req.getParameter("phoneNumber");
        String savePhoneNumber = req.getParameter("savePhoneNumber");
        String serviceId = req.getParameter("service");
        String childSeat = req.getParameter("childSeat");
        String orderComments = req.getParameter("orderComments");

        //TODO: phone number check

        resp.sendRedirect("/");
        Ride ride = taxiOrderingService.placeOrder(
                Integer.toString((Integer)req.getSession().getAttribute("userId")),
                cityId, saveCity, locationFrom, locationTo, phoneNumber, savePhoneNumber, serviceId,
                childSeat, orderComments);

        if (ride != null) {
            req.getSession().setAttribute("orderSuccess", true);
            req.getSession().setAttribute("responseMessage", "Order was successfully placed!");
            req.getRequestDispatcher("/taxi-ordering.jsp").forward(req, resp);
        } else {
            req.getSession().setAttribute("orderSuccess", false);
            req.getSession().setAttribute("responseMessage", "An error occurred while placing the order :(");
            req.getRequestDispatcher("/taxi-ordering.jsp").forward(req, resp);
        }
    }
}
