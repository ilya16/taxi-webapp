package ru.innopolis.model.dao.api;

import ru.innopolis.model.DAOException;
import ru.innopolis.model.pojo.Ride;

import java.util.List;

/**
 * Provides the interface for Ride DAO that communicates
 * with the relation Ride in the database.
 *
 * @author      Ilya Borovik
 * @version     1.0
 * @see         DAO
 */
public interface RideDAO extends DAO<Ride, Integer> {

    /**
     * Returns a list of all user rides.
     *
     * @param userId            user identifier
     * @return                  list of rides
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    List<Ride> getAllUserRides(Integer userId) throws DAOException;
}
