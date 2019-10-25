package com.legendApi.dto;

import com.legendApi.models.entities.PostEntity;

import java.time.LocalDateTime;
import java.util.Date;

public class PostResponseDTO {
    public PostResponseDTO() { }

    public PostResponseDTO(PostEntity postEntity) {
        setName(postEntity.getName());
        setCommentsCount(postEntity.getCommentCount());
        setSubscriberCount(postEntity.getSubscriberCount());
        setCommentsTodayCount(postEntity.getCommentsTodayCount());
        setDateCreated(postEntity.getDateCreated() != null ? postEntity.getDateCreated().toString() : null);
        setDateModified(postEntity.getDateModified() != null ? postEntity.getDateModified().toString() : null);
        setIsActive(postEntity.getIsActive());
    }

    private String name;
    private long subscriberCount;
    private long commentsTodayCount;
    private long commentsCount;
    private String dateCreated;
    private String dateModified;
    private boolean isActive;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getCommentsTodayCount() {return commentsTodayCount;}
    public void setCommentsTodayCount(long value) {commentsTodayCount = value;}

    public long getCommentsCount() { return commentsCount; }
    public void setCommentsCount(long value) { commentsCount = value; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String value) { dateCreated = value; }

    public String getDateModified() { return dateModified; }
    public void setDateModified(String value) { dateModified = value; }

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}
}
