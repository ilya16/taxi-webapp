package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Processes home page requests.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
@Controller
public class HomeController {
    static {
        PropertyConfigurator.configure(HomeController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

    /**
     * Processes GET requests for the hone page.
     *
     * @return                  ModelAndView object with index view
     */
    @GetMapping(value = {"/", "/home"})
    ModelAndView getHome() {
        LOGGER.info("HomeController: Home page GET");

        return new ModelAndView("index");
    }
}
