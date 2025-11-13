package org.example.impati.core.backup;

public interface BackupMapper {

    <T> String serialize(BackupRecord<T> data, Class<T> clazz);

    <T> BackupRecord<T> deserialize(String str, Class<T> clazz);
}
