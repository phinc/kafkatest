package org.kafka.producer.event;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.kafka.producer.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventSerializer implements Serializer<Event> {
    
    private final Logger log = LoggerFactory.getLogger(EventSerializer.class);

    @Override
    public byte[] serialize(String topic, Event event) {
        try {
            if (event == null)
                return null;
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(event);
            
        } catch (Exception e) {
            log.error("Could not serialize event: " + e.getMessage());
            throw new SerializationException("Could not serialize event", e);
        }
    }

}
