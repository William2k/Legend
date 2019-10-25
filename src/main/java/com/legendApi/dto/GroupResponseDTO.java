package com.legendApi.dto;

import com.legendApi.models.entities.GroupEntity;

import java.time.LocalDateTime;
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
        setDateCreated(group.getDateCreated().toString());
        setDateModified(group.getDateModified() != null ? group.getDateModified().toString() : null);
    }

    private String name;
    private String description;
    private boolean isActive;
    private long postCount;
    private long subscriberCount;
    private long postsTodayCount;
    private String dateCreated;
    private String dateModified;
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

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String value) { dateCreated = value; }

    public String getDateModified() { return dateModified; }
    public void setDateModified(String value) { dateModified = value; }
}
