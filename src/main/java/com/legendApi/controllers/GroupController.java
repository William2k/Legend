package com.legendApi.controllers;

import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.AddGroup;
import com.legendApi.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GroupResponseDTO> getGroups() {
        List<GroupResponseDTO> groups = groupService.getAll();

        return groups;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addGroup(@RequestBody AddGroup model) {
       groupService.addGroup(model);
    }
}
