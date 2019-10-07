package com.legendApi.controllers;

import com.legendApi.models.Login;
import com.legendApi.models.RegisterUser;
import com.legendApi.models.User;
import com.legendApi.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String Login(@RequestBody Login model, HttpServletResponse response) {
        String token = accountService.login(model);

        if(token == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            response.addCookie(new Cookie("Bearer ", token));

        }


        return token;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void PostUser(@RequestBody RegisterUser model) {
        accountService.addUser(model);
    }
}
