package org.kafka.producer.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.kafka.producer.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerFactory {
    
    private final Logger log = LoggerFactory.getLogger(ProducerFactory.class);
    
    private final static String KAFKA_PROPERTY_PREFIX = "kafka";

    private final PropertyUtil propertyUtil;
    
    public ProducerFactory (PropertyUtil propertyUtil) {
        this.propertyUtil = propertyUtil;
    }
    
    public <K, T> Producer<K, T> producer() {
        Map<String, Object> properties = propertyUtil.getProperties(KAFKA_PROPERTY_PREFIX);
        Map<String, Object> kafkaProperties = removeKeyPrefix(properties);
        kafkaProperties.put("transactional.id", UUID.randomUUID().toString());
        Producer<K, T> producer = new KafkaProducer<>(kafkaProperties);
        return producer;
    }
    
    private Map<String, Object> removeKeyPrefix(Map<String, Object> properties) {
        Map<String, Object> results = new HashMap<>();
        String prefix = KAFKA_PROPERTY_PREFIX + ".";
        properties.forEach((k, v) -> results.put(removePrefix(k, prefix), v));
        return results;
    }
    
    private String removePrefix(String key, String prefix) {
        log.info("Remove prefix from key {}", key);
        return key.startsWith(prefix) ? key.substring(prefix.length()) : key;
    }
}
