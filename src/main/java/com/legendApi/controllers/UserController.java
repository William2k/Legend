package com.legendApi.controllers;

import com.legendApi.models.User;
import com.legendApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("active")
    public Boolean isActive() {
        return true;
    }

    @RequestMapping("")
    public List<User> getUser() {
        return userService.getUsers();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable(value="id") long id) {
        return userService.getUserById(id);
    }
}
