package org.example.impati.core.backup;

import java.util.List;
import org.example.impati.core.MRepository;

public class BackupEntityLoader<K, E> {

    private final Class<E> clazz;
    private final MRepository<K, E> repository;
    private final BackupRepository backupRepository;

    public BackupEntityLoader(Class<E> clazz, MRepository<K, E> repository, BackupRepository backupRepository) {
        this.clazz = clazz;
        this.repository = repository;
        this.backupRepository = backupRepository;
    }

    public void load() {
        List<BackupRecord<E>> backupRecords = backupRepository.readAll(clazz);
        for (BackupRecord<E> data : backupRecords) {
            if (data.recordType() == RecordType.SAVE) {
                repository.save(data.data());
            } else if (data.recordType() == RecordType.DELETE) {
                repository.delete(data.data());
            }
        }
    }
}

