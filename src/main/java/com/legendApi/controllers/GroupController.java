package com.legendApi.controllers;

import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.AddGroup;
import com.legendApi.services.GroupService;
import com.legendApi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {
    private final UserService userService;
    private final GroupService groupService;

    public GroupController(UserService userService, GroupService groupService) {
        this.userService = userService;
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
