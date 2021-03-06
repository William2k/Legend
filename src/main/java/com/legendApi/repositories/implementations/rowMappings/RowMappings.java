package com.legendApi.repositories.implementations.rowMappings;

import com.legendApi.models.entities.CommentEntity;
import com.legendApi.models.entities.GroupEntity;
import com.legendApi.models.entities.PostEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RowMappings {
    public static GroupEntity groupRowMapping(ResultSet rs, int rowNum) throws SQLException {
        GroupEntity group = new GroupEntity();
        group.setId(rs.getLong("id"));
        group.setName(rs.getString("name"));
        group.setDescription(rs.getString("description"));
        group.setCreatorUsername(rs.getString("creator_username"));
        group.setDateCreated(rs.getObject("date_created", LocalDateTime.class));
        group.setDateModified(rs.getObject("date_modified", LocalDateTime.class));
        group.setIsActive(rs.getBoolean("is_active"));
        group.setPostCount(rs.getInt("post_count"));
        group.setSubscriberCount(rs.getInt("subscriber_count"));
        group.setPostsTodayCount(rs.getInt("posts_today"));
        group.setTags((String[])rs.getArray("tags").getArray());

        return group;
    }

    public static PostEntity postRowMapping(ResultSet rs, int rowNum) throws SQLException {
        PostEntity post = new PostEntity();
        post.setId(rs.getLong("id"));
        post.setName(rs.getString("name"));
        post.setCreatorUsername(rs.getString("creator_username"));
        post.setDateCreated(rs.getObject("date_created", LocalDateTime.class));
        post.setDateModified(rs.getObject("date_modified", LocalDateTime.class));
        post.setGroupId(rs.getLong("group_id"));
        post.setContent(rs.getString("content"));
        post.setIsActive(rs.getBoolean("is_active"));
        post.setLikes(rs.getLong("likes"));
        post.setSubscriberCount(rs.getLong("subscriber_count"));
        post.setCommentCount(rs.getLong("comment_count"));
        post.setCommentsTodayCount(rs.getLong("comments_today"));

        Boolean liked = rs.getBoolean("liked");
        if (rs.wasNull()) liked = null;

        post.setLiked(liked);

        return post;
    }

    public static CommentEntity commentRowMapping(ResultSet rs, int rowNum) throws SQLException {
        CommentEntity comment = new CommentEntity();
        comment.setId(rs.getLong("id"));
        comment.setContent(rs.getString("content"));
        comment.setPostId(rs.getInt("post_id"));
        comment.setLikes(rs.getLong("likes"));
        comment.setCreatorUsername(rs.getString("creator_username"));
        comment.setParentCommentId(rs.getInt("parent_comment_id"));
        comment.setDateCreated(rs.getObject("date_created", LocalDateTime.class));
        comment.setDateModified(rs.getObject("date_modified", LocalDateTime.class));
        comment.setIsActive(rs.getBoolean("is_active"));

        Boolean liked = rs.getBoolean("liked");
        if (rs.wasNull()) liked = null;

        comment.setLiked(liked);

        return comment;
    }

    public static UserEntity userRowMapping(ResultSet rs, int rowNum) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailAddress(rs.getString("email_address"));
        user.setPassword(rs.getString("password"));
        user.setStringRoles((String[])rs.getArray("roles").getArray());
        user.setDateCreated(rs.getObject("date_created", LocalDateTime.class));
        user.setDateModified(rs.getObject("date_modified", LocalDateTime.class));
        user.setIsActive(rs.getBoolean("is_active"));
        return user;
    }
}
