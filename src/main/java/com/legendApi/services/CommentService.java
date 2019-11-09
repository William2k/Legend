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
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDTO getById(long id, int maxLevel) {
        CommentEntity comment = commentRepository.getById(id);

        return entityToDTO(comment, 0, maxLevel);
    }

    public List<CommentResponseDTO> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, int maxLevel, boolean asc) {
        List<CommentEntity> comments = commentRepository.getAll(post, limit, lastDateCreated, initial, asc);

        List<CommentResponseDTO> result = entityToDTO(comments, 0, maxLevel);

        return result;
    }

    private CommentResponseDTO entityToDTO(CommentEntity comment, int parentLevel, int maxLevel) {
        CommentResponseDTO result = new CommentResponseDTO(comment);

        getChildComments(result, parentLevel, maxLevel);

        return result;
    }

    private List<CommentResponseDTO> entityToDTO(List<CommentEntity> comments, int parentLevel, int maxLevel) {
        List<CommentResponseDTO> result = comments.stream().map(CommentResponseDTO::new).collect(Collectors.toList());

        getChildComments(result, parentLevel, maxLevel);

        return result;
    }

    private void getChildComments(CommentResponseDTO comment, int parentLevel, int maxLevel) {
            List<CommentEntity> commentEntities = commentRepository.getChildComments(comment.getId());
            List<CommentResponseDTO> commentsList = entityToDTO(commentEntities, parentLevel, maxLevel);

            getChildComments(commentsList, parentLevel, maxLevel);
            comment.setComments(commentsList);
    }

    private void getChildComments(List<CommentResponseDTO> comments, int parentLevel, int maxLevel) {
        comments.forEach(comment -> {
            comment.setLevel(parentLevel + 1);

            if(comment.getLevel() < maxLevel) {
                getChildComments(comment, comment.getLevel(), maxLevel);
            } else {
                childCommentsExistCheck(comment);
            }
        });
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
