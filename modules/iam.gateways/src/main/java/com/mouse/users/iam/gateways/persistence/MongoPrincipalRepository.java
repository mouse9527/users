package com.mouse.users.iam.gateways.persistence;

import com.mouse.framework.data.mongo.MongoRepository;
import com.mouse.users.iam.domain.Principal;
import com.mouse.users.iam.domain.PrincipalRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoPrincipalRepository extends MongoRepository<Principal, String> implements PrincipalRepository {
    public MongoPrincipalRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Principal.class, COLLECTION_NAME);
    }
}
