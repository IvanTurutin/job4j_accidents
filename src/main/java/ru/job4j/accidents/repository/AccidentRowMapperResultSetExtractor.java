package ru.job4j.accidents.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import ru.job4j.accidents.model.Accident;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AccidentRowMapperResultSetExtractor implements ResultSetExtractor<List<Accident>> {
        private final RowMapper<Map<Integer, Accident>> rowMapper;

        public AccidentRowMapperResultSetExtractor(RowMapper<Map<Integer, Accident>> rowMapper) {
            Assert.notNull(rowMapper, "RowMapper is required");
            this.rowMapper = rowMapper;
        }

        @Override
        public List<Accident> extractData(ResultSet rs) throws SQLException {
            int rowNum = 0;
            Map<Integer, Accident> accidentsMap = new HashMap<>();
            while (rs.next()) {
                accidentsMap = this.rowMapper.mapRow(rs, rowNum++);
            }
            assert accidentsMap != null;
            return new ArrayList<>(accidentsMap.values());
        }

}
