package org.kafka.producer.entity;

public class Event {

    private String id;
    
    private String content;

    public Event(String id, String content) {
        super();
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", content=" + content + "]";
    }
    
    
}
