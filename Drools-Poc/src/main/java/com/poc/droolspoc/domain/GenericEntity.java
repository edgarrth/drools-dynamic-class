package com.poc.droolspoc.domain;

import java.util.Map;

public class GenericEntity {
    private String type;
    private Map<String, Object> body;

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
