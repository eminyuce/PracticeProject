package com.acqu.co.excel.converter.actuator.model.specs;

public class CustomSort {
    private String field;
    private String direction;

    // Constructor
    public CustomSort(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    // Getters and setters
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
