package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
@Slf4j
public class AccidentJdbc implements AccidentRepository {

    @GuardedBy("this")
    private final JdbcTemplate jdbc;
    private final static String MODEL = "accidents";
    private final static String ID = "id";
    private final static String ACCIDENT_TYPE_MODEL = "accident_types";
    private final static String ACCIDENTS_RULES_TABLE = "accidents_rules";
    private final static String RULES_TABLE = "rules";
    public final static String ACCIDENT_ID = "accident_id";
    public final static String ACCIDENT_NAME = "name";
    public final static String ACCIDENT_TEXT = "text";
    public final static String ACCIDENT_ADDRESS = "address";
    public final static String ACCIDENT_TYPE_ID = "accident_type_id";
    public final static String TYPE_ID = "type_id";
    public final static String TYPE_NAME = "type_name";
    public final static String RULE_ID = "rule_id";
    public final static String RULE_NAME = "rule_name";
    private final static String FIND_ALL = String.format(
            "select "
            + "a.id as %s, a.name as %s, %s, %s, "
            + "t.id as %s, t.name as %s, "
            + "r.id as %s, r.name as %s "
            + "from %s as a "
            + "left join %s as t "
            + "on a.accident_type_id = t.id "
            + "left join %s as ar "
            + "on ar.accident_id = a.id "
            + "left join %s as r "
            + "on r.id = ar.rule_id",
            ACCIDENT_ID, ACCIDENT_NAME, ACCIDENT_TEXT, ACCIDENT_ADDRESS,
            TYPE_ID, TYPE_NAME,
            RULE_ID, RULE_NAME,
            MODEL,
            ACCIDENT_TYPE_MODEL,
            ACCIDENTS_RULES_TABLE,
            RULES_TABLE
    );
    private final static String UPDATE_ACCIDENT = String.format(
            "update %s set %s = ?, %s = ?, %s = ?, %s = ? where id = ?",
            MODEL, ACCIDENT_NAME, ACCIDENT_TEXT, ACCIDENT_ADDRESS, ACCIDENT_TYPE_ID
    );
    private final static String FIND_BY_ID = FIND_ALL + " where a.id = ?";
    private final static String ADD = String.format(
            "insert into %s (name, text, address, accident_type_id) values (?, ?, ?, ?)",
            MODEL
    );
    private final static String ADD_RULE = String.format(
            "insert into %s (accident_id, rule_id) values (?, ?)",
            ACCIDENTS_RULES_TABLE
    );
    private final static String DELETE_ACCIDENT_RULE_MAPPING = String.format(
            "delete from %s where accident_id = ?",
            ACCIDENTS_RULES_TABLE
    );

    @Override
    public List<Accident> findAll() {
        log.debug(FIND_ALL);
        return Objects.requireNonNull(
                jdbc.query(
                        FIND_ALL,
                        (Object[]) null,
                        new AccidentRowMapperResultSetExtractor(new AccidentRowMapper())
                ));
    }

    @Override
    @Transactional
    public Optional<Accident> add(Accident accident) {
        Map<Integer, Object> args = Map.of(
                1, accident.getName(),
                2, accident.getText(),
                3, accident.getAddress(),
                4, accident.getType().getId()
        );
        int id = JdbcUtil.addAndGetId(jdbc, ADD, args, ID);
        if (id > 0) {
            accident.setId(id);
            if (!addAccidentRuleMapping(accident)) {
                return Optional.empty();
            }
            return Optional.of(accident);
        }
        return Optional.empty();
    }

    /**
     * Добавляет связь между нарушением и статьей нарушения
     * @param accident нарушение
     * @return true если связи созданы, false если не созданы
     */
    private boolean addAccidentRuleMapping(Accident accident) {
        for (Rule rule : accident.getRules()) {
            log.debug(ADD_RULE);
            if (jdbc.update(ADD_RULE, accident.getId(), rule.getId()) < 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<Accident> findById(int id) {
        log.debug(FIND_BY_ID);
        List<Accident> accidents = jdbc.query(
                FIND_BY_ID,
                new Object[]{id},
                new AccidentRowMapperResultSetExtractor(new AccidentRowMapper())
        );

        assert accidents != null;
        return accidents.size() == 1 ? Optional.ofNullable(accidents.get(0)) : Optional.empty();
    }

    @Override
    @Transactional
    public boolean update(Accident newAccident) {
        Optional<Accident> oldAccident = findById(newAccident.getId());
        if (oldAccident.isEmpty()) {
            throw new IllegalArgumentException("Такой объект не найден в БД, поэтому обновить его нельзя. "
                    + "Сначала его необходимо сохранить");
        }
        boolean rslt = true;
        if (!accidentEqual(oldAccident.get(), newAccident)) {
            rslt = updateAccident(newAccident);
        }

        if (!rslt) {
            return false;
        }

        if (
                oldAccident.get().getRules().size() != newAccident.getRules().size()
                || !oldAccident.get().getRules().containsAll(newAccident.getRules())
        ) {
            return updateRules(oldAccident.get(), newAccident);
        }

        return rslt;
    }

    /**
     * Сравнивает значения полей нарушений на эквивалентность
     * @param oldAccident старый объект нарушения
     * @param newAccident новый объект нарушения
     * @return true если эквивалентны, false если разные
     */
    private boolean accidentEqual(Accident oldAccident, Accident newAccident) {
        return Objects.equals(oldAccident.getName(), newAccident.getName())
                && Objects.equals(oldAccident.getText(), newAccident.getText())
                && Objects.equals(oldAccident.getAddress(), newAccident.getAddress())
                && Objects.equals(oldAccident.getType(), newAccident.getType());
    }

    /**
     * Обновляет нарушение без статей нарушений
     * @param accident нарушение
     * @return true если обновилось, false если не обновилось
     */
    private boolean updateAccident(Accident accident) {
        log.debug(UPDATE_ACCIDENT);
        return jdbc.update(
                UPDATE_ACCIDENT,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId()
        ) > 0;
    }

    /**
     * Обновляет статьи нарушения в нарушении
     * @param oldAccident старый объект нарушения
     * @param newAccident новый объект нарушения
     * @return true если обновилось, false если не обновилось
     */
    private boolean updateRules(Accident oldAccident, Accident newAccident) {
        if (oldAccident.getRules().size() > 0 && !deleteAccidentRuleMapping(oldAccident.getId())) {
            return false;
        }
        return addAccidentRuleMapping(newAccident);
    }

    /**
     * Очищает все связи нарушения и статьи нарушения для нарушения с определенным id
     * @param id нарушения
     * @return true если очищено, false если не очищено
     */
    private boolean deleteAccidentRuleMapping(int id) {
        log.debug(DELETE_ACCIDENT_RULE_MAPPING);
        return jdbc.update(DELETE_ACCIDENT_RULE_MAPPING, id) > 0;
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {
        jdbc.update("delete from accidents_rules");
        jdbc.update("delete from accidents");
    }
}
