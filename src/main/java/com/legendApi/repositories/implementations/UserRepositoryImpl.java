package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.User;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public UserRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    private UserEntity userRowMapping(ResultSet rs, int rowNum) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setPassword(rs.getString("password"));
        user.setIsActive(rs.getBoolean("is_active"));
        return user;
    }

    @Override
    public UserEntity getByUsername(String username) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE normalised_username = :username";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username.toUpperCase());

        UserEntity result = customJdbc.queryForObject(sql, parameters, this::userRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getAll() {
        List<UserEntity> result = customJdbc.query("SELECT * FROM legend.users", this::userRowMapping);

        return result;
    }

    @Override
    public UserEntity getById(long id) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        UserEntity result = customJdbc.queryForObject(sql, parameters, this::userRowMapping);

        return result;
    }

    @Override
    public long add(UserEntity user) {
        String sql = "INSERT INTO legend.users(username, normalised_username, first_name, last_name, email_address, password, is_active) " +
                "VALUES (:username, :normalisedUsername, :firstName, :lastName, :emailAddress, :password, :isActive)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("normalisedUsername", user.getUsername().toUpperCase());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getEmailAddress());
        parameters.put("password", user.getPassword());
        parameters.put("isActive", user.getIsActive());

        return customJdbc.update(sql, parameters);
    }

    @Override
    public void update(UserEntity user) {
        String sql = "UPDATE legend.users " +
                "SET first_name=:firstName, last_name=:lastName, email_address=:emailAddress, password=:password, is_active=:isActive " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", user.getId());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getEmailAddress());
        parameters.put("password", user.getPassword());
        parameters.put("isActive", user.getIsActive());

        customJdbc.update(sql, parameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.users" +
                "SET is_active = false " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        customJdbc.update(sql, parameters);
    }
}