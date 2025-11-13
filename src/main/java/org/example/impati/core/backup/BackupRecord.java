package org.example.impati.core.backup;

import java.time.LocalDateTime;

public record BackupRecord<T>(
        RecordType recordType,
        T data,
        LocalDateTime at
) {

    public static <T> BackupRecord<T> of(RecordType recordType, T data) {
        return new BackupRecord<>(
                recordType,
                data,
                LocalDateTime.now()
        );
    }
}
