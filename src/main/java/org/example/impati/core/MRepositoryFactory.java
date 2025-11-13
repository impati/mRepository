package org.example.impati.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.example.impati.core.method_invoker.MRepositoryInvokerFactory;
import org.example.impati.core.method_invoker.MRepositoryMethodInvoker;

public class MRepositoryFactory {

    private MRepositoryFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <K, E, T extends MRepository<K, E>> T create(Class<T> repoInterface, List<MRepositoryMethodInvoker<E>> methodInvokers) {
        Class<E> entityType = (Class<E>) resolveEntityType(repoInterface);
        MStore<Object, E> store = new MStore<>(entityType);
        List<MRepositoryMethodInvoker<E>> finalMethodInvokers = new ArrayList<>(methodInvokers);
        finalMethodInvokers.addAll(MRepositoryInvokerFactory.defaultCreate(entityType));

        return new MRepositoryProxy<>(repoInterface, store, finalMethodInvokers).create();
    }

    @SuppressWarnings("unchecked")
    public static <K, E, T extends MRepository<K, E>> T create(Class<T> repoInterface) {
        Class<E> entityType = (Class<E>) resolveEntityType(repoInterface);

        return create(repoInterface, MRepositoryInvokerFactory.defaultCreate(entityType));
    }

    private static Class<?> resolveEntityType(Class<?> repoInterface) {
        // 1) 자신이 곧바로 ParameterizedType MRepository<K,E>를 구현
        Class<?> e = resolveEntityTypeFromInterfaces(repoInterface.getGenericInterfaces());
        if (e != null) {
            return e;
        }

        throw new IllegalArgumentException("엔티티 제네릭 타입(E)을 해석할 수 없습니다: " + repoInterface);
    }

    private static Class<?> resolveEntityTypeFromInterfaces(Type[] genericIfaces) {
        for (Type t : genericIfaces) {
            if (t instanceof ParameterizedType pt) {
                Type raw = pt.getRawType();
                if (raw instanceof Class<?> rawClz && MRepository.class.isAssignableFrom(rawClz)) {
                    // MRepository<K,E> → 1번 인덱스가 E
                    Type eType = pt.getActualTypeArguments()[1];
                    if (eType instanceof Class<?> ec) {
                        return ec;
                    }
                    // E가 와일드카드/타입변수인 경우엔 추가 처리 필요
                }
                // 인터페이스 체인에 더 깊이 들어가야 할 수도 있음
                Class<?> nested = resolveEntityTypeFromInterfacesRec(pt);
                if (nested != null) {
                    return nested;
                }
            }
        }
        return null;
    }

    // ParameterizedType 내부에 또 인터페이스가 중첩돼 있을 가능성 처리
    private static Class<?> resolveEntityTypeFromInterfacesRec(ParameterizedType pt) {
        Type raw = pt.getRawType();
        if (raw instanceof Class<?> rc) {
            return resolveEntityType(rc);
        }
        return null;
    }
}
