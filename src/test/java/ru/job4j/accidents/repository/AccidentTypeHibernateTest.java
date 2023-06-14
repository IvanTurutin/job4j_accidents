package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import ru.job4j.accidents.config.HibernateConfiguration;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
@AllArgsConstructor
class AccidentTypeHibernateTest {
    private static AccidentTypeHibernate store;

    @BeforeAll
    public static void initStore() {
        store = new AccidentTypeHibernate(new SimpleCrudRepository(new HibernateConfiguration().sf()));
        store.truncateTable();
    }

    @BeforeEach
    public void createEntity() {
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenFindAll() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 1");
        AccidentType accidentType2 = new AccidentType();
        accidentType2.setName("AccidentType 2");
        store.add(accidentType);
        store.add(accidentType2);
        System.out.println(accidentType);
        System.out.println(accidentType2);
        List<AccidentType> accidentTypeDb = store.findAll();
        assertThat(accidentTypeDb).hasSize(2);
        accidentTypeDb.forEach(System.out::println);
    }

    @Test
    void whenAdd() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 1");
        System.out.println("accidentType before add = " + accidentType);
        store.add(accidentType);
        System.out.println("accidentType after add = " + accidentType);
        List<AccidentType> accidentTypeDb = store.findAll();
        assertThat(accidentTypeDb).hasSize(1);
        accidentTypeDb.forEach(System.out::println);
        assertThat(accidentTypeDb.get(0).getName()).isEqualTo(accidentType.getName());
    }

    @Test
    void whenFindById() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 1");
        store.add(accidentType);
        Optional<AccidentType> typeOptional = store.findById(accidentType.getId());
        assertTrue(typeOptional.isPresent());
        assertThat(typeOptional.get()).isEqualTo(accidentType);
    }

    @Test
    void whenUpdate() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 1");
        store.add(accidentType);
        List<AccidentType> accidentTypes = store.findAll();
        assertThat(accidentTypes).hasSize(1);
        AccidentType accidentTypeDb = accidentTypes.get(0);
        accidentTypeDb.setName("Updated AccidentType 1");
        boolean rslt = store.update(accidentTypeDb);
        assertTrue(rslt);
        Optional<AccidentType> typeOptional = store.findById(accidentTypeDb.getId());
        assertTrue(typeOptional.isPresent());
        assertThat(typeOptional.get().getName()).isEqualTo(accidentTypeDb.getName());
    }
}