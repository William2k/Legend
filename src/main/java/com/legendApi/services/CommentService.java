package com.legendApi.services;

import com.legendApi.core.enums.SortType;
import com.legendApi.core.exceptions.CustomHttpException;
import com.legendApi.dto.CommentResponseDTO;
import com.legendApi.models.AddComment;
import com.legendApi.models.entities.CommentEntity;
import com.legendApi.repositories.CommentRepository;
import com.legendApi.security.helpers.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    private long currentUserId() {
        return Optional.ofNullable(CurrentUser.getId()).orElse((long) 0);
    }

    public CommentResponseDTO getById(long id, int maxLevel, boolean asc) {
        long currentUserId = currentUserId();

        CommentEntity comment = commentRepository.getById(id, currentUserId);

        return entityToDTO(comment, 0, maxLevel, asc, currentUserId);
    }

    public List<CommentResponseDTO> getAll(long post, int limit, LocalDateTime lastDateCreated, long lastLikes, boolean initial, int maxLevel, SortType sortType, boolean asc) {
        long currentUserId = currentUserId();

        List<CommentEntity> comments = commentRepository.getAll(post, limit, lastDateCreated, lastLikes, initial, sortType, asc, currentUserId);

        return comments.parallelStream().map(commentEntity -> entityToDTO(commentEntity, 0, maxLevel, asc, currentUserId)).collect(Collectors.toList());
    }

    private CommentResponseDTO entityToDTO(CommentEntity comment, int parentLevel, int maxLevel, boolean ascComments, long currentUserId) {
        CommentResponseDTO result = new CommentResponseDTO(comment);

        if(parentLevel < maxLevel) {
            if(parentLevel == 1) {
                ascComments = !ascComments;
            }

            getChildComments(result, parentLevel, maxLevel, ascComments, currentUserId);
        } else {
            childCommentsExistCheck(result);
        }

        return result;
    }

    private void getChildComments(CommentResponseDTO comment, int parentLevel, int maxLevel, boolean asc, long currentUserId) {
        List<CommentEntity> commentEntities = commentRepository.getChildComments(comment.getId(), asc, currentUserId);
        List<CommentResponseDTO> commentsList = commentEntities.parallelStream().map(commentEntity -> entityToDTO(commentEntity, parentLevel + 1, maxLevel, asc, currentUserId)).collect(Collectors.toList());

        comment.setComments(commentsList);
    }

    private void childCommentsExistCheck(CommentResponseDTO comment) {
        boolean childCommentsExist = commentRepository.childCommentsExist(comment.getId());

        if(!childCommentsExist) {
            comment.setComments(new ArrayList<>());
        }
    }

    public long addLike(long commentId, boolean liked) {
        try {
            return commentRepository.addLike(currentUserId(), commentId, liked);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with liking comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public long removeLike(long commentId) {
        try {
            return commentRepository.removeLike(currentUserId(), commentId);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with unliking comment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public long addComment(AddComment model) {
        CommentEntity commentEntity = new CommentEntity();

        if(model.getContent().isEmpty()) {
            throw new CustomHttpException("Content cannot be null", HttpStatus.BAD_REQUEST);
        }

        commentEntity.setPostId(model.getPostId());
        commentEntity.setContent(model.getContent());
        commentEntity.setCreatorUsername(CurrentUser.getUsername());
        commentEntity.setParentCommentId(model.getParentCommentId());
        commentEntity.setIsActive(true);

        try {
            return commentRepository.add(commentEntity);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with adding the group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}