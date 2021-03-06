package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.PostRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
                "FROM legend.posts AS p " +
                "WHERE p.creator_id = :id " +
                "AND p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", creatorId)
                .addValue("userId", creatorId);

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
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
                "FROM legend.posts AS p JOIN legend.users_topics AS ut ON p.id = ut.topic_id " +
                "WHERE ut.user_id = :id " +
                "AND p.is_active = true " +
                "AND ut,is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("userId", userId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public Map<Long,String> getSimpleSubscribedPosts(long userId) {
        String sql = "SELECT p.id AS postId, " +
                "(SELECT g.name FROM legend.groups AS g WHERE g.id = p.group_id AND g.is_active = true) AS groupName " +
                "FROM legend.posts AS p JOIN legend.users_posts_subs AS ups ON p.id = ups.post_id " +
                "WHERE ups.user_id = :id " +
                "AND p.is_active = true " +
                "AND ups.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", userId);

        Map<Long, String> result = customJdbc.query(sql, namedParameters,  (ResultSet rs) -> {
            Map<Long, String> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getLong("postId"), rs.getString("groupName"));
            }
            return results;
        });

        return result;
    }

    @Override
    public long subscribe(long userId, long postId, String groupName) {
        String sql = "INSERT INTO legend.users_posts_subs(user_id, post_id, is_active, group_id) " +
                "SELECT :userId, :postId, :isActive, g.id FROM legend.groups AS g WHERE UPPER(name) = :groupName";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("postId", postId)
                .addValue("isActive", true)
                .addValue("groupName", groupName.toUpperCase());

        customJdbc.update(sql, namedParameters);

        sql =  "UPDATE legend.posts " +
                "SET subscriber_count = subscriber_count + 1 " +
                "WHERE id = :postId " +
                "RETURNING subscriber_count";

        long newSubs = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newSubs;
    }

    @Override
    public long unsubscribe(long userId, long postId, String groupName) {
        String sql = "DELETE FROM legend.users_posts_subs " +
                "WHERE user_id = :userId AND post_id = :postId AND group_id = " +
                "(SELECT g.id FROM legend.groups AS g WHERE UPPER(g.name) = :groupName)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("postId", postId)
                .addValue("groupName", groupName.toUpperCase());

        customJdbc.update(sql, namedParameters);

        sql = "UPDATE legend.posts " +
                "SET subscriber_count = subscriber_count - 1 " +
                "WHERE id = :postId " +
                "RETURNING subscriber_count";

        long newSubs = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newSubs;
    }

    @Override
    public long addLike(long userId, long postId, boolean liked) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("postId", postId)
                .addValue("liked", liked)
                .addValue("isActive", true);

        removeLike(userId, postId);

        String sql = "INSERT INTO legend.users_likes(user_id, post_id, liked, is_active) " +
                "VALUES (:userId, :postId, :liked, :isActive)";

        customJdbc.update(sql, namedParameters);

        sql =  "UPDATE legend.posts " +
                (liked ? "SET likes = likes + 1 " : "SET likes = likes - 1 ") +
                "WHERE id = :postId " +
                "RETURNING likes";

        long newLikes = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newLikes;
    }

    @Override
    public long removeLike(long userId, long postId) {
        String sql = "DELETE FROM legend.users_likes " +
                "WHERE user_id = :userId " +
                "AND post_id = :postId " +
                "RETURNING liked";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("postId", postId)
                .addValue("isActive", true);

        boolean liked;

        try{
            liked = customJdbc.queryForObject(sql, namedParameters, boolean.class);
        } catch (EmptyResultDataAccessException ex) {
            sql = "SELECT likes FROM legend.posts WHERE id = :postId";

            return customJdbc.queryForObject(sql, namedParameters, long.class);
        }

        sql = "UPDATE legend.posts " +
                (liked ? "SET likes = likes - 1 " : "SET likes = likes + 1 ") +
                "WHERE id = :postId " +
                "RETURNING likes";

        long newLikes = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newLikes;
    }

    @Override
    public List<PostEntity> searchPosts(String term, long userId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
                "FROM legend.posts AS p " +
                "WHERE p.is_active = true " +
                "AND LOWER(p.name) LIKE CONCAT('%', :term, '%')";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("term", term.toLowerCase());

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getAll(long userId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
                "FROM legend.posts AS p " +
                "WHERE p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getAll(String group, int limit, long lastCount, boolean initial, boolean asc, long userId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
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
                .addValue("limit", limit)
                .addValue("userId", userId);

        List<PostEntity> result =  customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public PostEntity getById(long id, long userId) {
        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM legend.comments AS c " +
                "WHERE c.post_id = p.id " +
                "AND c.date_created BETWEEN NOW() - INTERVAL '24 HOURS' AND NOW() " +
                "AND c.is_active = true) " +
                "AS comments_today, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND post_id = id) " +
                "FROM legend.posts AS p " +
                "WHERE p.id = :id " +
                "AND p.is_active = true";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId);

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
