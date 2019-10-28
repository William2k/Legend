package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.PostRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public PostRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public List<PostEntity> getAllByCreatorId(long creatorId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today " +
                "FROM legend.posts AS p " +
                "WHERE p.creator_id = :id " +
                "AND p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", creatorId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getSubscribedUsers(long topicId) {
        String sql = "SELECT u.* " +
                "FROM legend.users AS u JOIN legend.users_posts_subs AS ups ON u.id = ups.user_id " +
                "WHERE ups.topic_id = :id " +
                "AND ups.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", topicId);

        List<UserEntity> result = customJdbc.query(sql, namedParameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getSubscribedPosts(long userId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today " +
                "FROM legend.posts AS p JOIN legend.users_topics AS ut ON p.id = ut.topic_id " +
                "WHERE ut.user_id = :id " +
                "AND p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", userId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getAll() {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today " +
                "FROM legend.posts AS p " +
                "WHERE p.is_active = true";

        List<PostEntity> result = customJdbc.query(sql, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getAll(String group, int limit, long lastCount, boolean initial, boolean asc) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today " +
                "FROM legend.posts AS p " +
                "WHERE p.is_active = true " +
                "AND p.group_id = (SELECT g.id FROM legend.groups AS g WHERE UPPER(g.name) = :group) ";

        if(!initial) {
            sql += asc ? "AND comments_today > :lastCount " : "AND comments_today < :lastCount ";
        }

        sql += asc ? "ORDER BY comments_today ASC, p.id ASC " : "ORDER BY comments_today DESC, p.id DESC " +
                "LIMIT :limit";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("lastCount", lastCount)
                .addValue("group", group.toUpperCase())
                .addValue("limit", limit);

        List<PostEntity> result =  customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public PostEntity getById(long id) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today " +
                "FROM legend.posts AS p " +
                "WHERE p.id = :id " +
                "AND p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        PostEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public long add(PostEntity post) throws SQLException {
        String sql = "INSERT INTO legend.posts(name, content, is_active, group_id, creator_username) " +
                "VALUES (:name, :content, :isActive, :groupId, :creatorUsername); ";


        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", post.getName())
                .addValue("isActive", post.getIsActive())
                .addValue("content", post.getContent())
                .addValue("groupId", post.getGroupId())
                .addValue("creatorUsername", post.getCreatorUsername());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder, new String[] { "id" });

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        String updateSql = "UPDATE legend.groups " +
                "SET post_count = post_count + 1 " +
                "WHERE id = :groupId;";

        customJdbc.update(updateSql, namedParameters);

        return key.longValue();
    }

    @Override
    public void update(PostEntity post) {
        String sql = "UPDATE legend.posts " +
                "SET name=:name, date_modified=now() " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", post.getId())
            .addValue("name", post.getName());

        customJdbc.update(sql, namedParameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.posts " +
                "SET is_active = false " +
                "WHERE id = :id;"  +
                "UPDATE legend.groups " +
                "SET post_count = post_count - 1 " +
                "WHERE id = (SELECT group_id FROM legend.posts WHERE id = :id)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        customJdbc.update(sql, namedParameters);
    }
}
