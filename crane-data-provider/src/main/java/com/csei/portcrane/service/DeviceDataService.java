package com.csei.portcrane.service;

import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.SortedSet;

@Service
public class DeviceDataService {
    @Autowired
    private RedisConnector redis;

    public Set<Message> getMsgBySensor(String id){
        return redis.getMsgBySensor(id);
    }

    public SortedSet<Message> getMsgBySensorAndTime(String sensor, long timestamp){
        return redis.getMsgBySensorAndTime(sensor,timestamp);
    }
}
