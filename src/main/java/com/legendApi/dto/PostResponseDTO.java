package com.legendApi.dto;

import com.legendApi.models.entities.PostEntity;

import java.time.LocalDateTime;
import java.util.Date;

public class PostResponseDTO {
    public PostResponseDTO() { }

    public PostResponseDTO(PostEntity postEntity) {
        setId(postEntity.getId());
        setName(postEntity.getName());
        setContent(postEntity.getContent());
        setCommentsCount(postEntity.getCommentCount());
        setSubscriberCount(postEntity.getSubscriberCount());
        setCommentsTodayCount(postEntity.getCommentsTodayCount());
        setDateCreated(postEntity.getDateCreated() != null ? postEntity.getDateCreated().toString() : null);
        setDateModified(postEntity.getDateModified() != null ? postEntity.getDateModified().toString() : null);
        setCreator(postEntity.getCreatorUsername());
        setIsActive(postEntity.getIsActive());
        setLikes(postEntity.getLikes());
        setLiked(postEntity.isLiked());
    }

    private long id;
    private String name;
    private String content;
    private long subscriberCount;
    private long commentsTodayCount;
    private long commentsCount;
    private String dateCreated;
    private String dateModified;
    private String creator;
    private long likes;
    private boolean isActive;
    private Boolean liked;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public String getContent() { return content; }
    public void setContent(String value) { content = value; }

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

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public long getLikes() { return likes; }
    public void setLikes(long likes) { this.likes = likes; }

    public Boolean isLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
}
