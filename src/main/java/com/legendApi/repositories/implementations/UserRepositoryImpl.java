package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.User;
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

    private User userRowMapping(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setPassword(rs.getString("password"));
        user.setIsActive(rs.getBoolean("is_active"));
        return user;
    }

    @Override
    public User getByUsername(String username) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE normalised_username = :username";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username.toUpperCase());

        User result = customJdbc.queryForObject(sql, parameters, this::userRowMapping);

        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> result = customJdbc.query("SELECT * FROM legend.users", this::userRowMapping);

        return result;
    }

    @Override
    public User getById(long id) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        User result = customJdbc.queryForObject(sql, parameters, this::userRowMapping);

        return result;
    }

    @Override
    public long add(User user) {
        String sql = "INSERT INTO legend.users(id, username, normalised_username, first_name, last_name, email_address, password, is_active)" +
                "VALUES (:username, :normalisedUsername, :firstName, :lastName, :emailAddress, :password, :isActive)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", user.getId());
        parameters.put("username", user.getUsername());
        parameters.put("normalisedUsername", user.getUsername().toUpperCase());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getLastName());
        parameters.put("password", user.getPassword());
        parameters.put("isActive", user.getIsActive());

        return customJdbc.update(sql, parameters);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE legend.users" +
                "SET first_name=:firstName, last_name=:lastName, email_address=:emailAddress, password=:password, is_active=:isActive " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", user.getId());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getLastName());
        parameters.put("password", user.getPassword());
        parameters.put("isActive", user.getIsActive());

        customJdbc.update(sql, parameters);
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM legend.users " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        customJdbc.update(sql, parameters);
    }
}