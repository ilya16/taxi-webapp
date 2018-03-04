package model.dao.api;

import java.util.List;

public interface DAO<E, K> {

    E getEntityById(K id);

    List<E> getAll();

    K insert(E entity);

    boolean update(E entity);

    boolean delete(K id);

    E save(E entity);
}
