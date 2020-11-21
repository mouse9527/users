package com.mouse.framework.data.mongo;

import com.mouse.framework.domain.core.AggregationNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
@Import(MongoRepositoryTest.MongoTestEntityRepository.class)
class MongoRepositoryTest {
    public static final String MOCK_ID = "mock-id";
    public static final String LISA_SU = "lisa su";
    private @Resource MongoTestEntityRepository mongoTestEntityRepository;
    private @Resource MongoTemplate mongoTemplate;
    private TestEntity entity;

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
        entity.id = MOCK_ID;
        entity.name = LISA_SU;
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(MongoTestEntityRepository.COLLECTION_NAME);
    }

    @Test
    void should_be_able_to_save_obj_into_mongo() {
        mongoTestEntityRepository.save(entity);

        TestEntity fromMongo = mongoTemplate.findOne(query(where("id").is(MOCK_ID)), TestEntity.class, MongoTestEntityRepository.COLLECTION_NAME);

        assertEqual(fromMongo);
    }

    private void assertEqual(TestEntity fromMongo) {
        assertThat(fromMongo).isNotNull();
        assertThat(fromMongo == entity).isFalse();
        assertThat(fromMongo.id).isEqualTo(MOCK_ID);
        assertThat(fromMongo.name).isEqualTo(LISA_SU);
    }

    @Test
    void should_be_able_to_load_entity_from_repository() {
        mongoTemplate.save(entity, MongoTestEntityRepository.COLLECTION_NAME);

        TestEntity fromMongo = mongoTestEntityRepository.load(MOCK_ID);

        assertEqual(fromMongo);
    }

    @Test
    void should_be_able_to_raise_aggregation_not_found_exception_when_not_found() {
        Throwable throwable = catchThrowable(() -> mongoTestEntityRepository.load("unknown"));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(AggregationNotFoundException.class);
        assertThat(throwable).hasMessage(String.format("Aggregation %s not found in collection %s", TestEntity.class.getName(), MongoTestEntityRepository.COLLECTION_NAME));
    }

    static class TestEntity {
        private String id;
        private String name;
    }

    @Repository
    static class MongoTestEntityRepository extends MongoRepository<TestEntity, String> {
        public static final String COLLECTION_NAME = "test-entities";

        public MongoTestEntityRepository(MongoTemplate mongoTemplate) {
            super(mongoTemplate, TestEntity.class, COLLECTION_NAME);
        }
    }
}