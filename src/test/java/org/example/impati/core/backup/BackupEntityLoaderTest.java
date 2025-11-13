package org.example.impati.core.backup;

import org.example.impati.core.MId;
import org.example.impati.core.MRepository;
import org.example.impati.core.MRepositoryFactory;
import org.junit.jupiter.api.Test;

class BackupEntityLoaderTest {

    DemoRepository demoRepository = MRepositoryFactory.create(DemoRepository.class);

    BackupEntityLoader<String, Demo> loader = new BackupEntityLoader<>(
            Demo.class,
            demoRepository,
            new FileBackupRepository(
                    new SimpleBackupMapper(),
                    "/Users/jun-yeongchoe/Desktop/m-repository/src/main/resources/data/"
            )
    );

    @Test
    void test() {
        loader.load();

        demoRepository.findAll().forEach(System.out::println);
    }

    interface DemoRepository extends MRepository<String, Demo> {

    }

    public static class Demo {

        private int age;
        @MId
        private String name;

        @Override
        public String toString() {
            return "Demo{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
