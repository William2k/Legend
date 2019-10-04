package com.legendApi.repositories;

import com.legendApi.config.ConfigProperties;
import com.legendApi.core.CustomJdbc;
import com.legendApi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    @Autowired
    private CustomJdbc _customJdbc;

    @Autowired
    private ConfigProperties configProp;

    public UserRepository(CustomJdbc customJdbc) {
        _customJdbc = customJdbc;
    }

    public List<User> GetUsers() {
        List<User> result = _customJdbc.query("SELECT * FROM legend.users", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmailAddress(rs.getString("email_address"));
            user.setPassword(rs.getString("password"));
            user.setActive(rs.getBoolean("is_active"));
            return user;
        });

        return result;
    }
}