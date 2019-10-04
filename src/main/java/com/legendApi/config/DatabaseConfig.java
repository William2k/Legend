package com.legendApi.config;

import com.legendApi.core.CustomJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DatabaseConfig {
    @Bean
    public CustomJdbc getJdbc() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("legendAdmin");
        dataSource.setPassword("L3g3nd");
        dataSource.setCatalog("legend");
        dataSource.setSchema("legend");
        dataSource.setUrl("jdbc:postgresql:legend");
        return new CustomJdbc(dataSource);
    }
}
