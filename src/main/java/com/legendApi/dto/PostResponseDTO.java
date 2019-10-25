package com.legendApi.dto;

import com.legendApi.models.entities.PostEntity;

import java.util.Date;

public class PostResponseDTO {
    public PostResponseDTO() { }

    public PostResponseDTO(PostEntity postEntity) {
        setName(postEntity.getName());
        setCommentsCount(postEntity.getCommentCount());
        setSubscriberCount(postEntity.getSubscriberCount());
        setCommentsTodayCount(postEntity.getCommentsTodayCount());
        setIsActive(postEntity.getIsActive());
    }

    private String name;
    private long subscriberCount;
    private long commentsTodayCount;
    private long commentsCount;
    private boolean isActive;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getCommentsTodayCount() {return commentsTodayCount;}
    public void setCommentsTodayCount(long value) {commentsTodayCount = value;}

    public long getCommentsCount() { return commentsCount; }
    public void setCommentsCount(long value) { commentsCount = value; }

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}
}
