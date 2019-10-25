package com.legendApi.models.entities;

import java.util.Date;

public class PostEntity {
    private long id;
    private String name;
    private String content;
    private boolean isActive;
    private Date dateCreated;
    private Date dateModified;
    private long creatorId;
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

    public Date getDateCreated() {return  dateCreated;}
    public void  setDateCreated(Date value) {dateCreated = value;}

    public Date getDateModified() {return  dateModified;}
    public void  setDateModified(Date value) {dateModified = value;}

    public long getCreatorId() {return creatorId;}
    public void setCreatorId(long value) {creatorId = value;}

    public long getGroupId() {return groupId;}
    public void setGroupId(long value) {groupId = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getCommentCount() {return commentCount;}
    public void setCommentCount(long value) {commentCount = value;}

    public long getCommentsTodayCount() { return commentsTodayCount;}
    public void setCommentsTodayCount(long value) { commentsTodayCount = value; }
}
