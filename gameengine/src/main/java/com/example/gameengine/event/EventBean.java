package com.example.gameengine.event;

import java.util.Map;

/**
 * Created by rahul on 21/2/16.
 */
public class EventBean {
    private String system;
    private String event;
    private Map<String, Object> params;
    private Integer userId = null; // represents the user who generated the event. Could be null.

    public EventBean() {
    }

    public EventBean(String system, String event, Map<String, Object> params) {
        this.system = system;
        this.event = event;
        this.params = params;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
