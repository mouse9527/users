package com.mouse.users.iam.gateways.persistence;

import com.mouse.framework.data.mongo.MongoRepository;
import com.mouse.users.iam.domain.User;
import com.mouse.users.iam.domain.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoUserRepository extends MongoRepository<User, String> implements UserRepository {
    public MongoUserRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, User.class, COLLECTION_NAME);
    }

    @Override
    public Optional<User> loadByUsername(String username) {
        return findOptional(query(where("username").is(username)));
    }
}
