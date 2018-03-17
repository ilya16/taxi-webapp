package ru.innopolis.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import ru.innopolis.model.DAOException;
import ru.innopolis.model.pojo.*;
import ru.innopolis.model.dao.api.*;
import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.innopolis.services.ServiceException;
import ru.innopolis.services.api.TaxiOrderingService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the logic of Taxi Service.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         TaxiService
 */
@Service
public class TaxiOrderingServiceImpl implements TaxiOrderingService {
    static {
        PropertyConfigurator.configure(TaxiOrderingServiceImpl.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(TaxiOrderingServiceImpl.class);

    @Autowired
    private TaxiServiceDAO taxiServiceDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private RideDAO rideDAO;

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private UserDAO userDAO;

    public TaxiOrderingServiceImpl() {
    }

    public TaxiOrderingServiceImpl(TaxiServiceDAO taxiServiceDAO, CityDAO cityDAO,
                                   RideDAO rideDAO, CarDAO carDAO, UserDAO userDAO) {
        this.taxiServiceDAO = taxiServiceDAO;
        this.cityDAO = cityDAO;
        this.rideDAO = rideDAO;
        this.carDAO = carDAO;
        this.userDAO = userDAO;
    }

    /**
     * Manages the taxi order placing.
     *
     * @param userId                identifier of the user that placed the order
     * @param taxiServiceId         identifier of the taxi service of the order
     * @param cityId                identifier of the city in which order is placed
     * @param saveCity              should city information be saved in user account, or not
     * @param locationFrom          source location of the ride
     * @param locationTo            destination location of the ride
     * @param phoneNumber           phone number of the user
     * @param savePhoneNumber       should phone number information be saved in user account, or not
     * @param childSeat             is child needed, or not
     * @param orderComments         order comments
     * @return                      Ride object of the placed order
     * @throws ServiceException     if an error occurs while saving order information in the system
     */
    @Override
    public Ride placeOrder(Integer userId, Integer taxiServiceId, Integer cityId, boolean saveCity,
                           String locationFrom, String locationTo, String phoneNumber, boolean savePhoneNumber,
                           boolean childSeat, String orderComments) throws ServiceException {
        LOGGER.info("Processing and placing taxi order");

        User user;
        try {
            user = userDAO.getEntityById(userId);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException(String.format("User with id=%d was not found in the system", userId));
        }

        if (saveCity) {
            user.setCityId(cityId);
        }
        if (savePhoneNumber) {
            user.setPhoneNumber(phoneNumber);
        }

        if (saveCity || savePhoneNumber) {
            LOGGER.debug("Updating user information");
            try {
                userDAO.update(user);
            } catch (DAOException e) {
                LOGGER.error(e);
                throw new ServiceException("Cannot update user information");
            }
        }

        // since interaction with driver is not implemented,
        // order (ride) can be only "finished"
        Ride ride = new Ride(0, userId, 0, taxiServiceId, null,
                locationFrom, locationTo, null, null, 0,
                0, orderComments, "finished");

        LOGGER.info("Calculating distance and price for the service");
        TaxiService taxiService;
        try {
            taxiService = taxiServiceDAO.getEntityById(taxiServiceId);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException(
                    String.format("Taxi Service with id=%d was not found in the system", taxiServiceId)
            );
        }
        int distance = calculateDistance(locationFrom, locationTo);
        ride.setPrice(calculatePriceByDistance(taxiService, distance));

        LOGGER.info("Finding most suitable car and driver for the order");
        Car car = selectCarForTheTaxiOrder(ride, childSeat);
        ride.setCar(car);
        ride.setCarId(car.getId());

        int rideId;
        try {
            rideId = rideDAO.insert(ride);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException("Cannot save order information in the system");
        }

        ride.setId(rideId);
        return ride;
    }

    /**
     * Cancels the order with identifier rideId.
     * @param rideId                identifier of the order to be cancelled
     * @throws ServiceException     if an error occurs while cancelling the order
     */
    @Override
    public void cancelOrder(Integer rideId) throws ServiceException {
        LOGGER.info(String.format("Cancelling the order with id=%d", rideId));

        Ride ride;
        try {
            ride = rideDAO.getEntityById(rideId);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException(String.format("Order with id=%d was not found in the system", rideId));
        }

        if ("ordered".equals(ride.getStatus())) {
            ride.setStatus("cancelled");

            try {
                rideDAO.update(ride);
            } catch (DAOException e) {
                LOGGER.error(e);
                throw new ServiceException(String.format("Cannot cancel the order with id=%d", rideId));
            }
        } else {
            throw new ServiceException(String.format("Cannot cancel the order with id=%d since the ride has " +
                    "already started or finished", rideId));
        }
    }

    /**
     * Returns a list of all cities in the system.
     *
     * @param onlyActive            filtering only active (supported) cities
     * @return                      list of cities
     * @throws ServiceException     if an error occurs during the retrieval of all cities
     */
    @Override
    public List<City> getAllCities(boolean onlyActive) throws ServiceException {
        LOGGER.info("Getting all cities");

        List<City> cities;
        try {
            cities = cityDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException("Cannot retrieve a list of all cities");
        }

        if (onlyActive) {
            cities = cities.stream().filter(x -> !x.isUnsupported()).collect(Collectors.toList());
        }

        return cities;
    }

    /**
     * Returns a dictionary of all cities and taxi services available in them.
     * Finds all taxi services for given cities.
     *
     * @param cities                list of target cities
     * @param onlyActive            filtering only active (supported) taxi services
     * @return                      map of city - taxi services pairs
     * @throws ServiceException     if an error occurs during the retrieval of all taxi services
     */
    @Override
    public Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive)
            throws ServiceException {

        LOGGER.info("Getting all taxi services in cities");

        List<TaxiService> taxiServices;
        try {
            taxiServices = taxiServiceDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException("Cannot retrieve a list of taxi services in given cities");
        }

        taxiServices = taxiServices.stream()
                .filter(x -> cities.contains(x.getCity()))
                .filter(x -> onlyActive && !x.isRemoved())
                .collect(Collectors.toList());

        LOGGER.debug("Building a map of city - taxi service pairs");
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
     * Returns a list of all user taxi rides.
     *
     * @param userId                identifier of the user
     * @return                      list of user rides
     * @throws ServiceException     if an error occurs during the retrieval of user rides
     */
    @Override
    public List<Ride> getAllUserRides(Integer userId) throws ServiceException {
        LOGGER.info(String.format("Getting all ride of a user with id=%d", userId));

        try {
            return rideDAO.getAllUserRides(userId);
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException(String.format("Cannot retrieve a list of all rides of a user with id=%d",
                    userId));
        }
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
    private Car selectCarForTheTaxiOrder(Ride ride, boolean childSeat) throws ServiceException {
        /* Basic implementation */

        List<Car> cars;
        try {
            cars = carDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error(e);
            throw new ServiceException("Cannot retrieve a list of all cars");
        }

        cars = cars.stream()
                .filter(x -> !x.isBlocked() && (!childSeat || x.isHasChildSeat()))
                .collect(Collectors.toList());

        if (cars.isEmpty()) {
            LOGGER.info("No cars available for the given order parameters");
            throw new ServiceException("No cars available for the given order parameters");
        }

        Random random = new Random();
        return cars.get(random.nextInt(cars.size()));
    }
}
