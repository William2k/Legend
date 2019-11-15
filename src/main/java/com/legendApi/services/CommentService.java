package com.legendApi.services;

import com.legendApi.core.exceptions.CustomHttpException;
import com.legendApi.dto.CommentResponseDTO;
import com.legendApi.models.AddComment;
import com.legendApi.models.entities.CommentEntity;
import com.legendApi.repositories.CommentRepository;
import com.legendApi.repositories.UserRepository;
import com.legendApi.security.helpers.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDTO getById(long id, int maxLevel, boolean asc) {
        CommentEntity comment = commentRepository.getById(id);

        return entityToDTO(comment, 0, maxLevel, asc);
    }

    public List<CommentResponseDTO> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, int maxLevel, boolean asc) {
        List<CommentEntity> comments = commentRepository.getAll(post, limit, lastDateCreated, initial, asc);
        List<CommentResponseDTO> result = comments.parallelStream().map(commentEntity -> entityToDTO(commentEntity, 0, maxLevel, asc)).collect(Collectors.toList());

        return result;
    }

    private CommentResponseDTO entityToDTO(CommentEntity comment, int parentLevel, int maxLevel, boolean ascComments) {
        CommentResponseDTO result = new CommentResponseDTO(comment);

        if(parentLevel < maxLevel) {
            if(parentLevel == 1) {
                ascComments = !ascComments;
            }

            getChildComments(result, parentLevel, maxLevel, ascComments);
        } else {
            childCommentsExistCheck(result);
        }

        return result;
    }

    private void getChildComments(CommentResponseDTO comment, int parentLevel, int maxLevel, boolean asc) {
        List<CommentEntity> commentEntities = commentRepository.getChildComments(comment.getId(), asc);
        List<CommentResponseDTO> commentsList = commentEntities.parallelStream().map(commentEntity -> entityToDTO(commentEntity, parentLevel + 1, maxLevel, asc)).collect(Collectors.toList());

        comment.setComments(commentsList);
    }

    private void childCommentsExistCheck(CommentResponseDTO comment) {
        boolean childCommentsExist = commentRepository.childCommentsExist(comment.getId());

        if(!childCommentsExist) {
            comment.setComments(new ArrayList<>());
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
