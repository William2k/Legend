package com.legendApi.dto;

import com.legendApi.models.entities.GroupEntity;

import java.util.Date;

public class GroupResponseDTO {
    public GroupResponseDTO() {}

    public GroupResponseDTO(GroupEntity group) {
        setName(group.getName());
        setIsActive(group.getIsActive());
        setSubscriberCount(group.getSubscriberCount());
    }

    private String name;
    private boolean isActive;
    private long postCount;
    private long subscriberCount;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getPostCount() {return postCount;}
    public void setPostCount(long value) {postCount = value;}
}
