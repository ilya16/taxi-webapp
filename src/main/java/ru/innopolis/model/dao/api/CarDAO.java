package ru.innopolis.model.dao.api;

import ru.innopolis.model.pojo.Car;

/**
 * Provides the interface for Car DAO that communicates
 * with the relation Car in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         DAO
 */
public interface CarDAO extends DAO<Car, Integer> {
}
