package com.legendApi.controllers;

import com.legendApi.dto.PostResponseDTO;
import com.legendApi.models.AddPost;
import com.legendApi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PostResponseDTO getPost(@PathVariable("id") long id) {
        PostResponseDTO post = postService.getPostById(id);

        return post;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PostResponseDTO> getPosts(@RequestParam("group") String group, @RequestParam("limit") int limit, @RequestParam("lastCount") long lastCount, @RequestParam("initial") boolean initial, @RequestParam("asc") boolean asc) {
        List<PostResponseDTO> posts = postService.getAllPosts(group, limit, lastCount, initial, asc);

        return posts;
    }

    @RequestMapping(value = "subscribed", method = RequestMethod.GET)
    public Map<Long, String> getSimpleSubscribedPosts() {
        return postService.getSimpleSubscribedPost();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "{id}/subscribe", method = RequestMethod.POST)
    public void subscribeToPost(@PathVariable("id") long id, @RequestParam("group") String group) {
        postService.subscribeToPost(id, group);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addPost(@RequestBody AddPost model) {
        postService.addPost(model);
    }
}
