package com.legendApi.models.entities;

import java.util.Date;

public class CommentEntity {
    private long id;
    private String content;
    private boolean isActive;
    private Date dateCreated;
    private Date dateModified;
    private long topicId;
    private long parentCommentId;
    private long creatorId;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getContent() {return content;}
    public void setContent(String value) {content = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public Date getDateCreated() {return  dateCreated;}
    public void  setDateCreated(Date value) {dateCreated = value;}

    public Date getDateModified() {return  dateModified;}
    public void  setDateModified(Date value) {dateModified = value;}

    public long getTopicId() {return topicId;}
    public void setTopicId(long value) {topicId = value;}

    public long getParentCommentId() {return parentCommentId;}
    public void setParentCommentId(long value) {parentCommentId = value;}

    public long getCreatorId() {return creatorId;}
    public void setCreatorId(long value) {creatorId = value;}
}
