package ru.innopolis.services.api;

import ru.innopolis.model.pojo.City;
import ru.innopolis.model.pojo.Ride;
import ru.innopolis.model.pojo.TaxiService;
import ru.innopolis.services.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * Provides the interface for the Taxi Ordering related services.
 * Includes order placing and cancelling, methods for order history retrieval.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
public interface TaxiOrderingService {

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
    Ride placeOrder(Integer userId, Integer taxiServiceId, Integer cityId, boolean saveCity,
                    String locationFrom, String locationTo, String phoneNumber, boolean savePhoneNumber,
                    boolean childSeat, String orderComments) throws ServiceException;

    /**
     * Cancels the order with identifier rideId.
     *
     * @param rideId                identifier of the order to be cancelled
     * @throws ServiceException     if an error occurs while cancelling the order
     */
    void cancelOrder(Integer rideId) throws ServiceException;

    /**
     * Returns a list of all cities in the system.
     *
     * @param onlyActive            filtering only active (supported) cities
     * @return                      list of cities
     * @throws ServiceException     if an error occurs during the retrieval of all cities
     */
    List<City> getAllCities(boolean onlyActive) throws ServiceException;

    /**
     * Returns a dictionary of all cities and taxi services available in them.
     * Finds all taxi services for given cities.
     *
     * @param cities                list of target cities
     * @param onlyActive            filtering only active (supported) taxi services
     * @return                      map of city - taxi services pairs
     * @throws ServiceException     if an error occurs during the retrieval of all taxi services
     */
    Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive) throws ServiceException;

    /**
     * Returns a list of all user taxi rides.
     *
     * @param userId                identifier of the user
     * @return                      list of user rides
     * @throws ServiceException     if an error occurs during the retrieval of user rides
     */
    List<Ride> getAllUserRides(Integer userId) throws ServiceException;
}
