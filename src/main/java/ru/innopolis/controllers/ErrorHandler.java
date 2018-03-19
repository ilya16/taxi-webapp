package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@Controller
public class ErrorHandler {
    static {
        PropertyConfigurator.configure(ErrorHandler.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(ErrorHandler.class);

    @GetMapping(value = "/error")
    public ModelAndView error() {
        LOGGER.info("ErrorHandler: Error page GET");

        ModelAndView model = new ModelAndView("error");
        model.addObject("errorMessage",
                "Something went wrong on the server side. Please, try again later.");

        return model;
    }

    @GetMapping(value = "/403")
    @ExceptionHandler(value = {AccessDeniedException.class, BadCredentialsException.class})
    public ModelAndView accessDenied() {
        LOGGER.info("ErrorHandler: 403 page GET");

        ModelAndView model = new ModelAndView("error");
        model.addObject("errorMessage",
                "You do not have permission to access this page. " +
                        "Probably, you have to authorize in the system.");

        return model;
    }
}
