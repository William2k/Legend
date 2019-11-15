package com.legendApi.services;

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

    public CommentResponseDTO getById(long id, int maxLevel, boolean asc) {
        CommentEntity comment = commentRepository.getById(id);

        return entityToDTO(comment, 0, maxLevel, asc);
    }

    public List<CommentResponseDTO> getAll(long post, int limit, LocalDateTime lastDateCreated, boolean initial, int maxLevel, boolean asc) {
        List<CommentEntity> comments = commentRepository.getAll(post, limit, lastDateCreated, initial, asc);
        List<CommentResponseDTO> result = comments.parallelStream().map(commentEntity -> entityToDTO(commentEntity, 0, maxLevel, asc)).collect(Collectors.toList());

        return result;
    }

    private List<CommentResponseDTO> entityToDtoIterative(List<CommentEntity> commentEntities, int maxLevel, boolean ascComments) {

        List<CommentResponseDTO> commentResponseDTOs = commentEntities.parallelStream().map(CommentResponseDTO::new).collect(Collectors.toList());

        Deque<KeyValuePair> commentDeque = commentResponseDTOs.parallelStream().map(commentResponseDTO -> new KeyValuePair<>(0, commentResponseDTO)).collect(Collectors.toCollection(ArrayDeque::new));

        while (!commentDeque.isEmpty()) {
            KeyValuePair keyValuePair = commentDeque.pop();

            CommentResponseDTO commentResponseDTO = (CommentResponseDTO) keyValuePair.getValue();
            int parentLevel = (int) keyValuePair.getKey();

            if(parentLevel < maxLevel) {
                if(parentLevel == 1) {
                    ascComments = !ascComments;
                }

                List<CommentEntity> currentCommentEntities = commentRepository.getChildComments(commentResponseDTO.getId(), ascComments);

                List<CommentResponseDTO> commentsList = currentCommentEntities.parallelStream().map(CommentResponseDTO::new).collect(Collectors.toList());

                commentResponseDTO.setComments(commentsList);

                parentLevel += 1;

                int finalParentLevel = parentLevel;
                List<KeyValuePair> setCommentsList = commentsList.parallelStream().map(commentResponse -> new KeyValuePair<>(finalParentLevel, commentResponse)).collect(Collectors.toList());

                commentDeque.addAll(setCommentsList);
            } else {
                childCommentsExistCheck(commentResponseDTO);
            }
        }

        return commentResponseDTOs;
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

class KeyValuePair<K, V> {
    private final K key;
    private final V Value;

    KeyValuePair(K key, V value) {
        this.key = key;
        Value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return Value;
    }
}