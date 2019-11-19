package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.CommentEntity;
import com.legendApi.repositories.CommentRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public CommentRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public List<CommentEntity> getChildComments(long id, boolean asc, long userId) {
        String sql = "SELECT *, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND comment_id = id) " +
                "FROM legend.comments " +
                "WHERE parent_comment_id=:id " +
                "AND is_active = true ";

        sql += "ORDER BY date_created " + (asc ? "ASC " : "DESC ");

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId);

        List<CommentEntity> result = customJdbc.query(sql, namedParameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public boolean childCommentsExist(long id) {
        String sql = "SELECT EXISTS( " +
                "SELECT * FROM legend.comments " +
                "WHERE parent_comment_id=:id " +
                "AND is_active = true " +
                "LIMIT 1)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);

        boolean result = customJdbc.queryForObject(sql, namedParameters, boolean.class);

        return result;
    }

    @Override
    public List<CommentEntity> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, boolean asc, long userId) {
        String sql = "SELECT *, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND comment_id = id) " +
                "FROM legend.comments " +
                "WHERE is_active = true " +
                "AND post_id = :post " +
                "AND parent_comment_id = 0 ";

        if(!initial) {
            sql += asc ? "AND date_created > :lastDateCreated " : "AND date_created < :lastDateCreated ";
        }

        sql += "ORDER BY date_created " + (asc ? "ASC " : "DESC ") +
                "LIMIT :limit";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("post", post)
                .addValue("lastDateCreated", lastDateCreated)
                .addValue("limit", limit)
                .addValue("userId", userId);

        List<CommentEntity> result = customJdbc.query(sql, namedParameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public long addLike(long userId, long commentId, boolean liked) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("commentId", commentId)
                .addValue("liked", liked)
                .addValue("isActive", true);

        removeLike(userId, commentId);

        String sql = "INSERT INTO legend.users_likes(user_id, comment_id, liked, is_active) " +
                "VALUES (:userId, :commentId, :liked, :isActive)";

        customJdbc.update(sql, namedParameters);

        sql =   "UPDATE legend.comments " +
                (liked ? "SET likes = likes + 1 " : "SET likes = likes - 1 ") +
                "WHERE id = :commentId " +
                "RETURNING likes";

        long newLikes = customJdbc.queryForObject(sql, namedParameters, long.class);

        return newLikes;
    }

    @Override
    public long removeLike(long userId, long commentId) {
        String sql = "DELETE FROM legend.users_likes " +
                "WHERE user_id = :userId " +
                "AND comment_id = :commentId " +
                "RETURNING liked";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("commentId", commentId)
                .addValue("isActive", true);

        boolean liked;

        try{
            liked = customJdbc.queryForObject(sql, namedParameters, boolean.class);
        } catch (EmptyResultDataAccessException ex) {
            sql = "SELECT likes FROM legend.comments WHERE id = :commentId";

            return customJdbc.queryForObject(sql, namedParameters, long.class);
        }

        sql =  "UPDATE legend.comments " +
                (liked ? "SET likes = likes - 1 " : "SET likes = likes + 1 ") +
                "WHERE id = :commentId " +
                "RETURNING likes";

        return customJdbc.queryForObject(sql, namedParameters, long.class);
    }

    @Override
    public CommentEntity getById(long id, long userId) {
        String sql = "SELECT *, (SELECT liked FROM legend.users_likes WHERE user_id = :userId AND comment_id = id) " +
                "FROM legend.comments " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId);

        CommentEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public long add(CommentEntity comment) throws SQLException {
        String sql = "INSERT INTO legend.comments(content, is_active, post_id, parent_comment_id, creator_username) " +
                "VALUES (:content, :isActive, :postId, :parentCommentId, :creatorUsername);";


        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("content", comment.getContent())
            .addValue("isActive", comment.getIsActive())
            .addValue("postId", comment.getPostId())
            .addValue("parentCommentId", comment.getParentCommentId())
            .addValue("creatorUsername", comment.getCreatorUsername());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        customJdbc.update(sql, namedParameters, keyHolder, new String[] { "id" });

        Number key = keyHolder.getKey();

        if(key == null) {
            throw new SQLException("Something went wrong while adding the entity");
        }

        String updateSql = "UPDATE legend.posts " +
                "SET comment_count = comment_count + 1 " +
                "WHERE id = :postId;";

        customJdbc.update(updateSql, namedParameters);

        return key.longValue();
    }

    @Override
    public void update(CommentEntity comment) {
        String sql = "UPDATE legend.comments " +
                "SET content=:content, date_modified=now() " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", comment.getId())
            .addValue("content", comment.getContent());

        customJdbc.update(sql, namedParameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.comments " +
                "SET is_active = false " +
                "WHERE id = :id;" +
                "UPDATE legend.posts " +
                "SET comment_count = comment_count - 1 " +
                "WHERE id = (SELECT post_id FROM comments WHERE id = :id)";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("id", id);

        customJdbc.update(sql, namedParameters);
    }
}
