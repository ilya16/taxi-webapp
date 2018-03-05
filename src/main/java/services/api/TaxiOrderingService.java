package services.api;

import model.beans.City;
import model.beans.TaxiService;

import java.util.List;
import java.util.Map;

public interface TaxiOrderingService {
    List<City> getAllCities(boolean onlyActive);
    Map<City, List<TaxiService>> getAllCityTaxiServices(List<City> cities, boolean onlyActive);
}
