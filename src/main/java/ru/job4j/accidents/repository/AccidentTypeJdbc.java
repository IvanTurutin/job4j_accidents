package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
/*
@Repository
*/
@AllArgsConstructor
public class AccidentTypeJdbc implements AccidentTypeRepository {
    @GuardedBy("this")
    private final JdbcTemplate jdbc;
    private final static String MODEL = "accident_types";
    private final static String ID = "id";
    private final static String FIND_ALL = String.format("select * from %s", MODEL);
    private final static String FIND_BY_ID = FIND_ALL + " where id = ?";
    private final static String ADD = String.format("insert into %s (name) values (?)", MODEL);
    private final static String UPDATE = String.format("UPDATE %s SET name = ? WHERE id = ?", MODEL);


    @Override
    public List<AccidentType> findAll() {
        return jdbc.query(FIND_ALL, getRowMapper());
    }

    @Override
    public Optional<AccidentType> add(AccidentType type) {
        Map<Integer, Object> args = Map.of(1, type.getName());
        int id = JdbcUtil.addAndGetId(jdbc, ADD, args, ID);
        if (id > 0) {
            type.setId(id);
            return Optional.of(type);
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccidentType> findById(int id) {
        return Optional.ofNullable(jdbc.queryForObject(FIND_BY_ID, getRowMapper(), id));
    }

    /**
     * Создает RowMapper для заполнения объект Rule
     * @return объект RowMapper
     */
    private RowMapper<AccidentType> getRowMapper() {
        return (rs, row) -> {
            AccidentType accidentType = new AccidentType();
            accidentType.setId(rs.getInt("id"));
            accidentType.setName(rs.getString("name"));
            return accidentType;
        };
    }

    @Override
    public boolean update(AccidentType type) {
        return jdbc.update(UPDATE, type.getName(), type.getId()) > 0;
    }

    /**
     * Удаляет все записи из таблицы
     */
    public void truncateTable() {
        jdbc.update("delete from accidents_rules");
        jdbc.update("delete from accidents");
        jdbc.update("delete from accident_types");
    }


}
