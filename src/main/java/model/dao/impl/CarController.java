package model.dao.impl;

import model.pojo.Car;
import model.pojo.Driver;
import model.dao.api.CarDAO;
import model.DAOException;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarController implements CarDAO {
    static {
        PropertyConfigurator.configure(CarController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(CarController.class);

    /**
     * Finds Car object by its identifier in database.
     *
     * @param id                Car identifier
     * @return                  Car object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Car getEntityById(Integer id) throws DAOException {
        LOGGER.debug(String.format("Finding Car with id=%d", id));

        Car car;
        String sql = "SELECT *, c.is_blocked AS car_is_blocked, d.is_blocked AS driver_is_blocked " +
                "FROM cars c JOIN drivers d ON c.driver_id = d.id " +
                "WHERE c.id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                car = buildCarEntity(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException(String.format("Cannot get Car with id=%d", id));
        }

        LOGGER.debug("Found Car:" + car);

        return car;
    }

    /**
     * Returns a list of all Car entities from the database.
     *
     * @return                  list of all Car entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public List<Car> getAll() throws DAOException {
        LOGGER.debug("Getting all cars");

        List<Car> cars = new ArrayList<>();
        String sql = "SELECT *, c.is_blocked AS car_is_blocked, d.is_blocked AS driver_is_blocked " +
                "FROM cars c JOIN drivers d ON c.driver_id = d.id;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cars.add(buildCarEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot get all cars from the database.");
        }

        LOGGER.debug("Got all cars");

        return cars;
    }

    /**
     * Inserts new Car into the database.
     *
     * @param car               Car entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Integer insert(Car car) throws DAOException {
        LOGGER.debug("Inserting new car");

        int lastId = 0;

        if (car == null) { return lastId; }

        if (car.getId() > 0) { return car.getId(); }

        String sql = "INSERT INTO cars (serial_number, model, color, driver_id, " +
                "has_child_seat, is_blocked) " +
                "VALUES (?, ?, ?, ?, ?, FALSE);";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, car.getSerialNumber());
            statement.setString(2, car.getModel());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getDriverId());
            statement.setBoolean(5, car.isHasChildSeat());
            statement.executeQuery();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot insert new car into the database.");
        }

        LOGGER.info(String.format("Inserted new car with id=%d", lastId));

        return lastId;
    }

    /**
     * Updates Car entity fields in the database.
     *
     * @param car               Car entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public int update(Car car) throws DAOException {
        LOGGER.debug("Updating car fields.");

        int count = 0;

        if (car == null) { return count; }

        String sql = "UPDATE cars SET serial_number = ?, model = ?, color = ?, " +
                "driver_id = ?, has_child_seat = ?, is_blocked = ? WHERE id = ?;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, car.getSerialNumber());
            statement.setString(2, car.getModel());
            statement.setString(3, car.getColor());
            statement.setInt(4, car.getDriverId());
            statement.setBoolean(5, car.isHasChildSeat());
            statement.setBoolean(6, car.isBlocked());
            statement.setInt(7, car.getId());

            count = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DAOException("Cannot update car fields in the database.");
        }

        LOGGER.info(String.format("Updated fields of %d cars", count));

        return count;
    }

    /**
     * Saves the state of the Car entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param car               Car entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    @Override
    public Car save(Car car) throws DAOException {
        if (car == null) {
            return null;
        }

        LOGGER.debug("Saving the car information in the database.");

        if (car.getId() > 0){
            update(car);
        } else {
            int newId = insert(car);
            car.setId(newId);
        }

        LOGGER.debug(String.format("Information of a car with id=%d was saved in the database.", car.getId()));

        return car;
    }

    @Override
    public int delete(Car car) {
        return 0;
    }

    /**
     * Builds a Car object from row in resultSet.
     *
     * @param resultSet         ResultsSet object
     * @return                  Car object
     * @throws SQLException     if a column name is invalid
     */
    private Car buildCarEntity(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver(
                resultSet.getInt("driver_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getByte("age"),
                resultSet.getBoolean("driver_is_blocked")
        );

        return new Car(
                resultSet.getInt(1),
                resultSet.getString("serial_number"),
                resultSet.getString("model"),
                resultSet.getString("color"),
                driver,
                resultSet.getBoolean("has_child_seat"),
                resultSet.getBoolean("car_is_blocked")
        );
    }
}
