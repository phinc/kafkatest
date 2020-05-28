package org.kafka.producer.event;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.kafka.producer.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventDeserializer implements Deserializer<Event> {
    
    private final Logger log = LoggerFactory.getLogger(EventDeserializer.class);

    @Override
    public Event deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, Event.class);
        } catch (IOException e) {
            log.error("Failed to read event. {}", e.getMessage());
            throw new SerializationException("Could not deserialize event", e);
        }
    }

}
