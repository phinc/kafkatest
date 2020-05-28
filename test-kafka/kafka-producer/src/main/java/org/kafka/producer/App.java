package org.kafka.producer;

import org.kafka.producer.event.EventProducer;
import org.kafka.producer.event.ProducerFactory;
import org.kafka.producer.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:kafka-config.properties")
@SpringBootApplication
public class App {
    
    @Autowired
    private Environment environment;
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    public PropertyUtil propertyUtil() {
        return new PropertyUtil(environment);
    }
    
    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory(propertyUtil());
    }
    
    @Bean
    public EventProducer eventProducer() {
        return new EventProducer(producerFactory(), getTopic());
    }
    
    public String getTopic() {
        return environment.getProperty("kafka.events.topic");
    }
}
