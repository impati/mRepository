package org.example.impati.core.backup;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleBackupMapper implements BackupMapper {

    private static final String DELIMITER = "~!~~!~";

    @Override
    public <T> String serialize(BackupRecord<T> data, Class<T> clazz) {
        T obj = data.data();

        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append("[");

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            try {
                Object value = f.get(obj);
                sb.append(f.getName())
                        .append("=")
                        .append(value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return data.recordType() + DELIMITER + sb;
    }

    @Override
    public <T> BackupRecord<T> deserialize(String str, Class<T> clazz) {
        String[] split = str.split(DELIMITER);
        RecordType recordType = RecordType.valueOf(split[0]);
        try {
            int start = split[1].indexOf('[');
            int end = split[1].lastIndexOf(']');
            if (start < 0 || end < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid format: " + split[1]);
            }

            String body = split[1].substring(start + 1, end); // "age=12, name=impati"

            // "age=12, name=impati" -> Map<String, String>
            Map<String, String> fieldMap = new HashMap<>();
            String[] parts = body.split(",");
            for (String part : parts) {
                String[] kv = part.split("=");
                if (kv.length != 2) {
                    continue;
                }
                String key = kv[0].trim();
                String value = kv[1].trim();
                fieldMap.put(key, value);
            }

            // 기본 생성자가 있다고 가정
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            // 필드에 값 주입
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                String valueStr = fieldMap.get(f.getName());
                if (valueStr == null) {
                    continue;
                }
                Object converted = convert(valueStr, f.getType());
                f.set(instance, converted);
            }

            return BackupRecord.of(recordType, instance);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Object convert(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        }
        // 필요 시 enum, LocalDate 등 추가
        throw new IllegalArgumentException("Unsupported field type: " + targetType);
    }
}
