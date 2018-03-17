package ru.innopolis.model.dao.impl;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.innopolis.model.DAOException;
import ru.innopolis.model.dao.api.DAO;
import ru.innopolis.model.dao.api.UserDAO;
import ru.innopolis.model.pojo.User;
import ru.innopolis.model.utils.DataSourceFactory;
import ru.innopolis.model.utils.Encryptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the User DAO interface.
 * Communicates with the relation User in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         DAO
 * @see         UserDAO
 */
@Component
public class UserDAOImpl implements UserDAO {
    static {
        PropertyConfigurator.configure(UserDAOImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);

    /**
     * Finds a User object in the database by its login field.
     *
     * @param login             user login
     * @return                  found user entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public User findUserByLogin(String login) throws DAOException {
        LOGGER.debug(String.format("Finding user by login \"%s\"", login));

        User user;
        String sql = "SELECT * FROM users WHERE login = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("city_id"),
                        resultSet.getTimestamp("registration_date"),
                        resultSet.getBoolean("is_blocked")
                );
            }

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(String.format("Cannot get User with login=%s", login));
        }

        LOGGER.debug("Found User:" + user);

        return user;
    }

    /**
     * Finds a User object in the database by its login and password fields.
     *
     * @param login             user login
     * @param password          user password
     * @return                  found user entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public User findUserByLoginAndPassword(String login, String password) throws DAOException {
        LOGGER.debug(String.format("Finding user by login \"%s\" and password", login));

        User user = findUserByLogin(login);

        if (!Encryptor.checkPass(password, user.getPassword())) {
            LOGGER.debug("Password is not correct");
            throw new DAOException(String.format("Password of User with login=%s is not correct", login));
        }

        return user;
    }

    /**
     * Finds User object by its identifier in database.
     *
     * @param id                User identifier
     * @return                  User object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public User getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding User with id=%d", id));

        User user;
        String sql = "SELECT * FROM users WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("city_id"),
                        resultSet.getTimestamp("registration_date"),
                        resultSet.getBoolean("is_blocked")
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get User with id=%d", id));
        }

        LOGGER.debug("Found User:" + user);

        return user;
    }

    /**
     * Returns a list of all User entities from the database.
     *
     * @return                  list of all users
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<User> getAll() throws DAOException {
        LOGGER.debug("Getting all users from the database.");

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("city_id"),
                        resultSet.getTimestamp("registration_date"),
                        resultSet.getBoolean("is_blocked"))
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all users from the database.");
        }

        LOGGER.debug("Got all users");

        return users;
    }

    /**
     * Inserts new User into the database.
     *
     * @param user              User entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(User user) throws DAOException {
        LOGGER.debug("Inserting new user");

        int lastId = 0;

        if (user == null) { return lastId; }

        if (user.getId() > 0) { return user.getId(); }

        String sql = "INSERT INTO users (login, first_name, last_name, password, is_blocked) " +
                "VALUES (?, ?, ?, ?, FALSE);";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, Encryptor.hashPassword(user.getPassword()));
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new user into the database.");
        }

        LOGGER.info(String.format("Inserted new user with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates User entity fields in the database.
     *
     * @param user              User entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(User user) throws DAOException {
        LOGGER.debug("Updating user fields.");

        int count = 0;

        if (user == null) { return count; }

        String sql = "UPDATE users SET first_name = ?, last_name = ?, " +
                "phone_number = ?, city_id = ?, is_blocked = ? WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getPhoneNumber());
            statement.setInt(4, user.getCityId());
            statement.setBoolean(5, user.isBlocked());
            statement.setInt(6, user.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update user fields in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d users", count));

        return count;
    }

    /**
     * Deletes entity from the database.
     * Note:    to be safe, deleting is implemented
     *          as a change of the "is_blocked" field.
     *
     * @param user              entity to be deleted
     * @return                  number of deleted rows
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int delete(User user) throws DAOException {
        LOGGER.debug(String.format("Blocking the user with id=%d", user.getId()));

        int count;

        try {
            user.setBlocked(true);
            count = update(user);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot block the user.");
        }

        LOGGER.info(String.format("Blocked the user with id=%d", user.getId()));

        return count;
    }

    /**
     * Saves the state of the User entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param user              User entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public User save(User user) throws DAOException {
        if (user == null) {
            return null;
        }

        LOGGER.debug("Saving the user information in the database.");

        if (user.getId() > 0){
            update(user);
        } else {
            int newId = insert(user);
            user.setId(newId);
        }

        LOGGER.debug(String.format("Information of a user with id=%d was saved in the database.", user.getId()));

        return user;
    }
}
