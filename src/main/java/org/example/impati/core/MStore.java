package org.example.impati.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MStore<K, E> {

    private final Map<K, E> store;
    private final ReflectionEntityId<K, E> entityId;
    private final Class<E> entityType;

    public MStore(Class<E> entityType) {
        this.entityId = new ReflectionEntityId<>(entityType);
        this.entityType = entityType;
        this.store = new HashMap<>();
    }

    public E save(E entity) {
        store.put(entityId.get(entity), entity);
        return entity;
    }

    public void saveAll(List<E> entities) {
        store.putAll(entities.stream().collect(Collectors.toMap(entityId::get, Function.identity())));
    }

    public E findById(K id) {
        return store.get(id);
    }

    public List<E> findAll() {
        return store.values().stream().toList();
    }

    public E delete(E entity) {
        return store.remove(entityId.get(entity));
    }

    public void deleteAll(List<E> entities) {
        for (E entity : entities) {
            delete(entity);
        }
    }

    public Collection<E> values() {
        return store.values();
    }

    public Class<E> entityType() {
        return entityType;
    }
}
