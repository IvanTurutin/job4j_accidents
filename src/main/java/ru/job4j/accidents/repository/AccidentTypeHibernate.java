package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class AccidentTypeHibernate implements AccidentTypeRepository {

    private final SimpleCrudRepository cr;

    public static final String TABLE_ALIAS = "t";
    public static final String MODEL = "AccidentType";
    public static final String ID = "fID";
    public static final String FIND_ALL_STATEMENT = String.format(
            "select %s from %s as %s", TABLE_ALIAS, MODEL, TABLE_ALIAS
    );
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT
            + String.format(" order by %s.id", TABLE_ALIAS);
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);


    @Override
    public List<AccidentType> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, AccidentType.class);
    }

    @Override
    public Optional<AccidentType> add(AccidentType type) {
        return cr.add(type);
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, AccidentType.class,
                Map.of(ID, id)
        );
    }

    @Override
    public boolean update(AccidentType type) {
        return cr.run(session -> session.merge(type));
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {

        cr.run("delete from accidents_rules", new HashMap<>());
        cr.run(AccidentHibernate.TRUNCATE_TABLE, new HashMap<>());
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }
}
