package ru.innopolis.model.dao.api;

import ru.innopolis.model.DAOException;
import ru.innopolis.model.dao.api.DAO;
import ru.innopolis.model.pojo.User;

/**
 * Provides the interface for User DAO that communicates
 * with the relation User in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         DAO
 */
public interface UserDAO extends DAO<User, Integer> {

    /**
     * Finds a User object in the database by its login field.
     *
     * @param login             user login
     * @return                  found user entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    User findUserByLogin(String login) throws DAOException;

    /**
     * Finds a User object in the database by its login and password fields.
     *
     * @param login             user login
     * @param password          user password
     * @return                  found user entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    User findUserByLoginAndPassword(String login, String password) throws DAOException;
}
