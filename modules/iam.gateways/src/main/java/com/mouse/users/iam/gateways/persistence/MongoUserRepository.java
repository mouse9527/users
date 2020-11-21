package com.mouse.users.iam.gateways.persistence;

import com.mouse.framework.data.mongo.MongoRepository;
import com.mouse.users.iam.domain.User;
import com.mouse.users.iam.domain.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoUserRepository extends MongoRepository<User, String> implements UserRepository {
    public MongoUserRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, User.class, COLLECTION_NAME);
    }
}
