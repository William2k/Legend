package com.legendApi.models.entities;

import java.util.Date;

public class PostEntity {
    private long id;
    private String name;
    private boolean isActive;
    private Date dateCreated;
    private Date dateModified;
    private long creatorId;
    private long groupId;
    private long openingCommentId;
    private long subscriberCount;
    private long commentCount;
    private long commentsTodayComment;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getName() {return name;}
    public void setName(String value) {name = value;}

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

    public long getOpeningCommentId() {return openingCommentId;}
    public void setOpeningCommentId(long value) {openingCommentId = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getCommentCount() {return commentCount;}
    public void setCommentCount(long value) {commentCount = value;}

    public long getCommentsTodayComment() { return commentsTodayComment;}
    public void setCommentsTodayComment(long value) { commentsTodayComment = value; }
}
