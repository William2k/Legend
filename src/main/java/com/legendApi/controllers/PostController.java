package com.legendApi.controllers;

import com.legendApi.dto.PostResponseDTO;
import com.legendApi.models.AddPost;
import com.legendApi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PostResponseDTO> getPosts(@RequestParam("group") String group, @RequestParam("limit") int limit, @RequestParam("lastCount") long lastCount, @RequestParam("initial") boolean initial, @RequestParam("asc") boolean asc) {
        List<PostResponseDTO> posts = postService.getAll(group, limit, lastCount, initial, asc);

        return posts;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addPost(@RequestBody AddPost model) {
        postService.addPost(model);
    }
}
