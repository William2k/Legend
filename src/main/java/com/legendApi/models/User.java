package com.legendApi.models;

import com.legendApi.models.entities.UserEntity;

public class User extends UserEntity {
    public User() {

    }

    public User(RegisterUser model) {
        setUsername(model.getUsername());
        setFirstName(model.getFirstName());
        setLastName(model.getLastName());
        setEmailAddress(model.getEmailAddress());
        setPassword(model.getPassword());
    }

    public User(UserEntity entity) {
        setId(entity.getId());
        setUsername(entity.getUsername());
        setEmailAddress(entity.getEmailAddress());
        setFirstName(entity.getFirstName());
        setLastName(entity.getLastName());
        setIsActive(entity.getIsActive());
    }
}
