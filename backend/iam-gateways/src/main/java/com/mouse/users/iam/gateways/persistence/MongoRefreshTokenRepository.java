package com.mouse.users.iam.gateways.persistence;

import com.mouse.framework.data.mongo.MongoRepository;
import com.mouse.users.iam.domain.RefreshToken;
import com.mouse.users.iam.domain.RefreshTokenRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRefreshTokenRepository extends MongoRepository<RefreshToken, String> implements RefreshTokenRepository {
    public MongoRefreshTokenRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, RefreshToken.class, COLLECTION_NAME);
    }
}
