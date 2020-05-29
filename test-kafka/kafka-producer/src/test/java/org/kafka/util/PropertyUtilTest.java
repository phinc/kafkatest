package org.kafka.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kafka.producer.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {PropertyUtilTest.KAFKA_TEST_PROPERTY + "=" + PropertyUtilTest.KAFKA_TEST_PROPERTY_VALUE, 
        PropertyUtilTest.BLANK_PREFIX_TEST_PROPERTY + "=" + PropertyUtilTest.BLANK_PREFIX_TEST_PROPERTY_VALUE}, 
        classes = {PropertyUtil.class})
public class PropertyUtilTest {
    
    public final static String KAFKA_TEST_PROPERTY = "kafka.test.property";
    public final static String KAFKA_TEST_PROPERTY_VALUE = "test";
    
    public final static String BLANK_PREFIX_TEST_PROPERTY = ".test";
    public final static String BLANK_PREFIX_TEST_PROPERTY_VALUE = "test";

    private final static String KAFKA_PROPERTY_PREFIX = "kafka";
    
   
    
    @Autowired
    private PropertyUtil propertyUtil;
    
    private Condition<String> startsWithKafkaPrefix = new Condition<String>("starts with " + KAFKA_PROPERTY_PREFIX) {
        @Override
        public boolean matches(String key) {
            return key.startsWith(KAFKA_PROPERTY_PREFIX);
        }
    };
    
    private Condition<Entry<String, Object>> getMapEntryCondition(String key, String value) {
        return new Condition<Entry<String, Object>>(   
            "has property '" + key + "=" + value + "'") {

                @Override
                public boolean matches(Entry<String, Object> entry) {
                    return key.equals(entry.getKey()) && value.equals(entry.getValue().toString());
                }
        };        
    }
    

    @Test(timeout = 1000L)
    public void testPropertyUtilWithNullPrefix() {
        assertNotNull(propertyUtil);
        assertThat(propertyUtil).isNotNull();

        Map<String, Object> props = propertyUtil.getProperties(null);
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties()
            .containsEntry(BLANK_PREFIX_TEST_PROPERTY, BLANK_PREFIX_TEST_PROPERTY_VALUE)
            .containsEntry(KAFKA_TEST_PROPERTY, KAFKA_TEST_PROPERTY_VALUE);
    }
    
    @Test
    public void testPropertyUtilWithBlankPrefix() {
        Map<String, Object> props = propertyUtil.getProperties("");
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties()
            .containsEntry(BLANK_PREFIX_TEST_PROPERTY, BLANK_PREFIX_TEST_PROPERTY_VALUE)
            .containsEntry(KAFKA_TEST_PROPERTY, KAFKA_TEST_PROPERTY_VALUE);
    }

    @Test
    public void testPropertyUtilWithKafkaPrefix() {
        assertNotNull(propertyUtil);
        assertThat(propertyUtil).isNotNull();

        Map<String, Object> props = propertyUtil.getProperties(KAFKA_PROPERTY_PREFIX);
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties().hasKeySatisfying(startsWithKafkaPrefix);
    }
    
    @Test
    public void testPropertyUtilWithTestProperty() {
        Map<String, Object> props = propertyUtil.getProperties(KAFKA_PROPERTY_PREFIX);
        assertThat(props).hasEntrySatisfying(getMapEntryCondition(KAFKA_TEST_PROPERTY, KAFKA_TEST_PROPERTY_VALUE));
    }
    
    @Test
    public void testPropertyUtilWithSpacePrefix() {
        Map<String, Object> props = propertyUtil.getProperties(" ");
        assertThat(props).isNotNull().hasNoNullFieldsOrProperties()
            .containsEntry(BLANK_PREFIX_TEST_PROPERTY, BLANK_PREFIX_TEST_PROPERTY_VALUE)
            .containsEntry(KAFKA_TEST_PROPERTY, KAFKA_TEST_PROPERTY_VALUE);
    }
    
}
