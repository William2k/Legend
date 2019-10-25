package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.CommentEntity;
import com.legendApi.repositories.CommentRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public CommentRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public List<CommentEntity> getChildComments(long id) {
        String sql = "SELECT * FROM legend.comments " +
                "WHERE parent_comment_id=:id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        List<CommentEntity> result = customJdbc.query(sql, namedParameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public List<CommentEntity> getAll() {
        List<CommentEntity> result = customJdbc.query("SELECT * FROM legend.comments", RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public CommentEntity getById(long id) {
        String sql = "SELECT * FROM legend.comments " +
                "WHERE id = :id";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", id);

        CommentEntity result = customJdbc.queryForObject(sql, namedParameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public long add(CommentEntity comment) throws SQLException {
        String sql = "INSERT INTO legend.comments(content, is_active, post_id, parent_comment_id, creator_id) " +
                "VALUES (:content, :isActive, :postId, :parentCommentId, :creatorId);";


        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("content", comment.getContent())
            .addValue("isActive", comment.getIsActive())
            .addValue("postId", comment.getPostId())
            .addValue("parentCommentId", comment.getParentCommentId())
            .addValue("creatorId", comment.getCreatorId());

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
