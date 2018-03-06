package services.impl;

import model.beans.*;
import model.dao.api.*;
import model.dao.impl.*;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.api.TaxiOrderingService;

import java.util.*;
import java.util.stream.Collectors;

public class TaxiOrderingServiceImpl implements TaxiOrderingService {
    static {
        PropertyConfigurator.configure(UserServiceImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServiceImpl.class);

    private static TaxiServiceDAO taxiServiceController = new TaxiServiceController();
    private static CityDAO cityController = new CityController();
    private static RideDAO rideController = new RideController();
    private static CarDAO carController = new CarController();
    private static UserDAO userController = new UserController();

    @Override
    public Ride placeOrder(String userId, String cityId, String saveCity, String locationFrom,
                           String locationTo, String phoneNumber, String savePhoneNumber, String taxiServiceId,
                           String childSeat, String orderComments) {
        LOGGER.info("Processing and placing taxi order");

        User user = userController.getEntityById(Integer.parseInt(userId));
        if (saveCity != null) {
            user.setCityId(Integer.parseInt(cityId));
        }
        if (savePhoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        userController.update(user);

        Ride ride = new Ride(0, Integer.parseInt(userId), 0, Integer.parseInt(taxiServiceId), null,
                locationFrom, locationTo, null, null, 0,
                0, orderComments, "ordered");

        LOGGER.info("Calculating distance and price for the service");
        TaxiService taxiService = taxiServiceController.getEntityById(Integer.parseInt(taxiServiceId));
        int distance = calculateDistance(locationFrom, locationTo);
        ride.setPrice(calculatePriceByDistance(taxiService, distance));

        LOGGER.info("Finding a car and driver for the order");
        Car car = selectCarForTheTaxiOrder(ride, childSeat != null);
        ride.setCar(car);
        ride.setCarId(car.getId());

        int rideId = rideController.insert(ride);
        ride.setId(rideId);

        if (rideId > 0) return ride;
        return null;
    }

    @Override
    public List<City> getAllCities(boolean onlyActive) {
        LOGGER.info("Getting all cities");

        List<City> cities = cityController.getAll();

        if (onlyActive) {
            cities = cities.stream().filter(x -> !x.isUnsupported()).collect(Collectors.toList());
        }

        return cities;
    }

    @Override
    public Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive) {
        LOGGER.info("Getting all taxi services in cities");

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

    @Override
    public Ride getLastRide(Integer userId) {
        LOGGER.info(String.format("Getting last ride of a user with id=%d", userId));

        List<Ride> userRides = rideController.getAllUserRides(userId);
        if (userRides.size() == 0) return null;
        return userRides.get(userRides.size() - 1);
    }

    /**
     * Business logic that computes ride price depending on the city, taxi service type,
     * distance and other factors
     * @param taxiService       ride taxi service
     * @param distance          distance of the ride
     * @return                  price of the ride
     */
    private int calculatePriceByDistance(TaxiService taxiService, int distance) {
        /* Basic implementation */
        return taxiService.getBaseRate() + distance * 5;
    }

    /**
     * Calculates the distance between two locations
     * @param locationFrom      source
     * @param locationTo        destination
     * @return                  distance bettwen two locations
     */
    private int calculateDistance(String locationFrom, String locationTo) {
        /* Basic implementation */
        Random random = new Random();
        return random.nextInt(10);
    }

    /**
     * Business logic of car selection based on driver availability, location, service type
     * driver rating and other parameters.
     * @return                  the most suitable car for the order
     */
    private Car selectCarForTheTaxiOrder(Ride ride, boolean childSeat) {
        /* Basic implementation */
        List<Car> cars = carController.getAll()
                .stream()
                .filter(x -> !x.isBlocked() && x.isHasChildSeat() == childSeat)
                .collect(Collectors.toList());

        Random random = new Random();
        return cars.get(random.nextInt(cars.size()));
    }
}
