package com.csei.portcrane.domain;

public class SensorValue {
    private String type;
    private double value;

    public SensorValue(String type, double value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "[类型:" + type + ",值:" + value + "]";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
