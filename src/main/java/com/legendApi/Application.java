package com.legendApi;

import com.legendApi.config.ConfigProperties;
import com.legendApi.models.Role;
import com.legendApi.models.User;
import com.legendApi.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final AccountService accountService;
    private final ConfigProperties configProperties;

    @Autowired
    public Application(AccountService accountService, ConfigProperties configProperties) {
        this.accountService = accountService;
        this.configProperties = configProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... params) {
        initialise();
    }

    private void initialise() {
        String username = configProperties.getConfigValue("account.admin.username");
        String password = configProperties.getConfigValue("account.admin.password");
        String emailAddress =configProperties.getConfigValue("account.admin.emailAddress");

        if(username == null || password == null || emailAddress == null || accountService.existsByUsername(username))
        {
            return;
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setFirstName(username);
        admin.setLastName(username);
        admin.setPassword(password);
        admin.setEmailAddress(emailAddress);
        admin.setRoles(new ArrayList<>(Collections.singletonList(Role.ROLE_ADMIN)));

        accountService.signUp(admin);
    }
}