package model.dao.api;

import model.pojo.User;
import model.utils.DAOException;

/**
 * Provides the interface for User DAO that communicates
 * with the relation User in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         model.dao.api.DAO
 */
public interface UserDAO extends DAO<User, Integer> {

    /**
     * Finds a User object in the database by its login and password fields.
     *
     * @param login         user login
     * @param password      user password
     * @return              found user entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    User findUserByLoginAndPassword(String login, String password) throws DAOException;
}
