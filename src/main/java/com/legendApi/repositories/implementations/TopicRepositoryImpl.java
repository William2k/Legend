package com.legendApi.repositories.implementations;

import com.legendApi.core.CustomJdbc;
import com.legendApi.models.entities.TopicEntity;
import com.legendApi.models.entities.UserEntity;
import com.legendApi.repositories.TopicRepository;
import com.legendApi.repositories.implementations.rowMappings.RowMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TopicRepositoryImpl implements TopicRepository {
    private final CustomJdbc customJdbc;

    @Autowired
    public TopicRepositoryImpl(CustomJdbc customJdbc) {
        this.customJdbc = customJdbc;
    }

    @Override
    public List<TopicEntity> getAllByCreatorId(long creatorId) {
        String sql = "SELECT * FROM legend.topics" +
                "WHERE creator_id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", creatorId);

        List<TopicEntity> result = customJdbc.query(sql, parameters, RowMappings::topicRowMapping);

        return result;
    }

    @Override
    public List<UserEntity> getSubscribedUsers(long topicId) {
        String sql = "SELECT u.* " +
                "FROM legend.users AS u JOIN legend.users_topics AS ut ON u.id = ut.user_id" +
                "WHERE ut.topic_id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", topicId);

        List<UserEntity> result = customJdbc.query(sql, parameters, RowMappings::userRowMapping);

        return result;
    }

    @Override
    public List<TopicEntity> getSubscribedTopics(long userId) {
        String sql = "SELECT t.* " +
                "FROM legend.topics AS t JOIN legend.users_topics AS ut ON t.id = ut.topic_id" +
                "WHERE ut.user_id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", userId);

        List<TopicEntity> result = customJdbc.query(sql, parameters, RowMappings::topicRowMapping);

        return result;
    }

    @Override
    public List<TopicEntity> getAll() {
        List<TopicEntity> result = customJdbc.query("SELECT * FROM legend.topics", RowMappings::topicRowMapping);

        return result;
    }

    @Override
    public TopicEntity getById(long id) {
        String sql = "SELECT * FROM legend.topics " +
                "WHERE id = :id";

        Map<String, Long> parameters = new HashMap<>();
        parameters.put("id", id);

        TopicEntity result = customJdbc.queryForObject(sql, parameters, RowMappings::topicRowMapping);

        return result;
    }

    @Override
    public long add(TopicEntity topic) {
        String sql = "INSERT INTO legend.topics(name, is_active, creator_id) " +
                "VALUES (:name, :isActive, :creatorId)";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", topic.getName());
        parameters.put("isActive", topic.getIsActive());
        parameters.put("creatorId", topic.getCreatorId());

        return customJdbc.update(sql, parameters);
    }

    @Override
    public void update(TopicEntity topic) {
        String sql = "UPDATE legend.topics " +
                "SET name=:name, is_active=:isActive, date_modified=now() " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", topic.getId());
        parameters.put("name", topic.getName());
        parameters.put("isActive", topic.getIsActive());

        customJdbc.update(sql, parameters);
    }

    @Override
    public void delete(long id) {
        String sql = "UPDATE legend.topics" +
                "SET is_active = false " +
                "WHERE id = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        customJdbc.update(sql, parameters);
    }
}
