package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class JdbcUtil {

    /**
     * Добавляет объект в БД и возвращает присвоенный объекту id
     * @param jdbc JdbcTemplate
     * @param addStatement запрос на добавление объекта
     * @param args аргументы запроса
     * @param id наименование поля, которое требуется вернуть по окончании выполнения запроса
     * @return идентификатор, присвоенный базой данных объекту
     */
    public static int addAndGetId(JdbcTemplate jdbc, String addStatement, Map<Integer, Object> args, String id) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        log.debug(addStatement);
                        PreparedStatement ps =
                                connection.prepareStatement(addStatement, new String[] {id});
                        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
                            ps.setObject(entry.getKey(), entry.getValue());
                        }
                        return ps;
                    }
                },
                keyHolder
        );
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

}
