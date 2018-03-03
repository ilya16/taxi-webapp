package model.dao.impl;

import model.dao.interfaces.UserDAO;
import model.pojo.User;
import model.utils.DataSourceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserController implements UserDAO {
//    static {
//        PropertyConfigurator.configure(LoginServlet.class.getClassLoader().getResource("log4j.properties"));
//    }

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    // public static final String SELECT_USER_BY_CREDENTIALS = "SELECT * FROM users WHERE login = ? AND password = ?;";

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            user = new User(resultSet.getLong("id"), resultSet.getString("login"),
                    resultSet.getString("password"), resultSet.getBoolean("is_blocked"));

            LOGGER.info(String.format("Got user with login \"%s\"", login));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return user;
    }

    @Override
    public User registerUser(String login, String password) {
        String sql = "INSERT INTO users (login, password, is_blocked) VALUES (?, ?, FALSE);";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeQuery();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return findUserByLoginAndPassword(login, password);
    }

    @Override
    public User getEntityById(Integer id) {
        return null;
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
