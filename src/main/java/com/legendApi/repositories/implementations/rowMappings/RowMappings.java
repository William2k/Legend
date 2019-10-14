package com.legendApi.repositories.implementations.rowMappings;

import com.legendApi.models.entities.CommentEntity;
import com.legendApi.models.entities.TopicEntity;
import com.legendApi.models.entities.UserEntity;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMappings {
    public static TopicEntity topicRowMapping(ResultSet rs, int rowNum) throws SQLException {
        TopicEntity topic = new TopicEntity();
        topic.setId(rs.getLong("id"));
        topic.setName(rs.getString("name"));
        topic.setCreatorId(rs.getInt("creator_id"));
        topic.setDateCreated(rs.getDate("date_created"));
        topic.setDateModified(rs.getDate("date_modified"));
        topic.setIsActive(rs.getBoolean("is_active"));
        return topic;
    }

    public static CommentEntity commentRowMapping(ResultSet rs, int rowNum) throws SQLException {
        CommentEntity comment = new CommentEntity();
        comment.setId(rs.getLong("id"));
        comment.setContent(rs.getString("content"));
        comment.setTopicId(rs.getInt("topic_id"));
        comment.setCreatorId(rs.getInt("creator_id"));
        comment.setParentCommentId(rs.getInt("parent_comment_id"));
        comment.setDateCreated(rs.getDate("date_created"));
        comment.setDateModified(rs.getDate("date_modified"));
        comment.setIsActive(rs.getBoolean("is_active"));
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

        Array roles = rs.getArray("roles");

        user.setStringRoles((String[]) roles.getArray());
        user.setDateCreated(rs.getDate("date_created"));
        user.setDateModified(rs.getDate("date_modified"));
        user.setIsActive(rs.getBoolean("is_active"));
        return user;
    }
}
