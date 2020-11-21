package com.mouse.framework.data.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

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
        return mongoTemplate.findOne(query(where("_id").is(id)), entityClass, collectionName);
    }
}
