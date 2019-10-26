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

    public List<CommentResponseDTO> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, boolean asc) {
        List<CommentEntity> comments = commentRepository.getAll(post, limit, lastDateCreated, initial, asc);

        List<CommentResponseDTO> result = comments.stream().map(commentEntity -> {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO(commentEntity);
            commentResponseDTO.setCreator(userRepository.getById(commentEntity.getCreatorId()).getUsername());

            return commentResponseDTO;
        }).collect(Collectors.toList());

        return result;
    }

    public void addComment(AddComment model) {
        CommentEntity commentEntity = new CommentEntity();

        if(model.getContent().isEmpty()) {
            throw new CustomHttpException("Content cannot be null", HttpStatus.BAD_REQUEST);
        }

        commentEntity.setPostId(model.getPostId());
        commentEntity.setContent(model.getContent());
        commentEntity.setCreatorId(CurrentUser.getId());
        commentEntity.setParentCommentId(model.getParentCommentId());
        commentEntity.setIsActive(true);

        try {
            commentRepository.add(commentEntity);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with adding the group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}