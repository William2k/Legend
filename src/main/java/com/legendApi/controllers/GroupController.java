package com.legendApi.controllers;

import com.legendApi.dto.GroupResponseDTO;
import com.legendApi.models.AddGroup;
import com.legendApi.services.GroupService;
import com.legendApi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/group")
public class GroupController {
    private final UserService userService;
    private final GroupService groupService;

    public GroupController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public GroupResponseDTO getGroup(@PathVariable("name") String name ) {
        GroupResponseDTO group = groupService.getByName(name);

        return group;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GroupResponseDTO> getGroups() {
        List<GroupResponseDTO> groups = groupService.getAll();

        return groups;
    }

    @RequestMapping(value = "current-user", method = RequestMethod.GET)
    public List<GroupResponseDTO> getUserGroups() {
        List<GroupResponseDTO> groups = groupService.getUserGroups();

        return groups;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public void addGroup(@RequestBody AddGroup model) {
       groupService.addGroup(model);
    }
}
