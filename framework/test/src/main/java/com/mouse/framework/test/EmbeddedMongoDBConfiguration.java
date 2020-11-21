package com.mouse.framework.test;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
@EnableAutoConfiguration
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.data.mongo.MongoDatabaseFactoryDependentConfiguration")
public class EmbeddedMongoDBConfiguration {

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(EmbeddedMongoDB embeddedMongoDB) {
        return embeddedMongoDB.getMongoDatabaseFactory();
    }

    @Bean(destroyMethod = "stop")
    public EmbeddedMongoDB embeddedMongoDB() {
        return EmbeddedMongoDB.getInstance();
    }
}
