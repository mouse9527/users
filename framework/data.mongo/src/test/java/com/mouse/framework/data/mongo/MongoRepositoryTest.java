package com.mouse.framework.data.mongo;

import com.mouse.framework.domain.core.AggregationNotFoundException;
import com.mouse.framework.test.EnableEmbeddedMongoDB;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
@EnableEmbeddedMongoDB
@Import(MongoRepositoryTest.MongoTestEntityRepository.class)
class MongoRepositoryTest {
    public static final String MOCK_ID = "mock-id";
    public static final String LISA_SU = "lisa su";
    public static final String COLLECTION_NAME = "test-entities";
    private final TestEntity entity = new TestEntity(MOCK_ID, LISA_SU);
    private @Resource MongoTestEntityRepository mongoTestEntityRepository;
    private @Resource MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    @Test
    void should_be_able_to_save_obj_into_mongo() {
        mongoTestEntityRepository.save(entity);

        TestEntity fromMongo = mongoTemplate.findOne(query(where("id").is(MOCK_ID)), TestEntity.class, COLLECTION_NAME);

        assertEqual(fromMongo);
    }

    private void assertEqual(TestEntity fromMongo) {
        assertThat(fromMongo).isNotNull();
        assertThat(fromMongo == entity).isFalse();
        assertThat(fromMongo).isEqualTo(entity);
    }

    @Test
    void should_be_able_to_update_entity_when_it_exists() {
        mongoTemplate.save(entity);

        String modifiedName = "modified-name";
        entity.name = modifiedName;
        mongoTestEntityRepository.save(entity);

        TestEntity fromMongo = mongoTemplate.findOne(query(where("_id").is(MOCK_ID)), TestEntity.class, COLLECTION_NAME);
        assertThat(fromMongo).isNotNull();
        assertThat(fromMongo.id).isEqualTo(MOCK_ID);
        assertThat(fromMongo.name).isEqualTo(modifiedName);
    }

    @Test
    void should_be_able_to_load_entity_from_repository() {
        mongoTemplate.save(entity, COLLECTION_NAME);

        TestEntity fromMongo = mongoTestEntityRepository.load(MOCK_ID);

        assertEqual(fromMongo);
    }

    @Test
    void should_be_able_to_raise_aggregation_not_found_exception_when_not_found() {
        Throwable throwable = catchThrowable(() -> mongoTestEntityRepository.load("unknown"));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(AggregationNotFoundException.class);
        assertThat(throwable).hasMessage(String.format("Aggregation %s not found in collection %s", TestEntity.class.getName(), COLLECTION_NAME));
    }

    @Test
    void should_be_able_to_load_optional() {
        mongoTemplate.save(entity, COLLECTION_NAME);
        Optional<TestEntity> optional = mongoTestEntityRepository.loadOptional(MOCK_ID);
        assertThat(optional).hasValue(entity);
    }

    @Test
    void should_be_able_to_load_empty_optional() {
        Optional<TestEntity> empty = mongoTestEntityRepository.loadOptional(MOCK_ID);

        assertThat(empty).isEmpty();
    }

    @Test
    void should_be_able_to_list_entities_correctly() {
        mongoTemplate.save(entity, COLLECTION_NAME);
        TestEntity entity2 = new TestEntity("id-2", "mouse");
        mongoTemplate.save(entity2, COLLECTION_NAME);

        List<TestEntity> entities = mongoTestEntityRepository.listAll();

        assertThat(entities).hasSize(2);
        assertThat(entities).containsSequence(entity, entity2);
    }

    @Test
    void should_be_able_to_list_empty_when_not_found() {
        List<TestEntity> entities = mongoTestEntityRepository.listAll();

        assertThat(entities).isEmpty();
    }

    @Test
    void should_be_able_to_list_with_query() {
        mongoTemplate.save(entity, COLLECTION_NAME);
        mongoTemplate.save(new TestEntity("id-2", "mouse"), COLLECTION_NAME);

        List<TestEntity> entities = mongoTestEntityRepository.list(query(where("name").is(LISA_SU)));

        assertThat(entities).containsOnly(entity);

        List<TestEntity> empty = mongoTestEntityRepository.list(query(where("name").is("unknown")));

        assertThat(empty).isEmpty();
    }

    @Test
    void should_be_able_to_query_Optional() {
        mongoTemplate.save(entity, COLLECTION_NAME);
        mongoTemplate.save(new TestEntity("id-2", "mouse"), COLLECTION_NAME);

        assertThat(mongoTestEntityRepository.findOptional(query(where("name").is("lisa su")))).hasValue(entity);
        assertThat(mongoTestEntityRepository.findOptional(query(where("name").is("unknown")))).isEmpty();
    }

    @SuppressWarnings("FieldMayBeFinal")
    @EqualsAndHashCode
    static class TestEntity {
        private String id;
        private String name;

        public TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Repository
    static class MongoTestEntityRepository extends MongoRepository<TestEntity, String> {

        public MongoTestEntityRepository(MongoTemplate mongoTemplate) {
            super(mongoTemplate, TestEntity.class, COLLECTION_NAME);
        }
    }
}