package services.api;

import model.beans.City;
import model.beans.Ride;
import model.beans.TaxiService;

import java.util.List;
import java.util.Map;

public interface TaxiOrderingService {
    Ride placeOrder(String userId, String cityId, String saveCity, String locationFrom,
                    String locationTo, String phoneNumber, String savePhoneNumber, String serviceId,
                    String childSeat, String orderComments);
    List<City> getAllCities(boolean onlyActive);
    Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive);
    Ride getLastRide(Integer userId);
}
