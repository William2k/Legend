package com.legendApi.repositories;

import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PostRepository {
    List<PostEntity> getAllByCreatorId(long creatorId);

    List<UserEntity> getSubscribedUsers(long topicId);

    List<PostEntity> getSubscribedPosts(long userId);

    Map<Long, String> getSimpleSubscribedPosts(long userId);

    long subscribe(long userId, long postId, String groupName);

    long unsubscribe(long userId, long postId, String groupName);

    long addLike(long userId, long postId, boolean liked);

    long removeLike(long userId, long postId);

    List<PostEntity> searchPosts(String term, long userId);

    List<PostEntity> getAll(long userId);

    List<PostEntity> getAll(String group, int limit, long lastCount, boolean initial, boolean asc, long userId);

    PostEntity getById(long id, long userId);

    long add(PostEntity post) throws SQLException;

    void update(PostEntity post);

    void delete(long id);
}
