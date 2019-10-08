package com.legendApi.controllers;

import com.legendApi.dto.UserResponseDTO;
import com.legendApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDTO> getUser() {
        // Should return current user
        return userService.getUsers();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserResponseDTO getUser(@PathVariable(value="id") long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<UserResponseDTO> getAllUsers() {
        return userService.getUsers();
    }
}
