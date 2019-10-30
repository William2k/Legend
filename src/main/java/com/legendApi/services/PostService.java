package com.legendApi.services;

import com.legendApi.core.exceptions.CustomHttpException;
import com.legendApi.dto.PostResponseDTO;
import com.legendApi.models.AddPost;
import com.legendApi.models.entities.PostEntity;
import com.legendApi.repositories.GroupRepository;
import com.legendApi.repositories.PostRepository;
import com.legendApi.security.helpers.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public PostService(PostRepository postRepository, GroupRepository groupRepository) {
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
    }

    public PostResponseDTO getPostById(long id) {
        PostResponseDTO result = new PostResponseDTO(postRepository.getById(id));

        return result;
    }

    public List<PostResponseDTO> getAllPosts(String group, int limit, long lastCount, boolean initial, boolean asc) {
        List<PostEntity> posts = postRepository.getAll(group, limit, lastCount, initial, asc);

        List<PostResponseDTO> postDtos = posts
                .stream().map(PostResponseDTO::new)
                .collect(Collectors.toList());

        return postDtos;
    }

    public void subscribeToPost(long postId, String group) {
        try {
            postRepository.subscribe(CurrentUser.getId(), postId, group);
        } catch (SQLException e) {
            throw new CustomHttpException("Something went wrong subscribing to post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Map<Long, String> getSimpleSubscribedPost() {
        return postRepository.getSimpleSubscribedPosts(CurrentUser.getId());
    }

    public void addPost(AddPost model) {
        PostEntity post = new PostEntity();

        if(model.getName().isEmpty() || model.getContent().isEmpty()) {
            throw new CustomHttpException("Name and Content cannot be null", HttpStatus.BAD_REQUEST);
        }

        post.setName(model.getName());
        post.setContent(model.getContent());
        post.setGroupId(groupRepository.getGroupByName(model.getGroup()).getId());
        post.setCreatorUsername(CurrentUser.getUsername());
        post.setIsActive(true);

        try {
            postRepository.add(post);
        } catch (SQLException e) {
            throw new CustomHttpException("Something went wrong with adding the post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
