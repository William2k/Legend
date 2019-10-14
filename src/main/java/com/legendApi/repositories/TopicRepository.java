package com.legendApi.repositories;

import com.legendApi.models.entities.TopicEntity;
import com.legendApi.models.entities.UserEntity;

import java.util.List;

public interface TopicRepository extends CRUDRepository<TopicEntity> {
    List<TopicEntity> getAllByCreatorId(long creatorId);

    List<UserEntity> getSubscribedUsers(long topicId);

    List<TopicEntity> getSubscribedTopics(long userId);
}
