package ru.innopolis.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import ru.innopolis.model.DAOException;
import ru.innopolis.model.dao.api.UserDAO;
import ru.innopolis.model.pojo.User;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.UserService;

/**
 * Implements the logic of User Service.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         UserService
 */
@Service
public class UserServiceImpl implements UserService {
    static {
        PropertyConfigurator.configure(UserServiceImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Manages authorization of the user in the system.
     *
     * @param login                 user login
     * @param password              user password
     * @return                      User object if authorization was successful
     * @throws ServiceException     if an error occurs during the authorization in the system.
     */
    @Override
    public User auth(String login, String password) throws ServiceException {
        LOGGER.info(String.format("Authorizing the user with login=%s", login));

        User user;
        try {
            user = userDAO.findUserByLoginAndPassword(login, password);
        } catch (DAOException e) {
            LOGGER.error(e);
            user = null;
        }

        LOGGER.debug("Got user: " + user);

        if ((user != null) && user.isBlocked()) {
            LOGGER.debug(String.format("User with login=%s is blocked", login));
            user = null;
        }

        if (user == null) {
            throw new ServiceException("Authorization error. Incorrect login/password pair.");
        }

        LOGGER.info(String.format("Authorization of the user with login=%s is done", login));

        return user;
    }

    /**
     * Manages registration of the user in the system.
     *
     * @param login                 user login
     * @param firstName             user first name
     * @param lastName              user last name
     * @param password              user password
     * @return                      User object of newly registered user, if registration was successful
     * @throws ServiceException     if an error occurs during the registration in the system.
     */
    @Override
    public User register(String login, String firstName,
                         String lastName, String password) throws ServiceException {
        LOGGER.info(String.format("Registering the user with login=%s", login));

        User user = null;
        try {
            user = userDAO.findUserByLogin(login);
        } catch (DAOException e) {
            LOGGER.error(e);
            LOGGER.debug(String.format("No users with login \"%s\" exist", login));
        }

        if (user != null) {
            LOGGER.debug(String.format("User with login \"%s\" already exists", login));
            throw new ServiceException(String.format("User with login \"%s\" already exists", login));
        } else {
            try {
                Integer userId = userDAO.insert(
                        new User(
                                0, login, firstName, lastName, password,
                                null, 0, null, false)
                        );
                user = userDAO.getEntityById(userId);
            } catch (DAOException e) {
                LOGGER.error(e);
                throw new ServiceException("Registration error. An error occurred while registering new user");
            }
        }

        LOGGER.info("Registered new user:" + user);

        return user;
    }

    /**
     * Returns the User object by user identifier.
     *
     * @param userId                user identifier
     * @return                      User object if user with such id exists
     * @throws ServiceException     if an error occurs during the user data retrieval.
     */
    @Override
    public User getUser(Integer userId) throws ServiceException {
        LOGGER.debug(String.format("Getting information of the user with id=%d", userId));

        User user;
        try {
            user = userDAO.getEntityById(userId);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException(String.format("Cannot get the information about the user with id=%d", userId));
        }

        user.setPassword(null);
        return user;
    }
}
