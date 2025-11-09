package org.example.impati.core;

import java.util.List;

public interface MRepository<K, E> {

    E save(E entity);

    void saveAll(List<E> entities);

    E findById(K id);

    List<E> findAll();

    void delete(E entity);

    void deleteAll(List<E> entities);
}
