package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.innopolis.forms.CancelOrderForm;
import ru.innopolis.forms.TaxiOrderForm;
import ru.innopolis.model.pojo.City;
import ru.innopolis.model.pojo.Ride;
import ru.innopolis.model.pojo.TaxiService;
import ru.innopolis.model.pojo.User;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.TaxiOrderingService;
import ru.innopolis.services.api.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TaxiServiceController {
    static {
        PropertyConfigurator.configure(TaxiServiceController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiServiceController.class);

    @Autowired
    private TaxiOrderingService taxiOrderingService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/order-taxi")
    ModelAndView orderTaxiGet(TaxiOrderForm taxiOrderForm, HttpSession session) {
        LOGGER.info("TaxiServiceController: Order-Taxi page GET");

        ModelAndView modelAndView = new ModelAndView("order-taxi");

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

        modelAndView.addObject("cityServices", cityTaxiServices);

        try {
            User user = userService.getUser((Integer)session.getAttribute("userId"));
            taxiOrderForm.setPhoneNumber(user.getPhoneNumber());
            modelAndView.addObject("cityId", user.getCityId());
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        return modelAndView;
    }

    @PostMapping(value = "/order-taxi")
    ModelAndView orderTaxiPost(TaxiOrderForm taxiOrderForm, BindingResult bindingResult, HttpSession session) {
        LOGGER.info("TaxiServiceController: Order-Taxi page POST");

        ModelAndView modelAndView = new ModelAndView("order-taxi");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("responseMessage",
                    "An error occurred while placing the order :(\n");
            return modelAndView;
        }

        Integer cityId = taxiOrderForm.getCityId();
        boolean saveCity = taxiOrderForm.isSaveCity();
        String locationFrom = taxiOrderForm.getLocationFrom();
        String locationTo = taxiOrderForm.getLocationTo();
        String phoneNumber = taxiOrderForm.getPhoneNumber();
        boolean savePhoneNumber = taxiOrderForm.isSavePhoneNumber();
        Integer serviceId = taxiOrderForm.getServiceId();
        boolean childSeat = taxiOrderForm.isChildSeat();
        String orderComments = taxiOrderForm.getOrderComments();

        LOGGER.debug("Placing the order, sending all parameters to TaxiOrderingService");

        try {
            Ride ride = taxiOrderingService.placeOrder(
                    (Integer)session.getAttribute("userId"), serviceId,
                    cityId, saveCity, locationFrom, locationTo,
                    phoneNumber, savePhoneNumber,
                    childSeat, orderComments
            );

            LOGGER.info("Order was successfully placed");

            taxiOrderForm.setLocationFrom("");
            taxiOrderForm.setLocationTo("");
            taxiOrderForm.setOrderComments("");

            modelAndView.addObject("orderSuccess", true);
            modelAndView.addObject("responseMessage", "Order was successfully placed!");
            modelAndView.addObject("ride", ride);
            modelAndView.addObject("cityId", cityId);
        } catch (ServiceException e) {
            LOGGER.error(e);

            LOGGER.info("Order was not placed");

            modelAndView.addObject("orderSuccess", false);
            modelAndView.addObject(
                    "responseMessage",
                    "An error occurred while placing the order :(\n" +
                            "We are sorry. Probably, no drivers are busy right now. Please, try again later."
            );
        }

        return modelAndView;
    }

    @GetMapping(value = "/history")
    ModelAndView orderHistoryGet(CancelOrderForm cancelOrderForm, HttpSession session) {
        LOGGER.info("TaxiServiceController: Order-History page GET");

        ModelAndView modelAndView = new ModelAndView("history");

        Integer userId = (Integer)session.getAttribute("userId");
        List<Ride> userRides = getAllUserRides(userId);

        modelAndView.addObject("rides", userRides);

        try {
            User user = userService.getUser(userId);
            modelAndView.addObject("user", user);
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        return modelAndView;
    }

    @PostMapping(value = "/history")
    ModelAndView orderHistoryPost(CancelOrderForm cancelOrderForm, BindingResult bindingResult, HttpSession session) {
        LOGGER.info("TaxiServiceController: Order-History page POST");

        ModelAndView modelAndView = new ModelAndView("history");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("responseMessage", "An error occurred during registration.");
            return modelAndView;
        }

        Integer idToCancel = cancelOrderForm.getId();

        try {
            taxiOrderingService.cancelOrder(idToCancel);

            modelAndView.addObject("responseMessage", "Status was successfully updated!");
        } catch (NumberFormatException | ServiceException e) {
            LOGGER.error(e);
            modelAndView.addObject("responseMessage",
                    "An error occurred while updating the status. Please, try again!");
        }

        Integer userId = (Integer)session.getAttribute("userId");
        List<Ride> userRides = getAllUserRides(userId);

        modelAndView.addObject("rides", userRides);

        try {
            User user = userService.getUser(userId);
            modelAndView.addObject("user", user);
        } catch (ServiceException e) {
            LOGGER.error(e);
        }

        return modelAndView;
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
