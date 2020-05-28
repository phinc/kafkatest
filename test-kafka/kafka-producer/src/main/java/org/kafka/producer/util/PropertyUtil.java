package org.kafka.producer.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:*-config.properties")
public class PropertyUtil {
    
    private final Environment environment;
    
    public PropertyUtil(Environment environment) {
        this.environment = environment;
    }

    
    public Map<String, Object> getProperties(String prefix) {
        final Map<String, Object> properties = new HashMap<>();
        final List<org.springframework.core.env.PropertySource<?>> propertySources = ((AbstractEnvironment) environment).getPropertySources()
                .stream()
                .collect(Collectors.toList());
        Collections.reverse(propertySources);
        for (org.springframework.core.env.PropertySource<?> propertySource : propertySources) {
            if (propertySource instanceof EnumerablePropertySource) {
                for (String key : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                    if (key.startsWith(prefix)) {
                        properties.put(key, propertySource.getProperty(key));
                    }
                }
            }
        }
        return properties;
    }
}
