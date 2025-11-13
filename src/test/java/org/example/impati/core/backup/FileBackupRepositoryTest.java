package org.example.impati.core.backup;

import java.util.List;
import org.example.impati.core.MId;
import org.junit.jupiter.api.Test;

class FileBackupRepositoryTest {

    BackupMapper mapper = new SimpleBackupMapper();

    FileBackupRepository sut = new FileBackupRepository(
            mapper,
            "/Users/jun-yeongchoe/Desktop/m-repository/src/main/resources/data/"
    );

    @Test
    void save() {
        sut.save(new Demo(12, "impati"), Demo.class);
    }

    @Test
    void delete() {
        sut.delete(new Demo(12, "impati"), Demo.class);
    }

    @Test
    void readAll() {
        List<BackupRecord<Demo>> backupRecords = sut.readAll(Demo.class);
        for (BackupRecord<Demo> backupRecord : backupRecords) {
            System.out.println(backupRecord);
        }
    }

    public static class Demo {

        private int age;
        @MId
        private String name;

        public Demo(final int age, final String name) {
            this.age = age;
            this.name = name;
        }

        public Demo() {
        }
    }
}
