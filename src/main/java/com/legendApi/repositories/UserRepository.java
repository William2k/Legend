package com.legendApi.repositories;

import com.legendApi.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private NamedParameterJdbcTemplate _namedParameterJdbcTemplate;

    @Autowired
    private ConfigProperties configProp;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        _namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", "");

        String result = _namedParameterJdbcTemplate.queryForObject("SELECT first_Name FROM legend.users", namedParameters, String.class);
    }
}
