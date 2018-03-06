package model.dao.impl;

import model.beans.City;
import model.beans.TaxiService;
import model.dao.api.TaxiServiceDAO;
import model.utils.DataSourceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxiServiceController implements TaxiServiceDAO {
    static {
        PropertyConfigurator.configure(TaxiServiceController.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiServiceController.class);

    @Override
    public TaxiService getEntityById(Integer id) {
        TaxiService taxiService = null;
        String sql = "SELECT * FROM services WHERE id = ?;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            taxiService = new TaxiService(resultSet.getInt("id"), resultSet.getInt("city_id"),
                    resultSet.getString("service_type"), resultSet.getInt("base_rate"),
                    resultSet.getBoolean("is_removed"));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return taxiService;
    }

    @Override
    public List<TaxiService> getAll() {
        List<TaxiService> taxiServices = new ArrayList<>();
        Map<Integer, City> cities = new HashMap<>();
        String sql = "SELECT * FROM services s JOIN cities c ON s.city_id = c.id;";

        try {
            Connection connection = DataSourceFactory.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                City city;
                if (!cities.containsKey(resultSet.getInt("city_id"))) {
                    city = new City(resultSet.getInt("city_id"), resultSet.getString("name"),
                            resultSet.getString("region"), resultSet.getBoolean("is_unsupported"));
                    cities.put(city.getId(), city);
                } else {
                    city = cities.get(resultSet.getInt("city_id"));
                }

                TaxiService taxiService
                        = new TaxiService(resultSet.getInt("id"), city,
                        resultSet.getString("service_type"), resultSet.getInt("base_rate"),
                        resultSet.getBoolean("is_removed"));

                taxiServices.add(taxiService);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        return taxiServices;
    }

    @Override
    public Integer insert(TaxiService entity) {
        return null;
    }

    @Override
    public int update(TaxiService entity) {
        return 0;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public TaxiService save(TaxiService entity) {
        return null;
    }
}
