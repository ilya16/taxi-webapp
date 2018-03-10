package model.dao.impl;

import model.pojo.City;
import model.pojo.TaxiService;
import model.dao.api.TaxiServiceDAO;
import model.DAOException;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxiServiceController implements TaxiServiceDAO {
    static {
        PropertyConfigurator.configure(TaxiServiceController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiServiceController.class);

    /**
     * Finds TaxiService object by its identifier in database.
     *
     * @param id                TaxiService identifier
     * @return                  TaxiService object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public TaxiService getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding TaxiService with id=%d", id));

        TaxiService taxiService;
        String sql = "SELECT * FROM services s JOIN cities c ON s.city_id = c.id WHERE s.id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                City city = new City(
                        resultSet.getInt("city_id"),
                        resultSet.getString("name"),
                        resultSet.getString("region"),
                        resultSet.getBoolean("is_unsupported")
                );

                taxiService = new TaxiService(
                        resultSet.getInt("id"),
                        city,
                        resultSet.getString("service_type"),
                        resultSet.getInt("base_rate"),
                        resultSet.getBoolean("is_removed")
                );
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get TaxiService with id=%d", id));
        }

        LOGGER.debug("Found TaxiService:" + taxiService);

        return taxiService;
    }

    /**
     * Returns a list of all TaxiService entities from the database.
     *
     * @return                  list of all TaxiService entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<TaxiService> getAll() throws DAOException {
        LOGGER.debug("Getting all taxi services ");
        List<TaxiService> taxiServices = new ArrayList<>();
        Map<Integer, City> cities = new HashMap<>();
        String sql = "SELECT * FROM services s JOIN cities c ON s.city_id = c.id;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    City city;
                    if (!cities.containsKey(resultSet.getInt("city_id"))) {
                        city = new City(
                                resultSet.getInt("city_id"),
                                resultSet.getString("name"),
                                resultSet.getString("region"),
                                resultSet.getBoolean("is_unsupported")
                        );
                        cities.put(city.getId(), city);
                    } else {
                        city = cities.get(resultSet.getInt("city_id"));
                    }

                    TaxiService taxiService = new TaxiService(
                            resultSet.getInt("id"),
                            city,
                            resultSet.getString("service_type"),
                            resultSet.getInt("base_rate"),
                            resultSet.getBoolean("is_removed")
                    );

                    taxiServices.add(taxiService);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all taxi services from the database.");
        }

        LOGGER.debug("Got all taxi services");

        return taxiServices;
    }

    /**
     * Inserts new TaxiService into the database.
     *
     * @param taxiService       TaxiService entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(TaxiService taxiService) throws DAOException {
        LOGGER.debug("Inserting new taxi service");

        int lastId = 0;

        if (taxiService == null) { return lastId; }

        if (taxiService.getId() > 0) { return taxiService.getId(); }

        String sql = "INSERT INTO services (city_id, service_type, base_rate, is_removed) " +
                "VALUES (?, ?, ?, FALSE);";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, taxiService.getCityId());
            statement.setString(2, taxiService.getServiceType());
            statement.setInt(3, taxiService.getBaseRate());
            statement.executeQuery();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new taxiService into the database.");
        }

        LOGGER.info(String.format("Inserted new taxiService with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates TaxiService entity fields in the database.
     *
     * @param taxiService       TaxiService entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(TaxiService taxiService) throws DAOException {
        LOGGER.debug("Updating taxi service fields.");

        int count = 0;

        if (taxiService == null) { return count; }

        String sql = "UPDATE services SET city_id = ?, service_type = ?, " +
                "base_rate = ?, is_removed = ? WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taxiService.getCityId());
            statement.setString(2, taxiService.getServiceType());
            statement.setInt(3, taxiService.getBaseRate());
            statement.setBoolean(4, taxiService.isRemoved());
            statement.setInt(5, taxiService.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update driver taxi service in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d taxi services", count));

        return count;
    }

    /**
     * Saves the state of the TaxiService entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param taxiService       TaxiService entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public TaxiService save(TaxiService taxiService) throws DAOException {
        if (taxiService == null) {
            return null;
        }

        LOGGER.debug("Saving the taxi service information in the database.");

        if (taxiService.getId() > 0){
            update(taxiService);
        } else {
            int newId = insert(taxiService);
            taxiService.setId(newId);
        }

        LOGGER.debug(String.format("Information of a taxi service with id=%d was saved in the database.",
                taxiService.getId()));

        return taxiService;
    }

    @Override
    public int delete(TaxiService taxiService) {
        return 0;
    }
}
