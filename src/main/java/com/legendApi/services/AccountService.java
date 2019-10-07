package com.legendApi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.legendApi.config.ConfigProperties;
import com.legendApi.helpers.DateHelper;
import com.legendApi.helpers.PasswordHelper;
import com.legendApi.models.Login;
import com.legendApi.models.RegisterUser;
import com.legendApi.models.User;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final ConfigProperties configProperties;

    @Autowired
    public AccountService(UserRepository userRepository, ConfigProperties configProperties) {
        this.userRepository = userRepository;
        this.configProperties = configProperties;
    }

    public long GetCurrentUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);

            Claim idClaim = jwt.getClaim("userId");

            return idClaim.asLong();
        } catch (JWTDecodeException exception){
            //Invalid token
            throw exception;
        }
    }

    public String login(Login model) {
        UserEntity userEntity = userRepository.getByUsername(model.getUsername().toUpperCase());

        if(!PasswordHelper.verify(userEntity.getPassword(), model.getPassword(), userEntity.getUsername().toUpperCase(Locale.ENGLISH))) {
            return null;
        }

        try {
            String issuer = configProperties.getConfigValue("jwt.secret");
            String secret = configProperties.getConfigValue("jwt.issuer");
            Date date = DateHelper.addDays(1);
            Algorithm algorithm = Algorithm.HMAC512(secret);

            String token = JWT.create()
                    .withClaim("userId", userEntity.getId())
                    .withExpiresAt(date)
                    .withIssuer(issuer)
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception){
            throw exception;
        }
    }

    public void addUser(RegisterUser model) {
        User user = new User(model);
        String hashedPassword = PasswordHelper.hash(user.getPassword(), user.getUsername().toUpperCase(Locale.ENGLISH));

        user.setPassword(hashedPassword);
        user.setIsActive(true);

        userRepository.add(user);
    }
}
