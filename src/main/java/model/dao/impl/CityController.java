package model.dao.impl;

import model.pojo.City;
import model.dao.api.CityDAO;
import model.DAOException;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the City DAO interface.
 * Communicates with the relation City in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         model.dao.api.DAO
 * @see         model.dao.api.CityDAO
 */
public class CityController implements CityDAO {
    static {
        PropertyConfigurator.configure(CityController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(CityController.class);

    /**
     * Finds City object by its identifier in database.
     *
     * @param id                City identifier
     * @return                  City object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public City getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding City with id=%d", id));

        City city;
        String sql = "SELECT * FROM cities WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            city = new City(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("region"),
                    resultSet.getBoolean("is_unsupported")
            );

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get City with id=%d", id));
        }

        LOGGER.debug("Found City:" + city);

        return city;
    }

    /**
     * Returns a list of all City entities from the database.
     *
     * @return                  list of all City entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<City> getAll() throws DAOException {
        LOGGER.debug("Getting all cities");

        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                cities.add(new City(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("region"),
                        resultSet.getBoolean("is_unsupported")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all cities from the database.");
        }

        LOGGER.debug("Got all cities");

        return cities;
    }

    /**
     * Inserts new City into the database.
     *
     * @param city              City entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(City city) throws DAOException {
        LOGGER.debug("Inserting new city");

        int lastId = 0;

        if (city == null) { return lastId; }

        if (city.getId() > 0) { return city.getId(); }

        String sql = "INSERT INTO cities (name, region, is_unsupported) " +
                "VALUES (?, ?, FALSE);";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, city.getName());
            statement.setString(2, city.getRegion());
            statement.executeQuery();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                lastId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new city into the database.");
        }

        LOGGER.info(String.format("Inserted new city with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates City entity fields in the database.
     *
     * @param city              City entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(City city) throws DAOException {
        LOGGER.debug("Updating city fields.");

        int count = 0;

        if (city == null) { return count; }

        String sql = "UPDATE cities SET name = ?, region = ?, " +
                "is_unsupported = ? WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, city.getName());
            statement.setString(2, city.getRegion());
            statement.setBoolean(3, city.isUnsupported());
            statement.setInt(4, city.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update city fields in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d cities", count));

        return count;
    }

    /**
     * Saves the state of the City entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param city              City entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public City save(City city) throws DAOException {
        if (city == null) {
            return null;
        }

        LOGGER.debug("Saving the city information in the database.");

        if (city.getId() > 0){
            update(city);
        } else {
            int newId = insert(city);
            city.setId(newId);
        }

        LOGGER.debug(String.format("Information of a city with id=%d was saved in the database.", city.getId()));

        return city;
    }

    @Override
    public int delete(City city) {
        return 0;
    }
}
