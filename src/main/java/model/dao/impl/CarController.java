package model.dao.impl;

import model.beans.Car;
import model.dao.api.CarDAO;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        String sql = "SELECT * FROM cars;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("serial_number"),
                        resultSet.getString("model"), resultSet.getString("color"),
                        resultSet.getInt("driver_id"), resultSet.getBoolean("has_child_seat"),
                        resultSet.getBoolean("is_blocked")));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
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
