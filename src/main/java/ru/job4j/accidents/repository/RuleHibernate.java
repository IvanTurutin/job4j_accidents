package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class RuleHibernate implements RuleRepository {

    private final SimpleCrudRepository cr;

    public static final String TABLE_ALIAS = "t";
    public static final String MODEL = "Rule";
    public static final String ID = "fID";
    public static final String FIND_ALL_STATEMENT = String.format(
            "select %s from %s as %s", TABLE_ALIAS, MODEL, TABLE_ALIAS
    );
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT
            + String.format(" order by %s.id", TABLE_ALIAS);
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);


    @Override
    public List<Rule> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, Rule.class);
    }

    @Override
    public Optional<Rule> add(Rule rule) {
        return cr.add(rule);
    }

    @Override
    public Optional<Rule> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, Rule.class,
                Map.of(ID, id)
        );
    }

    @Override
    public boolean update(Rule type) {
        return cr.run(session -> session.merge(type));
    }

    @Override
    public Set<Rule> findRules(List<Integer> ids) {
        return ids
                .stream()
                .map(this::findById)
                .map(ro -> ro.orElseThrow(() -> new NoSuchElementException("Такая статья нарушения не найдена")))
                .collect(Collectors.toSet());
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {
        cr.run("DELETE FROM accidents_rules", new HashMap<>());
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }
}
