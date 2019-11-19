package com.legendApi.repositories;

import com.legendApi.models.entities.CommentEntity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository {
    List<CommentEntity> getChildComments(long id, boolean asc, long userId);
    boolean childCommentsExist(long id);
    List<CommentEntity> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, boolean asc, long userId);
    CommentEntity getById(long id, long userId);
    long removeLike(long userId, long commentId);
    long addLike(long userId, long commentId, boolean liked);
    void update(CommentEntity comment);
    void delete(long id);
    long add(CommentEntity comment) throws SQLException;
}
