package com.legendApi.dto;

import com.legendApi.models.entities.GroupEntity;

import java.util.Date;

public class GroupResponseDTO {
    public GroupResponseDTO() {}

    public GroupResponseDTO(GroupEntity group) {
        setName(group.getName());
        setDescription(group.getDescription());
        setIsActive(group.getIsActive());
        setSubscriberCount(group.getSubscriberCount());
        setPostCount(group.getPostCount());
        setTags(group.getTags());
        setPostsTodayCount(group.getPostsTodayCount());
    }

    private String name;
    private String description;
    private boolean isActive;
    private long postCount;
    private long subscriberCount;
    private long postsTodayCount;
    private String[] tags;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public String getDescription() {return description;}
    public void setDescription(String value) {description = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getPostCount() {return postCount;}
    public void setPostCount(long value) {postCount = value;}

    public long getPostsTodayCount() { return postsTodayCount; }
    public void setPostsTodayCount(long value) { postsTodayCount = value; }

    public String[] getTags() {return tags;}
    public void setTags(String[] value) {tags = value;}
}
