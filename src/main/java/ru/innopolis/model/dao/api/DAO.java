package ru.innopolis.model.dao.api;

import ru.innopolis.model.DAOException;

import java.util.List;

/**
 * Provides the interface for DAOs that communicate with database.
 *
 * @param <E>   type of the entity
 * @param <K>   type of the key
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
public interface DAO<E, K> {

    /**
     * Finds entity by its identifier in database.
     *
     * @param id                entity identifier
     * @return                  entity object
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    E getEntityById(K id) throws DAOException;

    /**
     * Returns a list of all entities from the database.
     *
     * @return                  list of all entities
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    List<E> getAll() throws DAOException;

    /**
     * Inserts new entity into the database.
     *
     * @param entity            entity to be inserted
     * @return                  identifier of the inserted entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    K insert(E entity) throws DAOException;

    /**
     * Updates entity fields in the database.
     *
     * @param entity            entity to be updated
     * @return                  number of updated rows in the database
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    int update(E entity) throws DAOException;

    /**
     * Deletes entity from the database.
     *
     * @param entity            entity to be deleted
     * @return                  number of deleted rows
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    int delete(E entity) throws DAOException;

    /**
     * Saves the state of the entity in the database.
     * Updates/Inserts entity in/into the database.
     *
     * @param entity            entity to be saved
     * @return                  saved entity
     * @throws DAOException     if any exception occurs while communicating with the database.
     */
    E save(E entity) throws DAOException;
}
