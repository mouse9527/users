package com.mouse.framework.test;

import com.github.silaev.mongodb.replicaset.MongoDbReplicaSet;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/*
 * Open a replica set mongodb in docker
 *
 * <b>Must call {@link EmbeddedMongoDB#stop()} when the end of use</b>
 */
public final class EmbeddedMongoDB {
    private static final Object LOCK = new Object();
    private static EmbeddedMongoDB instance;
    private final MongoDbReplicaSet mongoDbReplicaSet;

    private EmbeddedMongoDB() {
        mongoDbReplicaSet = MongoDbReplicaSet.builder()
                .mongoDockerImageName("mongo:4.4.0")
                .build();
        mongoDbReplicaSet.start();
        mongoDbReplicaSet.waitForMaster();
    }

    public static EmbeddedMongoDB getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new EmbeddedMongoDB();
                }
            }
        }
        return instance;
    }

    public void stop() {
        mongoDbReplicaSet.stop();
        EmbeddedMongoDB.instance = null;
    }

    public String getReplicaSetUrl() {
        return mongoDbReplicaSet.getReplicaSetUrl();
    }

    public MongoDatabaseFactory getMongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(getReplicaSetUrl());
    }
}
