package model.dao.impl;

import model.beans.*;
import model.beans.Driver;
import model.dao.api.RideDAO;
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
    public List<Ride> getAllUserRides(Integer userId) {
        LOGGER.info("Getting all user rides");

        List<Ride> rides = new ArrayList<>();
//         String sql = "SELECT * FROM rides r WHERE user_id = ? ORDER BY r.id ASC;";
        String sql = "SELECT *, c.is_blocked AS car_is_blocked, d.is_blocked AS driver_is_blocked FROM rides r\n" +
                " JOIN cars c ON r.car_id = c.id\n" +
                " JOIN drivers d ON c.driver_id = d.id\n" +
                " JOIN services s ON r.service_id = s.id\n" +
                " JOIN cities ct ON s.city_id = ct.id\n" +
                " WHERE user_id = ? ORDER BY r.id ASC;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
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

                Ride ride = new Ride(
                        resultSet.getInt("id"),
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

                rides.add(ride);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return rides;
    }

    @Override
    public Ride getEntityById(Integer id) {
        return null;
    }

    @Override
    public List<Ride> getAll() {
        return null;
    }

    @Override
    public Integer insert(Ride ride) {
        int lastId = 0;

        if (ride == null) {
            return lastId;
        }

        if (ride.getId() > 0){
            return ride.getId();
        }

        String sql = "INSERT INTO rides (user_id, car_id, service_id, location_from, location_to, " +
                "price, order_comments) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, ride.getUserId());
            preparedStatement.setInt(2, ride.getCarId());
            preparedStatement.setInt(3, ride.getTaxiServiceId());
            preparedStatement.setString(4, ride.getLocationFrom());
            preparedStatement.setString(5, ride.getLocationTo());
            preparedStatement.setInt(6, ride.getPrice());
            preparedStatement.setString(7, ride.getOrderComments());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                lastId = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    @Override
    public int update(Ride entity) {
        return 0;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Ride save(Ride entity) {
        return null;
    }
}
