package com.legendApi.services;

import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
