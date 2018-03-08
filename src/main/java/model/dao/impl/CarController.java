package model.dao.impl;

import model.beans.Car;
import model.beans.Driver;
import model.dao.api.CarDAO;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarController implements CarDAO {
    static {
        PropertyConfigurator.configure(CarController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(CarController.class);

    @Override
    public Car getEntityById(Integer id) {
        return null;
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT *, c.is_blocked AS car_is_blocked, d.is_blocked AS driver_is_blocked " +
                "FROM cars c JOIN drivers d ON c.driver_id = d.id;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
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
                        resultSet.getInt(1),
                        resultSet.getString("serial_number"),
                        resultSet.getString("model"),
                        resultSet.getString("color"),
                        driver,
                        resultSet.getBoolean("has_child_seat"),
                        resultSet.getBoolean("car_is_blocked")
                );

                cars.add(car);
            }
        } catch (SQLException e) {
            // TODO throw exceptions
            LOGGER.error(e);
        }

        return cars;
    }

    @Override
    public Integer insert(Car entity) {
        return null;
    }

    @Override
    public int update(Car entity) {
        return 0;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Car save(Car entity) {
        return null;
    }
}
