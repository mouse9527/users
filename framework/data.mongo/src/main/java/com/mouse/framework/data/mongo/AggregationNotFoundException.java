package com.mouse.framework.data.mongo;

// TODO: move to domain.core
public class AggregationNotFoundException extends RuntimeException {
    public AggregationNotFoundException(String aggregationName, String collectionName) {
        super(String.format("Aggregation %s not found in collection %s", aggregationName, collectionName));
    }
}
