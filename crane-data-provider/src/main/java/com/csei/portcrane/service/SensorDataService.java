package com.csei.portcrane.service;

import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.mongodb.connector.MongoConnector;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: xiaozhujun
 * Date: 13-11-4
 * Time: 上午9:19
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SensorDataService {
    private String sensorDB;
    private String sensorCollection;
    private RedisConnector redisConnector;
    private MongoConnector mongoConnector;
    private ResourceBundle resourceBundle;

    //构造函数
    public SensorDataService(){
        resourceBundle = ResourceBundle.getBundle("mongo");
        if (resourceBundle == null) {
            throw new IllegalArgumentException("[mongo.properties] is not found!");
        }
        sensorDB = resourceBundle.getString("mongo.sensorDB");
        sensorCollection = resourceBundle.getString("mongo.sensorCollection");

        redisConnector = new RedisConnector();
        mongoConnector = new MongoConnector(sensorDB,sensorCollection);
    }

    //保存消息对象
    public String saveMessage(String msg){
        DBObject dbObject = (DBObject) JSON.parse(msg);
        String sensor = dbObject.get(resourceBundle.getString("mongo.field.sensor.id")).toString();
        //long timestamp = new Date().getTime();

        String objectID = mongoConnector.insertDocument(msg);
        if(objectID!=null){
            if(redisConnector.set(sensor, objectID)){
                return objectID;
            }
        }
        return null;
    }

    //获得传感器当前最新的
    public DBObject getCurrentSensorData(String sensor){
        String mongoObjectID = redisConnector.get(sensor);
        if(mongoObjectID==null){
            return null;
        }
        return mongoConnector.getDocument(mongoObjectID);
    }

    //获得传感器当前数据数组
    public ArrayList getCurrentSensorDataArray(String sensor){
        DBObject dbObject = getCurrentSensorData(sensor);
        return (ArrayList)dbObject.get(resourceBundle.getString("mongo.field.sensor.data"));
    }
}