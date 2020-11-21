package com.mouse.framework.data.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class MongoRepository<T, ID> {
    private final MongoTemplate mongoTemplate;
    private final String collectionName;

    public MongoRepository(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
    }

    public void save(T entity) {
        mongoTemplate.save(entity, collectionName);
    }
}
