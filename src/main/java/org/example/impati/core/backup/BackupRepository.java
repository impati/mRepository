package org.example.impati.core.backup;

import java.util.List;

public interface BackupRepository {

    <T> void save(T entity, Class<T> clazz);

    <T> void delete(T entity, Class<T> clazz);

    <T> List<BackupRecord<T>> readAll(Class<T> clazz);
}
