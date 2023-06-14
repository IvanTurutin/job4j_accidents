package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.job4j.accidents.model.Rule;

import java.util.*;
import java.util.stream.Collectors;

@ThreadSafe
/*
@Repository
*/
@AllArgsConstructor
public class RuleJdbc implements RuleRepository {
    @GuardedBy("this")
    private final JdbcTemplate jdbc;
    private final static String MODEL = "rules";
    private final static String ID = "id";
    private final static String FIND_ALL = String.format("select * from %s", MODEL);
    private final static String FIND_BY_ID = FIND_ALL + " where id = ?";
    private final static String ADD = String.format("insert into %s (name) values (?)", MODEL);
    private final static String UPDATE = String.format("UPDATE %s SET name = ? WHERE id = ?", MODEL);

    @Override
    public List<Rule> findAll() {
        return jdbc.query(FIND_ALL, getRowMapper());
    }

    @Override
    public Optional<Rule> add(Rule rule) {
        Map<Integer, Object> args = Map.of(1, rule.getName());
        int id = JdbcUtil.addAndGetId(jdbc, ADD, args, ID);
        if (id > 0) {
            rule.setId(id);
            return Optional.of(rule);
        }
        return Optional.empty();

    }

    @Override
    public Optional<Rule> findById(int id) {
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID, getRowMapper(), id));
    }

    /**
     * Создает RowMapper для заполнения объект Rule
     * @return объект RowMapper
     */
    private RowMapper<Rule> getRowMapper() {
        return (rs, row) -> {
            Rule rule = new Rule();
            rule.setId(rs.getInt("id"));
            rule.setName(rs.getString("name"));
            return rule;
        };
    }

    @Override
    public boolean update(Rule type) {
        return jdbc.update(UPDATE, type.getName(), type.getId()) > 0;
    }

    @Override
    public Set<Rule> findRules(List<Integer> ids) {
        return ids.stream()
                .map(this::findById)
                .map(ro -> ro.orElseThrow(() -> new NoSuchElementException("Такая статья нарушения не найдена")))
                .collect(Collectors.toSet());
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {
        jdbc.update("delete from accidents_rules");
        jdbc.update("delete from rules");
    }


}
