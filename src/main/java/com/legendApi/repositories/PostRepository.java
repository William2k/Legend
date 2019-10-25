package com.legendApi.repositories;

import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;

import java.util.List;

public interface PostRepository extends CRUDRepository<PostEntity> {
    List<PostEntity> getAllByCreatorId(long creatorId);

    List<UserEntity> getSubscribedUsers(long topicId);

    List<PostEntity> getSubscribedPosts(long userId);

    List<PostEntity> getAll(String group, int limit, long lastCount, boolean initial, boolean asc);
}
