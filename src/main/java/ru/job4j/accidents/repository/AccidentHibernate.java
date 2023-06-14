package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class AccidentHibernate implements AccidentRepository {
    private final SimpleCrudRepository cr;

    public static final String TABLE_ALIAS = "t";
    public static final String MODEL = "Accident";
    public static final String ID = "fID";
    public static final String FIND_ALL_STATEMENT = String.format(
            "select %s from %s as %s", TABLE_ALIAS, MODEL, TABLE_ALIAS
    );
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT
            + String.format(" order by %s.id", TABLE_ALIAS);
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where t.id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);

    @Override
    public List<Accident> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, Accident.class);
    }

    @Override
    public Optional<Accident> add(Accident accident) {
        return cr.add(accident);
    }

    @Override
    public Optional<Accident> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, Accident.class,
                Map.of(ID, id)
        );
    }

    @Override
    public boolean update(Accident accident) {
        return cr.run(session -> session.merge(accident));
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {
        cr.run("delete from accidents_rules", new HashMap<>());
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

}
