package com.mouse.framework.data.mongo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@Import(MongoRepositoryTest.MongoTestEntityRepository.class)
class MongoRepositoryTest {
    private @Resource MongoTestEntityRepository mongoTestEntityRepository;
    private @Resource MongoTemplate mongoTemplate;

    @Test
    @Disabled
    void should_be_able_to_save_obj_into_mongo() {
        TestEntity entity = new TestEntity();
        entity.id = "mock-id";
        entity.name = "lisa su";

        mongoTestEntityRepository.save(entity);

        TestEntity fromMongo = mongoTemplate.findOne(query(where("id").is("mock-id")), TestEntity.class);

        assertThat(fromMongo).isNotNull();
        assertThat(fromMongo == entity).isFalse();
        assertThat(fromMongo.id).isEqualTo("mock-id");
        assertThat(fromMongo.name).isEqualTo("lisa su");
    }

    static class TestEntity {
        private String id;
        private String name;
    }

    @Repository
    static class MongoTestEntityRepository extends MongoRepository<TestEntity, String> {
    }
}