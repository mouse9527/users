package com.mouse.users.iam.gateways.persistence;

import com.mouse.framework.data.mongo.MongoRepository;
import com.mouse.users.iam.domain.Role;
import com.mouse.users.iam.domain.RoleRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRoleRepository extends MongoRepository<Role, String> implements RoleRepository {
    public MongoRoleRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Role.class, COLLECTION_NAME);
    }
}
