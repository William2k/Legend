package com.legendApi.services;

import com.legendApi.models.Login;
import com.legendApi.models.RegisterUser;
import com.legendApi.models.User;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.getAll().stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    public User getUserById(long id) {
        return new User(userRepository.getById(id));
    }
}
