package com.legendApi.config;

import com.legendApi.core.CustomJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {
    private final ConfigProperties config;

    @Autowired
    public DatabaseConfig(ConfigProperties config) {
        this.config = config;
    }

    @Bean
    public CustomJdbc getJdbc() {
        String url = config.getConfigValue("spring.datasource.url");
        String driverClassName = config.getConfigValue("spring.datasource.driverClassName");
        String username = config.getConfigValue("spring.datasource.username");
        String password = config.getConfigValue("spring.datasource.password");
        String catalog = config.getConfigValue("spring.datasource.catalog");
        String schema = config.getConfigValue("spring.datasource.schema");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setSchema(schema);
        dataSource.setCatalog(catalog);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return new CustomJdbc(dataSource);
    }
}
