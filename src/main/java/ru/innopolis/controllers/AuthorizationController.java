package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.innopolis.forms.SignInForm;
import ru.innopolis.model.pojo.User;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.UserService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Processes user authorization requests.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
@Controller
public class AuthorizationController {
    static {
        PropertyConfigurator.configure(AuthorizationController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(AuthorizationController.class);

    @Autowired
    private UserService userService;

    /**
     * Processes GET requests for the login page.
     *
     * @param signInForm        SignInForm object
     * @return                  ModelAndView object with login view
     */
    @GetMapping(value = "/login")
    ModelAndView loginGet(SignInForm signInForm) {
        LOGGER.info("AuthorizationController: Login page GET");

        return new ModelAndView("login");
    }

    /**
     * Processes POST requests for the login page.
     * Processes authorization of a user in the system.
     *
     * @param signInForm        SignInForm object with login credentials
     * @param bindingResult     request BindingResult object
     * @param session           HTTPSession object
     * @return                  ModelAndView object
     *                          with order-taxi view if authorization was unsuccessful
     *                          and login view otherwise
     */
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

            session.setAttribute("userLogin", login);
            session.setAttribute("userId", user.getId());

            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(() -> "ROLE_USER");

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getLogin(),
                    user.getPassword(),
                    grantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            modelAndView.setViewName("redirect:/order-taxi");
        } catch (ServiceException e) {
            LOGGER.error(e);

            modelAndView.addObject("responseMessage", "Incorrect login/password pair. Please, try again.");

            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

    /**
     * Processes GET requests for the logout page.
     * Redirects to the home page.
     *
     * @return                  ModelAndView object with index view
     */
    @GetMapping(value = "/logout")
    ModelAndView logoutGet() {
        LOGGER.info("AuthorizationController: Logout page GET");

        SecurityContextHolder.getContext().setAuthentication(null);

        return new ModelAndView("index");
    }
}
