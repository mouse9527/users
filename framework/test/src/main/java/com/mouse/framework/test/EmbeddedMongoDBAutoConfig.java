package com.mouse.framework.test;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "application.mongodb.embedded", havingValue = "true")
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.data.mongo.MongoDatabaseFactoryDependentConfiguration")
public class EmbeddedMongoDBAutoConfig {

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return EmbeddedMongoDB.getInstance().getMongoDatabaseFactory();
    }
}
