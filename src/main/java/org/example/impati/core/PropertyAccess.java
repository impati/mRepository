package org.example.impati.core;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PropertyAccess<E> {

    private final Map<String, Function<E, Object>> accessors;

    private PropertyAccess(Map<String, Function<E, Object>> accessors) {
        this.accessors = accessors;
    }

    public static <E> PropertyAccess<E> forType(Class<E> type) {
        Map<String, Function<E, Object>> map = new HashMap<>();

        // 1) record
        if (type.isRecord()) {
            for (var rc : type.getRecordComponents()) {
                var accessor = rc.getAccessor(); // e.g. Demo#age()
                map.put(lowerFirst(rc.getName()), instanceMethod(accessor));
                map.put(rc.getName(), instanceMethod(accessor)); // 원래 이름도 허용
            }
        }

        // 2) POJO (JavaBeans getter)
        for (Method m : type.getMethods()) {
            if (m.getParameterCount() == 0) {
                String name = m.getName();
                String prop = null;
                if (name.startsWith("get") && name.length() > 3) {
                    prop = lowerFirst(name.substring(3));
                } else if (name.startsWith("is") && name.length() > 2) {
                    prop = lowerFirst(name.substring(2));
                }
                if (prop != null) {
                    map.putIfAbsent(prop, instanceMethod(m)); // record 우선, 없을 때만 등록
                }
            }
        }

        return new PropertyAccess<>(Collections.unmodifiableMap(map));
    }

    public Object get(E entity, String property) {
        var fn = accessors.get(property);
        if (fn == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        return fn.apply(entity);
    }

    private static <E> Function<E, Object> instanceMethod(Method m) {
        m.setAccessible(true);
        return e -> {
            try {
                return m.invoke(e);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        };
    }

    private static String lowerFirst(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
