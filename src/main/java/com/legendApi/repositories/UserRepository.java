package com.legendApi.repositories;

import com.legendApi.models.User;

import java.util.List;

public interface UserRepository extends CRUDRepository<User> {
    User getByUsername(String username);
}
