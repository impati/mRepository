package org.example.mrepository.core;

public interface EntityId<K, E> {

    K get(E entity);

    E set(E entity, K id); // 불변 객체면 id가 채워진 새 인스턴스를 반환
}
