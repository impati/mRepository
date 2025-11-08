package org.example.mrepository.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import org.example.mrepository.core.method_invoker.MRepositoryInvokerFactory;
import org.example.mrepository.core.method_invoker.MRepositoryMethodInvoker;

public class MRepositoryFactory {

    /**
     * 권장: 레포지토리 인터페이스(예: DemoRepository.class)를 넘기면
     * MRepository<K,E>의 E를 추론해 프록시를 바로 돌려준다.
     */
    @SuppressWarnings("unchecked")
    public static <K, E, T extends MRepository<K, E>> T create(Class<T> repoInterface) {
        Class<E> entityType = (Class<E>) resolveEntityType(repoInterface);
        List<MRepositoryMethodInvoker> methodInvokers = MRepositoryInvokerFactory.create(entityType);

        return new MRepositoryProxy(repoInterface, methodInvokers).create();
    }

    /**
     * repoInterface가 MRepository<K,E>를 직접/간접으로 확장한다고 가정하고 E의 실 타입을 찾는다.
     */
    private static Class<?> resolveEntityType(Class<?> repoInterface) {
        // 1) 자신이 곧바로 ParameterizedType MRepository<K,E>를 구현?
        Class<?> e = resolveEntityTypeFromInterfaces(repoInterface.getGenericInterfaces());
        if (e != null) {
            return e;
        }

        // 2) 상속 계층(부모 인터페이스)에도 있을 수 있음
        Class<?> sup = repoInterface.getSuperclass();
        if (sup != null && sup != Object.class) {
            e = resolveEntityTypeFromInterfaces(sup.getGenericInterfaces());
            if (e != null) {
                return e;
            }
        }

        // 3) 간접 상속/다중 상속 대비: 인터페이스 트리를 너비우선으로 훑기
        Queue<Class<?>> q = new ArrayDeque<>();
        q.add(repoInterface);
        while (!q.isEmpty()) {
            Class<?> cur = q.poll();
            Class<?> x = resolveEntityTypeFromInterfaces(cur.getGenericInterfaces());
            if (x != null) {
                return x;
            }
            for (Class<?> itf : cur.getInterfaces()) {
                q.add(itf);
            }
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
            } else if (t instanceof Class<?> c) {
                // 비제네릭 인터페이스 → 그 인터페이스가 다시 무엇을 상속하는지 확인
                Class<?> nested = resolveEntityType(c);
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
