package com.csei.portcrane.domain;

import java.util.Iterator;
import java.util.List;

public class Message implements Comparable<Message> {
    private String sensor;
    private long timestamp;
    private List<SensorValue> values;

    public Message() {}

    public String toString() {
        String result = "传感器" + sensor + ",时间:" + timestamp;
        Iterator<SensorValue> it = values.iterator();
        int i = 1;
        while(it.hasNext()){
            SensorValue sensorValue = it.next();
            result += ",值" + i + ":" + sensorValue;
            i++;
        }
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public List<SensorValue> getValues() {
        return values;
    }

    public void setValues(List<SensorValue> values) {
        this.values = values;
    }

    public int compareTo(Message msg) {
        if (this.timestamp == msg.getTimestamp()) {
            return 0;
        } else if (this.timestamp > msg.getTimestamp()) {
            return 1;
        } else {
            return -1;
        }

    }
}

