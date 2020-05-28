package org.kafka.producer;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kafka.producer.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    private final static String KAFKA_PROPERTY_PREFIX = "kafka";

    @Autowired
    private PropertyUtil propertyUtil;

    @Test(timeout = 1000L)
    public void testPropertyUtilWithNullPrefix() {

        assertNotNull(propertyUtil);
        assertThat(propertyUtil).isNotNull();

        Map<String, Object> props = propertyUtil.getProperties(null);
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties();
    }

    @Test
    public void testPropertyUtilWithKafkaPrefix() {

        assertNotNull(propertyUtil);
        assertThat(propertyUtil).isNotNull();

        Map<String, Object> props = propertyUtil.getProperties(KAFKA_PROPERTY_PREFIX);
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties().hasKeySatisfying(startsWithKafkaPrefix);
    }

    Condition<String> startsWithKafkaPrefix = new Condition<String>("starts with " + KAFKA_PROPERTY_PREFIX) {
        @Override
        public boolean matches(String key) {
            return key.startsWith(KAFKA_PROPERTY_PREFIX);
        }
    };
}
