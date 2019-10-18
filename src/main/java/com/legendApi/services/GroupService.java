package com.legendApi.services;

import com.legendApi.core.exceptions.CustomHttpException;
import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.AddGroup;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.repositories.GroupRepository;
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

   public List<GroupResponseDTO> getAll() {
        List<GroupEntity> groups = groupRepository.getAll();

        List<GroupResponseDTO> groupDtos = groups
                .stream().map(GroupResponseDTO::new)
                .collect(Collectors.toList());

       groupDtos.forEach(groupResponseDTO ->
                        groupResponseDTO.setPostCount( groupRepository.getPostCount(groupResponseDTO.getName()) ));

        return groupDtos;
   }

   public void addGroup(AddGroup group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(group.getName());
        groupEntity.setDescription(group.getDescription());
        groupEntity.setTags(group.getTags());

        try {
            groupRepository.add(groupEntity);
        } catch (Exception ex) {
            throw new CustomHttpException("Something went wrong with adding the group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }
}
