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
        String sql = "SELECT * FROM legend.posts" +
                "WHERE creator_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", creatorId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getSubscribedUsers(long topicId) {
        String sql = "SELECT u.* " +
                "FROM legend.users AS u JOIN legend.users_posts_subs AS ups ON u.id = ups.user_id" +
                "WHERE ups.topic_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", topicId);

        List<UserEntity> result = customJdbc.query(sql, namedParameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getSubscribedPosts(long userId) {
        String sql = "SELECT p.* " +
                "FROM legend.posts AS p JOIN legend.users_topics AS ut ON p.id = ut.topic_id" +
                "WHERE ut.user_id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", userId);

        List<PostEntity> result = customJdbc.query(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public List<PostEntity> getAll() {
        List<PostEntity> result = customJdbc.query("SELECT * FROM legend.posts", RowMappings::postRowMapping);

        return result;
    }

    @Override
    public PostEntity getById(long id) {
        String sql = "SELECT * FROM legend.posts " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        PostEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::postRowMapping);

        return result;
    }

    @Override
    public long add(PostEntity post) throws SQLException {
        String sql = "INSERT INTO legend.posts(name, description, is_active, open_comment_id, creator_id) " +
                "VALUES (:name, :isActive, :openCommentId, :creatorId)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", post.getName())
                .addValue("isActive", post.getIsActive())
                .addValue("openCommentId", post.getOpeningCommentId())
                .addValue("creatorId", post.getCreatorId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder, new String[] { "id" });

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        return key.longValue();
    }

    @Override
    public void update(PostEntity post) {
        String sql = "UPDATE legend.posts " +
                "SET name=:name, is_active=:isActive, date_modified=now() " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", post.getId())
            .addValue("name", post.getName())
            .addValue("isActive", post.getIsActive());

        customJdbc.update(sql, namedParameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.posts" +
                "SET is_active = false " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        customJdbc.update(sql, namedParameters);
    }
}
