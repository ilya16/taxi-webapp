package services.impl;

import model.beans.City;
import model.beans.Ride;
import model.beans.TaxiService;
import model.dao.api.CityDAO;
import model.dao.api.TaxiServiceDAO;
import model.dao.impl.CityController;
import model.dao.impl.TaxiServiceController;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.api.TaxiOrderingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaxiOrderingServiceImpl implements TaxiOrderingService {
    static {
        PropertyConfigurator.configure(UserServiceImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServiceImpl.class);

    private static TaxiServiceDAO taxiServiceController = new TaxiServiceController();
    private static CityDAO cityController = new CityController();

    @Override
    public List<City> getAllCities(boolean onlyActive) {
        List<City> cities = cityController.getAll();

        if (onlyActive) {
            cities = cities.stream().filter(x -> !x.isUnsupported()).collect(Collectors.toList());
        }

        return cities;
    }

    @Override
    public Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive) {
        List<TaxiService> taxiServices = taxiServiceController.getAll();

        taxiServices = taxiServices.stream()
                .filter(x -> cities.contains(x.getCity()))
                .filter(x -> onlyActive && !x.isRemoved())
                .collect(Collectors.toList());

        Map<City, List<TaxiService>> cityTaxiServiceMap = new HashMap<>();

        for (City city: cities) {
            cityTaxiServiceMap.put(city, new ArrayList<>());
        }

        for (TaxiService taxiService: taxiServices) {
            cityTaxiServiceMap.get(taxiService.getCity()).add(taxiService);
        }

        return cityTaxiServiceMap;
    }

    /**
     * Business logic that computes ride price depending on the city, taxi service type,
     * distance and other factors
     * @param taxiService       ride taxi service
     * @param distance          distance of the ride
     * @return                  price of the ride
     */
    private int calculatePriceByDistance(TaxiService taxiService, int distance) {
        /* Basic implementation used */
        return taxiService.getBaseRate() + distance * 5;
    }

    private int calculatePrice(Ride ride) {
        return 0;
    }
}
