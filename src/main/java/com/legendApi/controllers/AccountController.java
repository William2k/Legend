package com.legendApi.controllers;

import com.legendApi.models.Login;
import com.legendApi.models.RegisterUser;
import com.legendApi.models.User;
import com.legendApi.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "signIn", method = RequestMethod.POST)
    public String SignIn(@RequestBody Login model) {
        String token = accountService.signIn(model);

        return token;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "signUp", method = RequestMethod.POST)
    public void SignUp(@RequestBody RegisterUser model) {
        accountService.signUp(model);
    }
}
