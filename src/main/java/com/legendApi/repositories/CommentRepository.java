package com.legendApi.repositories;

import com.legendApi.models.entities.CommentEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends CRUDRepository<CommentEntity> {
    List<CommentEntity> getChildComments(long id, boolean asc);
    boolean childCommentsExist(long id);
    List<CommentEntity> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, boolean asc);
}
