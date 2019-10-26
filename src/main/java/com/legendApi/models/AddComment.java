package com.legendApi.models;

public class AddComment {
    private long postId;
    private String content;
    private long parentCommentId;

    public long getPostId() { return postId; }
    public void setPostId(long postId) { this.postId = postId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(long parentCommentId) { this.parentCommentId = parentCommentId; }
}
