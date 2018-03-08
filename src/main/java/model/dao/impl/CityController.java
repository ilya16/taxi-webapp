package model.dao.impl;

import model.beans.City;
import model.dao.api.CityDAO;
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

public class CityController implements CityDAO {
    static {
        PropertyConfigurator.configure(CityController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(CityController.class);

    @Override
    public City getEntityById(Integer id) {
        return null;
    }

    @Override
    public List<City> getAll() {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities;";

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                cities.add(new City(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("region"), resultSet.getBoolean("is_unsupported")));
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return cities;
    }

    @Override
    public Integer insert(City entity) {
        return null;
    }

    @Override
    public int update(City entity) {
        return 0;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public City save(City entity) {
        return null;
    }
}
