package ru.innopolis.model.dao.impl;

import ru.innopolis.model.dao.api.DAO;
import ru.innopolis.model.dao.api.DriverDAO;
import ru.innopolis.model.pojo.Driver;
import ru.innopolis.model.DAOException;
import ru.innopolis.model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Driver DAO interface.
 * Communicates with the relation Driver in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         DAO
 * @see         DriverDAO
 */
public class DriverDAOImpl implements DriverDAO {
    static {
        PropertyConfigurator.configure(DriverDAOImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(DriverDAOImpl.class);

    /**
     * Finds Driver object by its identifier in database.
     *
     * @param id                Driver identifier
     * @return                  Driver object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Driver getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding Driver with id=%d", id));

        Driver driver;
        String sql = "SELECT * FROM drivers WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                driver = new Driver(
                        resultSet.getInt("driver_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age"),
                        resultSet.getBoolean("is_blocked")
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get Driver with id=%d", id));
        }

        LOGGER.debug("Found Driver:" + driver);

        return driver;
    }

    /**
     * Returns a list of all Driver entities from the database.
     *
     * @return                  list of all Driver entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<Driver> getAll() throws DAOException {
        LOGGER.debug("Getting all drivers");

        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    drivers.add(new Driver(
                            resultSet.getInt("driver_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getByte("age"),
                            resultSet.getBoolean("is_blocked")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all drivers from the database.");
        }

        LOGGER.debug("Got all drivers");

        return drivers;
    }

    /**
     * Inserts new Driver into the database.
     *
     * @param driver            Driver entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(Driver driver) throws DAOException {
        LOGGER.debug("Inserting new driver");

        int lastId = 0;

        if (driver == null) { return lastId; }

        if (driver.getId() > 0) { return driver.getId(); }

        String sql = "INSERT INTO drivers (first_name, last_name, age, is_blocked) " +
                "VALUES (?, ?, ?, FALSE);";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, driver.getFirstName());
            statement.setString(2, driver.getLastName());
            statement.setInt(3, driver.getAge());
            statement.executeQuery();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new driver into the database.");
        }

        LOGGER.info(String.format("Inserted new driver with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates Driver entity fields in the database.
     *
     * @param driver            Driver entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(Driver driver) throws DAOException {
        LOGGER.debug("Updating driver fields.");

        int count = 0;

        if (driver == null) { return count; }

        String sql = "UPDATE drivers SET first_name = ?, last_name = ?, " +
                "age = ?, is_blocked = ? WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, driver.getFirstName());
            statement.setString(2, driver.getLastName());
            statement.setInt(3, driver.getAge());
            statement.setBoolean(4, driver.isBlocked());
            statement.setInt(5, driver.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update driver fields in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d drivers", count));

        return count;
    }

    /**
     * Saves the state of the Driver entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param driver            Driver entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Driver save(Driver driver) throws DAOException {
        if (driver == null) {
            return null;
        }

        LOGGER.debug("Saving the driver information in the database.");

        if (driver.getId() > 0){
            update(driver);
        } else {
            int newId = insert(driver);
            driver.setId(newId);
        }

        LOGGER.debug(String.format("Information of a driver with id=%d was saved in the database.", driver.getId()));

        return driver;
    }

    @Override
    public int delete(Driver driver) {
        return 0;
    }
}
