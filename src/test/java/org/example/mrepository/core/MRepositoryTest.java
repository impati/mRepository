package org.example.mrepository.core;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MRepositoryTest {

    DemoRepository sut = MRepositoryFactory.create(DemoRepository.class);

    @Test
    void save() {
        Demo demo = sut.save(new Demo(12, "impati"));

        assertThat(demo)
                .extracting(Demo::age, Demo::name)
                .contains(12, "impati");
    }

    @Test
    void findById() {
        sut.save(new Demo(12, "impati"));

        Demo demo = sut.findById("impati");

        assertThat(demo)
                .extracting(Demo::age, Demo::name)
                .contains(12, "impati");
    }

    @Test
    void findBy() {
        sut.save(new Demo(12, "impati"));

        List<Demo> demos = sut.findByAge(12);

        assertThat(demos).hasSize(1);
    }

    @Test
    void saveAll() {
        List<Demo> demos = List.of(
                new Demo(12, "hello"),
                new Demo(13, "world")
        );

        sut.saveAll(demos);

        assertThat(sut.findAll()).hasSize(2);
    }

    @Test
    void findAll() {
        List<Demo> demos = List.of(
                new Demo(12, "hello"),
                new Demo(13, "world")
        );
        sut.saveAll(demos);

        List<Demo> result = sut.findAll();

        assertThat(result).hasSize(2)
                .extracting(Demo::name)
                .contains("hello", "world");
    }

    @Test
    void delete() {
        List<Demo> demos = List.of(
                new Demo(12, "hello"),
                new Demo(13, "world")
        );
        sut.saveAll(demos);

        sut.delete(new Demo(12, "hello"));

        assertThat(sut.findAll()).hasSize(1)
                .extracting(Demo::name)
                .contains("world");
    }

    @Test
    void deleteAll() {
        List<Demo> demos = List.of(
                new Demo(12, "hello"),
                new Demo(13, "world")
        );
        sut.saveAll(demos);

        sut.deleteAll(demos);

        assertThat(sut.findAll()).isEmpty();
    }

    public interface DemoRepository extends MRepository<String, Demo> {

        List<Demo> findByAge(int age);
    }

    public record Demo(
            int age,

            @MId
            String name
    ) {

    }
}
