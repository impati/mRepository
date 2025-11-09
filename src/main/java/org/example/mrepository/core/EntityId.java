package org.example.mrepository.core;

public interface EntityId<K, E> {

    K get(E entity);
}
