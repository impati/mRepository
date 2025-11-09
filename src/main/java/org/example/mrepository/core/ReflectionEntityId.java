package org.example.mrepository.core;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class ReflectionEntityId<K, E> implements EntityId<K, E> {

    private final Field idField;

    public ReflectionEntityId(Class<E> type) {
        this.idField = Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(MId.class))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("@MId 필드가 없습니다: " + type));
        this.idField.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    public K get(E e) {
        try {
            return (K) idField.get(e);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
