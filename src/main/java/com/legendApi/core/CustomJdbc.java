package com.legendApi.core;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.Map;

public class CustomJdbc extends NamedParameterJdbcTemplate {
    public CustomJdbc(DataSource dataSource) {
        super(dataSource);
    }
}
