package com.legendApi.services;

import com.legendApi.config.ConfigProperties;
import com.legendApi.core.exceptions.CustomException;
import com.legendApi.models.Login;
import com.legendApi.models.RegisterUser;
import com.legendApi.models.Role;
import com.legendApi.models.User;
import com.legendApi.repositories.UserRepository;
import com.legendApi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final ConfigProperties configProperties;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AccountService(UserRepository userRepository, ConfigProperties configProperties, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.configProperties = configProperties;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public String signIn(Login login) {
        return signIn(login.getUsername(), login.getPassword());
    }

    public String signIn(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(authenticationToken);

            User user = new User(userRepository.getByUsername(username));

            return jwtTokenProvider.createToken(username, user.getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUp(RegisterUser registerUser) {
        User user = new User(registerUser);
        return signUp(user);
    }

    public String signUp(User user) {
        if (!existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.addRole(Role.ROLE_USER);
            user.setIsActive(true);
            userRepository.add(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
