package org.example.impati.core.backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class BackupProcessor {

    private final BackupMapper backupMapper;
    private final String dir;

    public BackupProcessor(BackupMapper backupMapper, String dir) {
        this.backupMapper = backupMapper;
        this.dir = dir;
    }

    public <T> void save(T entity, Class<T> clazz) {
        append(RecordType.SAVE, entity, clazz);
    }

    public <T> void delete(T entity, Class<T> clazz) {
        append(RecordType.DELETE, entity, clazz);
    }

    private <T> void append(final RecordType recordType, final T entity, final Class<T> clazz) {
        File file = new File(dir);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) {
                throw new IllegalStateException("디렉토리 생성 실패");
            }
        }
        String serialize = backupMapper.serialize(BackupRecord.of(recordType, entity), clazz) + System.lineSeparator();

        try {
            Files.write(
                    Paths.get(dir + clazz.getSimpleName()),
                    serialize.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T> List<BackupRecord<T>> readAll(Class<T> clazz) {
        try {
            File file = Paths.get(dir, clazz.getSimpleName()).toFile();
            if (!file.exists()) {
                return List.of();
            }

            String origin = new String(Files.readAllBytes(file.toPath()));
            System.out.println("origin=" + origin);
            return Arrays.stream(origin.split(System.lineSeparator()))
                    .map(str -> backupMapper.deserialize(str, clazz))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
