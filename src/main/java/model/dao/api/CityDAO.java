package model.dao.api;

import model.pojo.City;

/**
 * Provides the interface for City DAO that communicates
 * with the relation City in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         model.dao.api.DAO
 */
public interface CityDAO extends DAO<City, Integer> {
}
