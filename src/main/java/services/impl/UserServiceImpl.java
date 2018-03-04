package services.impl;

import model.dao.impl.UserController;
import model.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.api.UserService;

public class UserServiceImpl implements UserService {
    static {
        PropertyConfigurator.configure(UserServiceImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private static UserController userController = new UserController();

    @Override
    public User auth(String login, String password) {
        User user = userController.findUserByLoginAndPassword(login, password);

        LOGGER.debug("Got user: " + user);

        if ((user != null) && user.isBlocked()) {
            LOGGER.debug(String.format("User \"%s\" is blocked", login));
            return null;
        }

        return user;
    }

    @Override
    public User register(String login, String firstName, String lastName, String password) {
        User user = userController.findUserByLoginAndPassword(login, password);

        if (user != null) {
            LOGGER.debug(String.format("User with login \"%s\" already exists", login));
            return null;
        }

        user = userController.registerUser(login, firstName, lastName, password);
        System.out.println(user);

        LOGGER.debug(String.format("Registered new user: %s", user));

        return user;
    }
}
