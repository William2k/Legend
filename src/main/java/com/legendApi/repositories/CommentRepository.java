package com.legendApi.repositories;

import com.legendApi.models.entities.CommentEntity;

import java.util.List;

public interface CommentRepository extends CRUDRepository<CommentEntity> {
    List<CommentEntity> getChildComments(long id);
}
