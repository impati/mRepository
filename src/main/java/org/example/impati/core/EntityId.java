package org.example.impati.core;

public interface EntityId<K, E> {

    K get(E entity);
}
