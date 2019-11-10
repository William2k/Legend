package com.legendApi.services;

import com.legendApi.core.exceptions.CustomHttpException;
import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.AddGroup;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.repositories.GroupRepository;
import com.legendApi.security.helpers.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public GroupResponseDTO getByName(String name) {
        GroupResponseDTO group = new GroupResponseDTO(groupRepository.getGroupByName(name));

        group.setPostCount(groupRepository.getPostCount(group.getName()));

        return group;
    }

    public List<GroupResponseDTO> getUserGroups() {
        List<GroupEntity> groups = groupRepository.getAllByCreatorId(CurrentUser.getId());

        List<GroupResponseDTO> groupDtos = groups
                .parallelStream().map(GroupResponseDTO::new)
                .collect(Collectors.toList());

        return groupDtos;
    }

   public List<GroupResponseDTO> getAll() {
        List<GroupEntity> groups = groupRepository.getAll();

        List<GroupResponseDTO> groupDtos = groups
                .parallelStream().map(GroupResponseDTO::new)
                .collect(Collectors.toList());

        return groupDtos;
   }

    public List<GroupResponseDTO> getAll(int limit, long lastCount, boolean initial, boolean asc) {
        List<GroupEntity> groups = groupRepository.getAll(limit, lastCount, initial, asc);

        List<GroupResponseDTO> groupDtos = groups
                .parallelStream().map(GroupResponseDTO::new)
                .collect(Collectors.toList());

        return groupDtos;
    }

    public void subscribeToGroup(String groupName) {
        try {
            groupRepository.subscribe(CurrentUser.getId(), groupName);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong subscribing to group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void unsubscribeToGroup(String groupName) {
        try {
            groupRepository.unsubscribe(CurrentUser.getId(), groupName);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong unsubscribing to group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<String> getSimpleSubscribedGroups() {
        return groupRepository.getSimpleSubscribedGroups(CurrentUser.getId());
    }

   public void addGroup(AddGroup group) {
        String name = group.getName();

        if(name.contains(" ")) {
            throw new CustomHttpException("Group name includes spaces", HttpStatus.BAD_REQUEST);
        }

        boolean exists = groupRepository.existsByName(name);

        if(exists) {
            throw new CustomHttpException("Group already exists", HttpStatus.CONFLICT);
        }

       if(group.getName().toUpperCase().equals("SUBSCRIBE")) {
           throw new CustomHttpException("'SUBSCRIBE' cannot be used as a name", HttpStatus.BAD_REQUEST);
       }

       GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(name);
        groupEntity.setDescription(group.getDescription());
        groupEntity.setTags(group.getTags());
        groupEntity.setCreatorUsername(CurrentUser.getUsername());
        groupEntity.setIsActive(true);

        try {
            groupRepository.add(groupEntity);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with adding the group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }
}
