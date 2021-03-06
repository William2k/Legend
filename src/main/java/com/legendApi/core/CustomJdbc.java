package com.legendApi.core;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomJdbc extends NamedParameterJdbcTemplate {
    public CustomJdbc(DataSource dataSource) {
        super(dataSource);
    }

    private <T> T queryReturn(List<T> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }
        else if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        else{
            return results.iterator().next();
        }
    }

    public <T> T queryForNullableObject(String sql, MapSqlParameterSource parameters, T defaultValue) throws DataAccessException {
        return Optional.ofNullable((T) queryForObject(sql, parameters, defaultValue.getClass())).orElse(defaultValue);
    }
}
