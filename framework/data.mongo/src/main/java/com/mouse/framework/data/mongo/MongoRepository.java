package com.mouse.framework.data.mongo;

import com.mouse.framework.domain.core.AggregationNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class MongoRepository<T, ID> {
    private final MongoTemplate mongoTemplate;
    private final Class<T> entityClass;
    private final String collectionName;

    public MongoRepository(MongoTemplate mongoTemplate, Class<T> entityClass, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;
        this.collectionName = collectionName;
    }

    public void save(T entity) {
        mongoTemplate.save(entity, collectionName);
    }

    public T load(ID id) {
        return loadOptional(id).orElseThrow(() -> new AggregationNotFoundException(entityClass.getName(), collectionName));
    }

    public Optional<T> loadOptional(ID id) {
        return Optional.ofNullable(mongoTemplate.findOne(query(where("_id").is(id)), entityClass, collectionName));
    }

    public Optional<T> findOptional(Query query) {
        return Optional.ofNullable(mongoTemplate.findOne(query, entityClass, collectionName));
    }

    public List<T> listAll() {
        return mongoTemplate.findAll(entityClass, collectionName);
    }

    public List<T> list(Query query) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }
}

