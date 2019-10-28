package com.legendApi.models.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class PostEntity {
    private long id;
    private String name;
    private String content;
    private boolean isActive;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String creatorUsername;
    private long groupId;
    private long subscriberCount;
    private long commentCount;
    private long commentsTodayCount;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public String getContent() { return content; }
    public void setContent(String value) { content = value; }

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public LocalDateTime getDateCreated() {return  dateCreated;}
    public void  setDateCreated(LocalDateTime value) {dateCreated = value;}

    public LocalDateTime getDateModified() {return  dateModified;}
    public void  setDateModified(LocalDateTime value) {dateModified = value;}

    public String getCreatorUsername() {return creatorUsername;}
    public void setCreatorUsername(String value) {creatorUsername = value;}

    public long getGroupId() {return groupId;}
    public void setGroupId(long value) {groupId = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getCommentCount() {return commentCount;}
    public void setCommentCount(long value) {commentCount = value;}

    public long getCommentsTodayCount() { return commentsTodayCount;}
    public void setCommentsTodayCount(long value) { commentsTodayCount = value; }
}
