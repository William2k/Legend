package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.CommentEntity;
import com.legendApi.repositories.CommentRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public CommentRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public List<CommentEntity> getChildComments(long id) {
        String sql = "SELECT * FROM legend.comments" +
                "WHERE parent_comment_id=:id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        List<CommentEntity> result = customJdbc.query(sql, parameters, RowMappings::commentRowMapping);

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

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        CommentEntity result = customJdbc.queryForObject(sql, parameters, RowMappings::commentRowMapping);

        return result;
    }

    @Override
    public long add(CommentEntity comment) {
        String sql = "INSERT INTO legend.comments(content, is_active, topic_id, parent_comment_id, creator_id) " +
                "VALUES (:content, :isActive, :topicId, :parentCommentId, :creatorId)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("content", comment.getContent());
        parameters.put("isActive", comment.getIsActive());
        parameters.put("topicId", comment.getTopicId());
        parameters.put("parentCommentId", comment.getParentCommentId());
        parameters.put("creatorId", comment.getCreatorId());

        return customJdbc.update(sql, parameters);
    }

    @Override
    public void update(CommentEntity comment) {
        String sql = "UPDATE legend.comments " +
                "SET content=:content, is_active=:isActive, date_modified=now() " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", comment.getId());
        parameters.put("content", comment.getContent());
        parameters.put("isActive", comment.getIsActive());

        customJdbc.update(sql, parameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.comments" +
                "SET is_active = false " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        customJdbc.update(sql, parameters);
    }
}
