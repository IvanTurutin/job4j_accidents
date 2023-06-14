package ru.job4j.accidents.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.accidents.config.HibernateConfiguration;
import ru.job4j.accidents.model.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleHibernateTest {
    private static RuleHibernate store;

    @BeforeAll
    public static void initStore() {
        store = new RuleHibernate(new SimpleCrudRepository(new HibernateConfiguration().sf()));
        store.truncateTable();    }

    @BeforeEach
    public void createEntity() {
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenFindAll() {
        Rule rule = new Rule();
        rule.setName("Rule 1");
        Rule rule1 = new Rule();
        rule1.setName("Rule 2");
        store.add(rule);
        store.add(rule1);
        List<Rule> ruleList = store.findAll();
        assertThat(ruleList).hasSize(2);
        ruleList.forEach(System.out::println);
    }

    @Test
    void whenAdd() {
        Rule rule = new Rule();
        rule.setName("Rule 1");
        System.out.println("Rule before add = " + rule);
        store.add(rule);
        System.out.println("Rule after add = " + rule);
        List<Rule> ruleList = store.findAll();
        assertThat(ruleList).hasSize(1);
        ruleList.forEach(System.out::println);
        assertThat(ruleList.get(0).getName()).isEqualTo(rule.getName());
    }

    @Test
    void whenFindById() {
        Rule rule = new Rule();
        rule.setName("Rule 1");
        store.add(rule);
        List<Rule> ruleList = store.findAll();
        assertThat(ruleList).hasSize(1);
        Rule ruleDb = ruleList.get(0);
        Optional<Rule> ruleOptional = store.findById(ruleDb.getId());
        assertTrue(ruleOptional.isPresent());
        assertThat(ruleOptional.get()).isEqualTo(ruleDb);
    }

    @Test
    void whenUpdate() {
        Rule rule = new Rule();
        rule.setName("Rule 1");
        store.add(rule);
        List<Rule> ruleList = store.findAll();
        assertThat(ruleList).hasSize(1);
        Rule ruleDb = ruleList.get(0);
        ruleDb.setName("Updated Rule 1");
        boolean rslt = store.update(ruleDb);
        assertTrue(rslt);
        Optional<Rule> ruleOptional = store.findById(ruleDb.getId());
        assertTrue(ruleOptional.isPresent());
        assertThat(ruleOptional.get().getName()).isEqualTo(ruleDb.getName());
    }

    @Test
    void whenFindRules() {
        Rule rule = new Rule();
        rule.setName("Rule 1");
        Rule rule1 = new Rule();
        rule1.setName("Rule 2");
        store.add(rule);
        store.add(rule1);
        List<Rule> ruleList = store.findAll();
        assertThat(ruleList).hasSize(2);
        List<Integer> ids = new ArrayList<>();
        ids.add(ruleList.get(0).getId());
        ids.add(ruleList.get(1).getId());
        Set<Rule> rules = store.findRules(ids);
        assertThat(rules).hasSize(2).contains(ruleList.get(0), ruleList.get(1));
    }
}