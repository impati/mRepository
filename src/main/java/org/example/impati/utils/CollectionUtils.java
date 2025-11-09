package org.example.impati.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    @SuppressWarnings("unchecked")
    public static List<Object> toCollection(Object arg) {
        if (arg == null) {
            return List.of();
        }
        if (arg.getClass().isArray()) {
            int len = Array.getLength(arg);
            List<Object> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(Array.get(arg, i));
            }
            return list;
        }
        // Iterable 지원
        if (arg instanceof Iterable<?> it) {
            List<Object> list = new ArrayList<>();
            for (Object e : it) {
                list.add(e);
            }
            return list;
        }
        throw new IllegalArgumentException("saveAll requires a Collection/Iterable/array argument, but was: " + arg.getClass());
    }
}
