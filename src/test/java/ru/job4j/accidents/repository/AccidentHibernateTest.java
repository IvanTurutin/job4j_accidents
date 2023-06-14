package ru.job4j.accidents.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.accidents.config.HibernateConfiguration;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AccidentHibernateTest {
    private static AccidentHibernate store;
    private static AccidentTypeHibernate typeStore;
    private static RuleHibernate ruleStore;
    private static Accident accident;

    @BeforeAll
    public static void initStore() {
        SimpleCrudRepository cr = new SimpleCrudRepository(new HibernateConfiguration().sf());
        store = new AccidentHibernate(cr);
        store.truncateTable();
        typeStore = new AccidentTypeHibernate(cr);
        ruleStore = new RuleHibernate(cr);


    }

    @BeforeEach
    public void createEntity() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 1");
        typeStore.add(accidentType);

        Rule rule = new Rule();
        rule.setName("Rule 1");
        Rule rule2 = new Rule();
        rule2.setName("Rule 2");
        ruleStore.add(rule);
        ruleStore.add(rule2);

        accident = new Accident();
        accident.setName("Accident 1");
        accident.setAddress("Address 1");
        accident.setText("Text 1");
        accident.setType(accidentType);
        Set<Rule> rules = new HashSet<>();
        rules.add(rule);
        rules.add(rule2);
        accident.setRules(rules);

    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
        typeStore.truncateTable();
        ruleStore.truncateTable();
    }


    @Test
    void whenFindAll() {
        AccidentType accidentType = new AccidentType();
        accidentType.setName("AccidentType 2");
        typeStore.add(accidentType);

        Rule rule = new Rule();
        rule.setName("Rule 3");
        Rule rule2 = new Rule();
        rule2.setName("Rule 4");
        ruleStore.add(rule);
        ruleStore.add(rule2);

        Accident accident2 = new Accident();
        accident2.setName("Accident 2");
        accident2.setAddress("Address 2");
        accident2.setText("Text 2");
        accident2.setType(accidentType);
        accident2.setRules(Set.of(rule, rule2));

        store.add(accident2);
        store.add(accident);

        List<Accident> accidents = store.findAll();
        assertThat(accidents).isNotEmpty().hasSize(2).contains(accident, accident2);
        accidents.forEach(System.out::println);

    }

    @Test
    void whenAdd() {
        System.out.println("accident before add = " + accident);
        store.add(accident);
        System.out.println("accident after add = " + accident);
        Optional<Accident> accidentDb = store.findById(accident.getId());
        assertThat(accidentDb).isPresent();
        assertThat(accidentDb.get().getId()).isEqualTo(accident.getId());
        assertThat(accidentDb.get().getName()).isEqualTo(accident.getName());
        assertThat(accidentDb.get().getText()).isEqualTo(accident.getText());
        assertThat(accidentDb.get().getType()).isEqualTo(accident.getType());
        assertThat(accidentDb.get().getAddress()).isEqualTo(accident.getAddress());
        assertThat(accidentDb.get().getRules()).isNotEmpty().hasSize(accident.getRules().size()).containsAll(accident.getRules());
        accidentDb.ifPresent(System.out::println);
    }

    @Test
    void whenFindWithNullRules() {
        accident.setRules(new HashSet<>());
        store.add(accident);
        Optional<Accident> accidentDb = store.findById(accident.getId());
        assertThat(accidentDb).isPresent();
        assertThat(accidentDb.get().getId()).isEqualTo(accident.getId());
        assertThat(accidentDb.get().getName()).isEqualTo(accident.getName());
        assertThat(accidentDb.get().getText()).isEqualTo(accident.getText());
        assertThat(accidentDb.get().getType()).isEqualTo(accident.getType());
        assertThat(accidentDb.get().getAddress()).isEqualTo(accident.getAddress());
        assertThat(accidentDb.get().getRules()).isEmpty();
    }

    @Test
    void whenUpdate() {
        store.add(accident);
        accident.setName("Updated name");
        accident.setText("Updated text");
        accident.setAddress("Updated address");

        AccidentType accidentType2 = new AccidentType();
        accidentType2.setName("AccidentType 2");
        typeStore.add(accidentType2);
        accident.setType(accidentType2);

        Rule rule3 = new Rule();
        rule3.setName("Rule 3");
        ruleStore.add(rule3);
        accident.addRule(rule3);

        store.update(accident);

        Optional<Accident> accidentDb = store.findById(accident.getId());
        assertThat(accidentDb).isPresent();
        assertThat(accidentDb.get().getId()).isEqualTo(accident.getId());
        assertThat(accidentDb.get().getName()).isEqualTo(accident.getName());
        assertThat(accidentDb.get().getText()).isEqualTo(accident.getText());
        assertThat(accidentDb.get().getType()).isEqualTo(accident.getType());
        assertThat(accidentDb.get().getAddress()).isEqualTo(accident.getAddress());
        assertThat(accidentDb.get().getRules()).isNotEmpty().hasSize(accident.getRules().size()).containsAll(accident.getRules());

        accident.setRules(new HashSet<>());
        store.update(accident);

        accidentDb = store.findById(accident.getId());
        assertThat(accidentDb).isPresent();
        assertThat(accidentDb.get().getRules()).isEmpty();
    }
}