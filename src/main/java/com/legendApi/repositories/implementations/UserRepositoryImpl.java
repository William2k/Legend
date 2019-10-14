package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.UserRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    @Override
    public UserEntity getByUsername(String username) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE normalised_username = :username";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username.toUpperCase());

        UserEntity result = customJdbc.queryForObject(sql, parameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT EXISTS (SELECT id FROM legend.users " +
                "WHERE normalised_username = :username)";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username.toUpperCase());

        boolean result = customJdbc.queryForObject(sql, parameters, boolean.class); // ignore error postgresql will always true or false for this

        return result;
    }

    @Override
    public List<UserEntity> getAll() {
        List<UserEntity> result = customJdbc.query("SELECT * FROM legend.users", RowMappings::userRowMapping);

        return result;
    }

    @Override
    public UserEntity getById(long id) {
        String sql = "SELECT * FROM legend.users " +
                "WHERE id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        UserEntity result = customJdbc.queryForObject(sql, parameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public long add(UserEntity user) {
        String sql = "INSERT INTO legend.users(username, normalised_username, first_name, last_name, email_address, password, roles, is_active) " +
                "VALUES (:username, :normalisedUsername, :firstName, :lastName, :emailAddress, :password, :roles, :isActive)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("normalisedUsername", user.getUsername().toUpperCase());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getEmailAddress());
        parameters.put("password", user.getPassword());
        parameters.put("roles", user.getStringRoles());
        parameters.put("isActive", user.getIsActive());

        return customJdbc.update(sql, parameters);
    }

    @Override
    public void update(UserEntity user) {
        String sql = "UPDATE legend.users " +
                "SET first_name=:firstName, last_name=:lastName, email_address=:emailAddress, password=:password, roles=:roles, is_active=:isActive, date_modified=now() " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", user.getId());
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailAddress", user.getEmailAddress());
        parameters.put("password", user.getPassword());
        parameters.put("roles", user.getStringRoles());
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