package com.legendApi.controllers;

import com.legendApi.models.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    @RequestMapping("active")
    public Boolean isActive() {
        return true;
    }

    @RequestMapping("")
    public User GetUser(@RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName) {
        return new User(1, firstName, lastName);
    }
}
