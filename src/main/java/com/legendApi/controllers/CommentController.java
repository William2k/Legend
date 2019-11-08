package com.legendApi.controllers;

import com.legendApi.dto.CommentResponseDTO;
import com.legendApi.models.AddComment;
import com.legendApi.models.AddGroup;
import com.legendApi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public CommentResponseDTO getComment(@PathVariable("id") long id) {
        CommentResponseDTO result = commentService.getById(id);

        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CommentResponseDTO> getCommentsForPost(@RequestParam("post") long post, @RequestParam("limit") int limit, @RequestParam("lastDateCreated") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastDateCreated, @RequestParam("initial") boolean initial, @RequestParam("asc") boolean asc) {
        List<CommentResponseDTO> result = commentService.getAll(post, limit, lastDateCreated, initial, asc);

        return result;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public long addComment(@RequestBody AddComment model) {
        long id = commentService.addComment(model);

        return id;
    }
}
