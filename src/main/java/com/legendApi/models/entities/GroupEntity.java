package com.legendApi.models.entities;

import java.util.Date;

public class GroupEntity {
    private long id;
    private String name;
    private String description;
    private boolean isActive;
    private Date dateCreated;
    private Date dateModified;
    private long creatorId;
    private long subscriberCount;
    private long postCount;
    private long postsTodayCount;
    private String[] tags;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public String getDescription() {return description;}
    public void setDescription(String value) {description = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public Date getDateCreated() {return  dateCreated;}
    public void  setDateCreated(Date value) {dateCreated = value;}

    public Date getDateModified() {return  dateModified;}
    public void  setDateModified(Date value) {dateModified = value;}

    public long getCreatorId() {return creatorId;}
    public void setCreatorId(long value) {creatorId = value;}

    public long getSubscriberCount() {return subscriberCount;}
    public void setSubscriberCount(long value) {subscriberCount = value;}

    public long getPostCount() {return postCount;}
    public void setPostCount(long value) {postCount = value;}

    public long getPostsTodayCount() {return postsTodayCount;}
    public void setPostsTodayCount(long value) {postsTodayCount = value;}

    public String[] getTags() {return tags;}
    public void setTags(String[] value) {tags = value;}
}
