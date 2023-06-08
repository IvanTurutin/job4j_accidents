package ru.job4j.accidents.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AccidentRowMapper implements RowMapper<Map<Integer, Accident>> {
    private final Map<Integer, Accident> accidents = new HashMap<>();
    @Override
    public Map<Integer, Accident> mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("row in getRowMapper() = " + rowNum);
        int key = rs.getInt(AccidentJdbc.ACCIDENT_ID);
        Accident accident = accidents.compute(key, (k, v) -> {
            try {
                return v == null ? createAccident(rs) : addRule(rs, v);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        log.debug("accident in getRowMapper() = " + accident);
        return accidents;
    }

    /**
     * Собирает объект нарушения из ResultSet
     * @param rs ResultSet
     * @return объект нарушения
     * @throws SQLException ошибка БД
     */
    private Accident createAccident(ResultSet rs) throws SQLException {
        Accident accident = new Accident();
        accident.setId(rs.getInt(AccidentJdbc.ACCIDENT_ID));
        accident.setName(rs.getString(AccidentJdbc.ACCIDENT_NAME));
        accident.setText(rs.getString(AccidentJdbc.ACCIDENT_TEXT));
        accident.setAddress(rs.getString(AccidentJdbc.ACCIDENT_ADDRESS));
        AccidentType accidentType = new AccidentType();
        accidentType.setId(rs.getInt(AccidentJdbc.TYPE_ID));
        accidentType.setName(rs.getString(AccidentJdbc.TYPE_NAME));
        accident.setType(accidentType);
        return addRule(rs, accident);
    }

    /**
     * Добавляет в объект нарушения статью нарушения из ResultSet
     * @param rs ResultSet
     * @param accident объект нарушения
     * @return объект нарушения
     * @throws SQLException ошибка БД
     */
    private Accident addRule(ResultSet rs, Accident accident) throws SQLException {
        if (rs.getInt(AccidentJdbc.RULE_ID) != 0) {
            Rule rule = new Rule();
            rule.setId(rs.getInt(AccidentJdbc.RULE_ID));
            rule.setName(rs.getString(AccidentJdbc.RULE_NAME));
            accident.addRule(rule);
        }
        return accident;
    }
}
