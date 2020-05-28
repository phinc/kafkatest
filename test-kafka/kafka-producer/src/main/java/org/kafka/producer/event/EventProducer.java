package org.kafka.producer.event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.kafka.producer.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProducer {
    
    private final Logger log = LoggerFactory.getLogger(EventProducer.class);
    
    private final ProducerFactory producerFactory;
    
    private Producer<String, Event> producer;
    
    private final String topic;

    public EventProducer(ProducerFactory producerFactory, String topic) {
        super();
        this.producerFactory= producerFactory;
        this.topic = topic;
    }
    
    @PostConstruct
    private void init() {
        this.producer = producerFactory.producer();
        this.producer.initTransactions();
    }
    
    public void produce(Event... events) {
        try {
            producer.beginTransaction();
            send(events);
            producer.commitTransaction();
        } catch (ProducerFencedException e) {
            producer.close();
        } catch (KafkaException e) {
            producer.abortTransaction();
        }
    }
    
    private void send(Event... events) {
        for (final Event event : events) {
            final ProducerRecord<String, Event> record = new ProducerRecord<>(topic, event);
            log.info("publishing = " + record);
            producer.send(record);
        }
    }

    @PreDestroy
    public void close() {
        producer.close();
    }

}
