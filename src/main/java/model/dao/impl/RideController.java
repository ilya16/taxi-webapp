package model.dao.impl;

import model.pojo.*;
import model.pojo.Driver;
import model.dao.api.RideDAO;
import model.DAOException;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RideController implements RideDAO {
    static {
        PropertyConfigurator.configure(RideController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(RideController.class);

    @Override
    public List<Ride> getAllUserRides(Integer userId) throws DAOException {
        LOGGER.debug(String.format("Getting all user rides of user with id=%d", userId));

        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT *, c.is_blocked AS car_is_blocked, d.is_blocked AS driver_is_blocked FROM rides r\n" +
                " JOIN cars c ON r.car_id = c.id\n" +
                " JOIN drivers d ON c.driver_id = d.id\n" +
                " JOIN services s ON r.service_id = s.id\n" +
                " JOIN cities ct ON s.city_id = ct.id\n" +
                " WHERE user_id = ? ORDER BY r.id ASC;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rides.add(buildRideEntity(resultSet, true));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all user rides from the database.");
        }

        LOGGER.debug("Got all user rides");

        return rides;
    }

    /**
     * Finds Ride object by its identifier in database.
     *
     * @param id                Ride identifier
     * @return                  Ride object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Ride getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding Driver with id=%d", id));

        Ride ride;
        String sql = "SELECT * FROM rides WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                ride = buildRideEntity(resultSet, false);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get Ride with id=%d", id));
        }

        LOGGER.debug("Found Ride:" + ride);

        return ride;
    }

    /**
     * Returns a list of all Ride entities from the database.
     *
     * @return                  list of all Ride entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<Ride> getAll() throws DAOException {
        LOGGER.debug("Getting all rides");

        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT * FROM rides;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rides.add(buildRideEntity(resultSet, false));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all rides from the database.");
        }

        LOGGER.debug("Got all rides");

        return rides;
    }

    /**
     * Inserts new Ride into the database.
     *
     * @param ride              Ride entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(Ride ride) throws DAOException {
        LOGGER.debug("Inserting new ride");

        int lastId = 0;

        if (ride == null) {
            return lastId;
        }

        if (ride.getId() > 0){
            return ride.getId();
        }

        String sql = "INSERT INTO rides (user_id, car_id, service_id, location_from, location_to, " +
                "price, order_comments, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?::ride_status)";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, ride.getUserId());
            statement.setInt(2, ride.getCarId());
            statement.setInt(3, ride.getTaxiServiceId());
            statement.setString(4, ride.getLocationFrom());
            statement.setString(5, ride.getLocationTo());
            statement.setInt(6, ride.getPrice());
            statement.setString(7, ride.getOrderComments());
            statement.setString(8, ride.getStatus());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new ride into the database.");
        }

        LOGGER.info(String.format("Inserted new ride with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates Ride entity fields in the database.
     *
     * @param ride              Ride entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(Ride ride) throws DAOException {
        LOGGER.debug("Updating ride fields.");

        int count = 0;

        if (ride == null) { return count; }

        String sql = "UPDATE rides SET user_id = ?, car_id = ?, service_id = ?, " +
                "order_time = ?, location_from = ?, location_to = ?, " +
                "time_start = ?, time_end = ?, price = ?, rating = ?," +
                "order_comments = ?, status = ?::ride_status WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ride.getUserId());
            statement.setInt(2, ride.getCarId());
            statement.setInt(3, ride.getTaxiServiceId());
            statement.setTimestamp(4, ride.getOrderTime());
            statement.setString(5, ride.getLocationFrom());
            statement.setString(6, ride.getLocationTo());
            statement.setTimestamp(7, ride.getTimeStart());
            statement.setTimestamp(8, ride.getTimeEnd());
            statement.setInt(9, ride.getPrice());
            statement.setInt(10, ride.getRating());
            statement.setString(11, ride.getOrderComments());
            statement.setString(12, ride.getStatus());
            statement.setInt(13, ride.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update ride fields in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d rides", count));

        return count;
    }

    /**
     * Saves the state of the Ride entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param ride              Ride entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Ride save(Ride ride) throws DAOException {
        if (ride == null) {
            return null;
        }

        LOGGER.debug("Saving the ride information in the database.");

        if (ride.getId() > 0){
            update(ride);
        } else {
            int newId = insert(ride);
            ride.setId(newId);
        }

        LOGGER.debug(String.format("Information of a ride with id=%d was saved in the database.", ride.getId()));

        return ride;
    }

    @Override
    public int delete(Ride ride) {
        return 0;
    }

    /**
     * Builds a Ride object from row in resultSet.
     *
     * @param resultSet         ResultsSet object
     * @param detailed          were joins made or not
     * @return                  Ride object
     * @throws SQLException     if a column name is invalid
     */
    private Ride buildRideEntity(ResultSet resultSet, boolean detailed) throws SQLException {
        if (detailed) {
            Driver driver = new Driver(
                    resultSet.getInt("driver_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getByte("age"),
                    resultSet.getBoolean("driver_is_blocked")
            );

            Car car = new Car(
                    resultSet.getInt("car_id"),
                    resultSet.getString("serial_number"),
                    resultSet.getString("model"),
                    resultSet.getString("color"),
                    driver,
                    resultSet.getBoolean("has_child_seat"),
                    resultSet.getBoolean("car_is_blocked")
            );

            City city = new City(
                    resultSet.getInt("city_id"),
                    resultSet.getString("name"),
                    resultSet.getString("region"),
                    resultSet.getBoolean("is_unsupported")
            );

            TaxiService taxiService = new TaxiService(
                    resultSet.getInt("service_id"),
                    city,
                    resultSet.getString("service_type"),
                    resultSet.getInt("base_rate"),
                    resultSet.getBoolean("is_removed")
            );

            return new Ride(
                    resultSet.getInt(1),
                    resultSet.getInt("user_id"),
                    car,
                    taxiService,
                    resultSet.getTimestamp("order_time"),
                    resultSet.getString("location_from"),
                    resultSet.getString("location_to"),
                    resultSet.getTimestamp("time_start"),
                    resultSet.getTimestamp("time_end"),
                    resultSet.getInt("price"),
                    resultSet.getInt("rating"),
                    resultSet.getString("order_comments"),
                    resultSet.getString("status")
            );
        } else {
            return new Ride(
                    resultSet.getInt(1),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("car_id"),
                    resultSet.getInt("service_id"),
                    resultSet.getTimestamp("order_time"),
                    resultSet.getString("location_from"),
                    resultSet.getString("location_to"),
                    resultSet.getTimestamp("time_start"),
                    resultSet.getTimestamp("time_end"),
                    resultSet.getInt("price"),
                    resultSet.getInt("rating"),
                    resultSet.getString("order_comments"),
                    resultSet.getString("status")
            );
        }
    }
}
