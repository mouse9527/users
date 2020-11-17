package com.mouse.framework.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Serializer {
    private static final ObjectMapper OBJECT_MAPPER;
    private static final Logger LOGGER = LogManager.getLogger(Serializer.class);

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    static byte[] serialize(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize:", e);
            return new byte[]{};
        }
    }
}
