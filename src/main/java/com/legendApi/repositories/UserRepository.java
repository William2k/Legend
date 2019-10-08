package com.legendApi.repositories;

import com.legendApi.models.entities.UserEntity;

public interface UserRepository extends CRUDRepository<UserEntity> {
    UserEntity getByUsername(String username);

    boolean existsByUsername(String username);
}
