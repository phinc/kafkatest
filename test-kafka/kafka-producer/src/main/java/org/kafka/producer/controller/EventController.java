package org.kafka.producer.controller;


import java.util.concurrent.atomic.AtomicInteger;

import org.kafka.producer.entity.Event;
import org.kafka.producer.event.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/events")
public class EventController {
    
    private static final String template = "Hello, %s!";
    
    private final EventProducer eventProducer;
    
    private final AtomicInteger count = new AtomicInteger(0);
    
    
    @Autowired
    public EventController(EventProducer eventProducer) {
        super();
        this.eventProducer = eventProducer;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Event getEvent(@RequestParam(value = "id", defaultValue = "1") String id) {
        return new Event(id, String.format(template, id));
    }
    
    @RequestMapping(value="/{event}", method=RequestMethod.POST)
    public Event pushEvent(@PathVariable  String event) {
        Event res = new Event(String.valueOf(count.getAndIncrement()), event);
        eventProducer.produce(res);
        return res;
    }
}
