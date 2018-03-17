package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.innopolis.forms.SignInForm;
import ru.innopolis.model.pojo.User;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class AuthorizationController {
    static {
        PropertyConfigurator.configure(AuthorizationController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(AuthorizationController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    ModelAndView loginGet(SignInForm signInForm) {
        LOGGER.info("AuthorizationController: Login page GET");

        return new ModelAndView("login");
    }

    @PostMapping(value = "/login")
    ModelAndView loginPost(@ModelAttribute SignInForm signInForm, BindingResult bindingResult, HttpSession session) {
        LOGGER.info("AuthorizationController: Login page POST");

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("responseMessage", "An error occurred during authorization.");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        String login = signInForm.getLogin();
        String password = signInForm.getPassword();

        try {
            User user = userService.auth(login, password);

            LOGGER.info(String.format("User with login=%s has successfully authorized in the system", login));

//            session.setAttribute("userLogin", login);
            session.setAttribute("userId", user.getId());

            modelAndView.setViewName("redirect:/order-taxi");
        } catch (ServiceException e) {
            LOGGER.error(e);

            modelAndView.addObject("responseMessage", "Incorrect login/password pair. Please, try again.");

            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    @GetMapping(value = "/logout")
    ModelAndView logoutGet() {
        LOGGER.info("AuthorizationController: Logout page GET");

        return new ModelAndView("index");
    }
}
