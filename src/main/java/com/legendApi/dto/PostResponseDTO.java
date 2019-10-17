package com.legendApi.dto;

import com.legendApi.models.entities.PostEntity;

import java.util.Date;

public class PostResponseDTO {
    public PostResponseDTO() { }

    public PostResponseDTO(PostEntity postEntity) {
        name = postEntity.getName();
        isActive = postEntity.getIsActive();
    }

    private String name;
    private long subscriberCount;
    private long postsTodayCount;
    private boolean isActive;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getPostsTodayCount() {return postsTodayCount;}
    public void setPostsTodayCount(long value) {postsTodayCount = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}
}
