package com.legendApi.repositories;

import com.legendApi.models.entities.GroupEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GroupRepository extends CRUDRepository<GroupEntity> {
    void subscribe(long userId, String groupName) throws SQLException;

    void subscribe(long userId, long groupId) throws SQLException;

    boolean existsByName(String name);

    long getPostCount(long groupId);

    long getPostCount(String groupName);

    GroupEntity getGroupByName(String name);

    List<GroupEntity> getAllByCreatorId(long creatorId);

    List<GroupEntity> getAll(int limit, long lastCount, boolean initial, boolean asc);

    List<UserEntity> getSubscribedUsers(long groupId);

    List<GroupEntity> getSubscribedGroups(long userId);

    List<String> getSimpleSubscribedGroups(long userId);
}
