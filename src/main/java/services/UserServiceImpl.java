package services;

import model.dao.impl.UserController;
import model.pojo.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService {
//    static {
//        PropertyConfigurator.configure(LoginServlet.class.getClassLoader().getResource("log4j.properties"));
//    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private static UserController userDAO = new UserController();

    @Override
    public User auth(String login, String password) {
        User user = userDAO.findUserByLoginAndPassword(login, password);

        System.out.println(user);
        LOGGER.debug("Got user: " + user);

        if ((user != null) && user.isBlocked()){
            return null;
        }

        LOGGER.debug("User is not blocked");

        return user;
    }

    @Override
    public User register(String login, String password) {
        User user = userDAO.findUserByLoginAndPassword(login, password);

        if (user != null) {
            LOGGER.debug("User with login \"%s\" already exists");
            return null;
        }

        user = userDAO.registerUser(login, password);
        System.out.println(user);

        LOGGER.debug("User was registered");

        return user;
    }
}
