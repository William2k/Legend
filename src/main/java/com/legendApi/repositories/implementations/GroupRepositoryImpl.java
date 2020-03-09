package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.GroupRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import jdk.internal.jline.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public GroupRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT EXISTS (SELECT id FROM legend.groups " +
                "WHERE UPPER(name) = :name)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name.toUpperCase());

        boolean result = customJdbc.queryForObject(sql, namedParameters, boolean.class);

        return result;
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
    public long subscribe(long userId, String groupName) {
        String sql = "INSERT INTO legend.users_groups_subs(user_id, group_id, is_active) " +
                "SELECT :userId, id, :isActive " +
                "FROM legend.groups AS g " +
                "WHERE UPPER(g.name) = :groupName ";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("groupName", groupName.toUpperCase())
                .addValue("isActive", true);

        customJdbc.update(sql, namedParameters);

        sql = "UPDATE legend.groups " +
                "SET subscriber_count = subscriber_count + 1 " +
                "WHERE id = (SELECT id FROM legend.groups WHERE UPPER(name) = :groupName) " +
                "RETURNING subscriber_count";

        long newSubs = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newSubs;
    }

    @Override
    public long subscribe(long userId, long groupId) {
        String sql = "INSERT INTO legend.users_groups_subs(user_id, group_id, is_active) " +
                "VALUES (:userId, :groupId, :isActive)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("groupId", groupId)
            .addValue("isActive", true);

        customJdbc.update(sql, namedParameters);

        sql = "UPDATE legend.groups " +
                "SET subscriber_count = subscriber_count + 1 " +
                "WHERE group_id = :groupId " +
                "RETURNING subscriber_count";

        long newSubs = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newSubs;
    }

    @Override
    public long unsubscribe(long userId, String groupName) {
        String sql = "DELETE FROM legend.users_groups_subs " +
                "WHERE user_id = :userId AND group_id = " +
                "(SELECT g.id FROM legend.groups AS g WHERE UPPER(g.name) = :groupName)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("groupName", groupName.toUpperCase());

        customJdbc.update(sql, namedParameters);

        sql = "UPDATE legend.groups " +
                "SET subscriber_count = subscriber_count - 1 " +
                "WHERE id = (SELECT id FROM legend.groups WHERE UPPER(name) = :groupName) " +
                "RETURNING subscriber_count";

        long newSubs = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newSubs;
    }

    @Override
    public GroupEntity getGroupByName(String name) {
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g " +
                "WHERE g.is_active = true " +
                "AND UPPER(name) = :name " +
                "ORDER BY posts_today, g.id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("name", name.toUpperCase());

        GroupEntity result = customJdbc.queryForObject(sql, namedParameters,  RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> getAllByCreatorId(long creatorId) {
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g" +
                "WHERE g.creator_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", creatorId);

        List<GroupEntity> result = customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getSubscribedUsers(long groupId) {
        String sql = "SELECT u.* " +
                "FROM legend.users AS u JOIN legend.users_groups AS ug ON u.id = ug.user_id " +
                "WHERE ug.group_id = :id " +
                "AND u.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", groupId);

        List<UserEntity> result = customJdbc.query(sql, namedParameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> searchGroups(String term) {
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g " +
                "WHERE g.is_active = true " +
                "AND (LOWER(g.description) LIKE CONCAT('%', :term, '%') OR :term = ANY(g.tags)) " +
                "ORDER BY posts_today, g.id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("term", term.toLowerCase());

        List<GroupEntity> result = customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<GroupEntity> getSubscribedGroups(long userId) {
        String sql = "SELECT g.* " +
                "FROM legend.groups AS g JOIN legend.users_groups AS ug ON g.id = ug.topic_id " +
                "WHERE ug.user_id = :id " +
                "AND g.is_active = true " +
                "AND ug.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", userId);

        List<GroupEntity> result = customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public List<String> getSimpleSubscribedGroups(long userId) {
        String sql = "SELECT g.name " +
                "FROM legend.groups AS g JOIN legend.users_groups_subs AS ugs ON g.id = ugs.group_id " +
                "WHERE ugs.user_id = :id " +
                "AND g.is_active = true " +
                "AND ugs.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", userId);

        List<String> result = customJdbc.queryForList(sql, namedParameters, String.class);

        return result;
    }

    @Override
    public List<GroupEntity> getAll() {
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g " +
                "WHERE g.is_active = true " +
                "ORDER BY posts_today, g.id";

        List<GroupEntity> result = customJdbc.query(sql, RowMappings::groupRowMapping);

        return result;
    }

    public List<GroupEntity> getAll(int limit, long lastCount, boolean initial, boolean asc) {

        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g " +
                "WHERE g.is_active = true ";

        if(!initial) {
            sql += asc ? "AND posts_today > :lastCount " : "AND posts_today < :lastCount ";
        }

        sql += asc ? "ORDER BY posts_today ASC, g.id ASC " : "ORDER BY posts_today DESC, g.id DESC " +
                "LIMIT :limit";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("lastCount", lastCount)
                .addValue("limit", limit);

        List<GroupEntity> result =  customJdbc.query(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public GroupEntity getById(long id) {
        String sql = "SELECT g.*, " +
                "(SELECT COUNT(*) FROM legend.posts AS p " +
                "WHERE p.group_id = g.id " +
                "AND p.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND p.is_active = true) " +
                "AS posts_today " +
                "FROM legend.groups AS g " +
                "WHERE g.is_active = true " +
                "AND id = :id " +
                "ORDER BY posts_today, g.id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        GroupEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::groupRowMapping);

        return result;
    }

    @Override
    public long add(GroupEntity group) throws SQLException {
        String sql = "INSERT INTO legend.groups(name, description, is_active, creator_username, tags) " +
                "VALUES (:name, :description, :isActive, :creatorUsername, :tags)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", group.getName())
                .addValue("description", group.getDescription())
                .addValue("isActive", group.getIsActive())
                .addValue("creatorUsername", group.getCreatorUsername())
                .addValue("tags", group.getTags());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder, new String[] { "id" });

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        return key.longValue();
    }

    @Override
    public void update(GroupEntity group) {
        String sql = "UPDATE legend.groups " +
                "SET name=:name, description=:description, tags=:tags, date_modified=now() " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", group.getId())
                .addValue("name", group.getName())
                .addValue("description", group.getDescription())
                .addValue("tags", group.getTags());

        customJdbc.update(sql, namedParameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.groups " +
                "SET is_active = false " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        customJdbc.update(sql, namedParameters);
    }
}
