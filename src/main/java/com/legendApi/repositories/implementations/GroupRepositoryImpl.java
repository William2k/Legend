package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.GroupRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public GroupRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public long getPostCount(long groupId) {
        String sql = "SELECT COUNT(p.*) " +
                "FROM legend.groups AS g JOIN legend.posts AS p ON g.id = p.group_id " +
                "WHERE g.id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", groupId);

        long result = customJdbc.queryForObject(sql, namedParameters, long.class);

        return result;
    }

    @Override
    public long getPostCount(String groupName) {
        String sql = "SELECT COUNT(p.*) " +
                "FROM legend.groups AS g JOIN legend.posts AS p ON g.id = p.group_id " +
                "WHERE UPPER(g.name) = :name";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", groupName.toUpperCase());

        long result = customJdbc.queryForObject(sql, namedParameters, long.class);

        return result;
    }

    @Override
    public long subscribe(long userId, long groupId) throws SQLException {
        String sql = "INSERT INTO legend.groups(user_id, group_id, is_active)" +
                "VALUES (:userId, :groupId, :isActive)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("groupId", groupId)
            .addValue("isActive", true);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder);

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        return key.longValue();
    }

    @Override
    public GroupEntity getGroupByName(String name) {
        String sql = "SELECT * FROM legend.groups" +
                "WHERE name = :name";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("name", name);

        GroupEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> getAllByCreatorId(long creatorId) {
        String sql = "SELECT * FROM legend.groups" +
                "WHERE creator_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", creatorId);

        List<GroupEntity> result = customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getSubscribedUsers(long groupId) {
        String sql = "SELECT u.* " +
                "FROM legend.users AS u JOIN legend.users_groups AS ug ON u.id = ug.user_id" +
                "WHERE ug.group_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", groupId);

        List<UserEntity> result = customJdbc.query(sql, namedParameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> getSubscribedGroups(long userId) {
        String sql = "SELECT g.* " +
                "FROM legend.groups AS g JOIN legend.users_groups AS ug ON g.id = ug.topic_id" +
                "WHERE ug.user_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", userId);

        List<GroupEntity> result = customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> getAll() {
        List<GroupEntity> result = customJdbc.query("SELECT * FROM legend.groups", RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public GroupEntity getById(long id) {
        String sql = "SELECT * FROM legend.groups " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        GroupEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public long add(GroupEntity group) throws SQLException {
        String sql = "INSERT INTO legend.groups(name, is_active, creator_id) " +
                "VALUES (:name, :isActive, :creatorId)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", group.getName())
                .addValue("isActive", group.getIsActive())
                .addValue("creatorId", group.getCreatorId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder);

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        return key.longValue();
    }

    @Override
    public void update(GroupEntity group) {
        String sql = "UPDATE legend.groups " +
                "SET name=:name, is_active=:isActive, date_modified=now() " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", group.getId())
            .addValue("name", group.getName())
            .addValue("isActive", group.getIsActive());

        customJdbc.update(sql, namedParameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.groups" +
                "SET is_active = false " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        customJdbc.update(sql, namedParameters);
    }
}