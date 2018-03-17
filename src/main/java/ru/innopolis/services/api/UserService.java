package ru.innopolis.services.api;

import ru.innopolis.model.pojo.User;
import ru.innopolis.services.ServiceException;

/**
 * Provides the interface for the User related services.
 * Includes registration, authorization and retrieval of user information.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
public interface UserService {

    /**
     * Manages authorization of the user in the system.
     *
     * @param login                 user login
     * @param password              user password
     * @return                      User object if authorization was successful
     * @throws ServiceException     if an error occurs during the authorization in the system
     */
    User auth(String login, String password) throws ServiceException;

    /**
     * Manages registration of the user in the system.
     *
     * @param login                 user login
     * @param firstName             user first name
     * @param lastName              user last name
     * @param password              user password
     * @return                      User object of newly registered user, if registration was successful
     * @throws ServiceException     if an error occurs during the registration in the system
     */
    User register(String login, String firstName, String lastName, String password) throws ServiceException;

    /**
     * Returns the User object by user identifier.
     *
     * @param userId                user identifier
     * @return                      User object
     * @throws ServiceException     if an error occurs during the user data retrieval
     */
    User getUser(Integer userId) throws ServiceException;
}
