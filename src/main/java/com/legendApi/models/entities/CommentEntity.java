package com.legendApi.models.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class CommentEntity {
    private long id;
    private String content;
    private boolean isActive;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private long postId;
    private long parentCommentId;
    private String creatorUsername;
    private long likes;

    public long getId() {return id;}
    public void setId(long value) {id = value;}

    public String getContent() {return content;}
    public void setContent(String value) {content = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public LocalDateTime getDateCreated() {return  dateCreated;}
    public void  setDateCreated(LocalDateTime value) {dateCreated = value;}

    public LocalDateTime getDateModified() {return  dateModified;}
    public void  setDateModified(LocalDateTime value) {dateModified = value;}

    public long getLikes() { return likes; }
    public void setLikes(long likes) { this.likes = likes; }

    public long getPostId() {return postId;}
    public void setPostId(long value) {postId = value;}

    public long getParentCommentId() {return parentCommentId;}
    public void setParentCommentId(long value) {parentCommentId = value;}

    public String getCreatorUsername() {return creatorUsername;}
    public void setCreatorUsername(String value) {creatorUsername = value;}
}
