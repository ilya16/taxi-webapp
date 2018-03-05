package model.dao.impl;

import model.dao.api.UserDAO;
import model.beans.User;
import model.utils.DataSourceFactory;
import model.utils.Encryptor;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserController implements UserDAO {
    static {
        PropertyConfigurator.configure(UserController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE login = ?;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            /* Passwords match check */
            if (Encryptor.checkPass(password, resultSet.getString("password"))) {
                user = new User(resultSet.getLong("id"), resultSet.getString("login"),
                        resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("password"), resultSet.getString("phone_number"),
                        resultSet.getTimestamp("registration_date"), resultSet.getBoolean("is_blocked"));

            } else {
                LOGGER.debug("Password is not correct");
            }

            LOGGER.debug(String.format("Got user with login \"%s\"", login));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return user;
    }

    @Override
    public User registerUser(String login, String firstName, String lastName, String password) {
        String sql = "INSERT INTO users (login, first_name, last_name, password, is_blocked) " +
                "VALUES (?, ?, ?, ?, FALSE);";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, Encryptor.hashPassword(password));
            statement.executeQuery();

            LOGGER.info(String.format("Inserted user with login \"%s\"", login));

            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return findUserByLoginAndPassword(login, password);
    }

    @Override
    public User getEntityById(Integer id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            user = new User(resultSet.getLong("id"), resultSet.getString("login"),
                    resultSet.getString("first_name"), resultSet.getString("last_name"),
                    resultSet.getString("password"), resultSet.getString("phone_number"),
                    resultSet.getTimestamp("registration_date"), resultSet.getBoolean("is_blocked"));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public Integer insert(User entity) {
        return null;
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public User save(User entity) {
        return null;
    }
}
