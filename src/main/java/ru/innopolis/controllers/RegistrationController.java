package ru.innopolis.controllers;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.innopolis.forms.SignUpForm;
import ru.innopolis.model.pojo.User;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.UserService;

/**
 * Processes user registration requests.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
@Controller
public class RegistrationController {
    static {
        PropertyConfigurator.configure(RegistrationController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    /**
     * Processes GET requests for the sign-up page.
     *
     * @param signUpForm        SignUpForm object
     * @return                  ModelAndView object with sign-up view
     */
    @GetMapping(value = "/sign-up")
    ModelAndView signUpGet(SignUpForm signUpForm) {
        LOGGER.info("RegistrationController: Login page GET");

        return new ModelAndView("sign-up");
    }

    /**
     * Processes POST requests for the sign-up page.
     * Processes authorization of a user in the system.
     *
     * @param signUpForm        SignInForm object with login credentials
     * @param bindingResult     request BindingResult object
     * @return                  ModelAndView object
     *                          with login view if authorization was unsuccessful
     *                          and sign-up view otherwise
     */
    @PostMapping(value = "/sign-up")
    ModelAndView signUpPost(@ModelAttribute SignUpForm signUpForm, BindingResult bindingResult) {
        LOGGER.info("RegistrationController: Login page POST");

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("responseMessage", "An error occurred during registration.");
            modelAndView.setViewName("sign-up");
            return modelAndView;
        }

        String login = signUpForm.getLogin();
        String firstName = signUpForm.getFirstName();
        String lastName = signUpForm.getLastName();
        String password = signUpForm.getPassword();
        String passwordConfirm = signUpForm.getPasswordConfirm();

        boolean registrationSuccess = false;

        if (password.length() < 6) {
            modelAndView.addObject("responseMessage",
                    "Password should be at least 6 symbols long");
        }
        else if (!password.equals(passwordConfirm)) {
            modelAndView.addObject("responseMessage",
                    "Passwords do not match");
        }
        else {
            try {
                User user = userService.register(login, firstName, lastName, password);

                LOGGER.info(String.format("User with login=%s has successfully registered in the system",
                        user.getLogin()));

                modelAndView.addObject("responseMessage",
                        "Registration was successful! Sign into the System below:");

                registrationSuccess = true;
            } catch (ServiceException e) {
                LOGGER.error(e);

                modelAndView.addObject("responseMessage",
                        String.format("Login \"%s\" is already taken, enter another one", login));
            }
        }

        if (registrationSuccess) {
            modelAndView.setViewName("redirect:/login");
        } else {
            signUpForm.setPassword("");
            signUpForm.setPasswordConfirm("");
            modelAndView.addObject("signUpForm", signUpForm);

            modelAndView.setViewName("sign-up");
        }

        return modelAndView;
    }
}
