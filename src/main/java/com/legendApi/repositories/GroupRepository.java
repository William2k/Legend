package com.legendApi.repositories;

import com.legendApi.models.entities.GroupEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.SQLException;
import java.util.List;

public interface GroupRepository extends CRUDRepository<GroupEntity> {
    long subscribe(long userId, long groupId) throws SQLException;

    long getPostCount(long groupId);

    long getPostCount(String groupName);

    GroupEntity getGroupByName(String name);

    List<GroupEntity> getAllByCreatorId(long creatorId);

    List<UserEntity> getSubscribedUsers(long groupId);

    List<GroupEntity> getSubscribedGroups(long userId);
}
