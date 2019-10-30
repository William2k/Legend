package com.legendApi.repositories;

import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PostRepository extends CRUDRepository<PostEntity> {
    List<PostEntity> getAllByCreatorId(long creatorId);

    List<UserEntity> getSubscribedUsers(long topicId);

    List<PostEntity> getSubscribedPosts(long userId);

    Map<Long, String> getSimpleSubscribedPosts(long userId);

    void subscribe(long userId, long postId, String groupName);

    void unsubscribe(long userId, long postId, String groupName);

    List<PostEntity> getAll(String group, int limit, long lastCount, boolean initial, boolean asc);
}
