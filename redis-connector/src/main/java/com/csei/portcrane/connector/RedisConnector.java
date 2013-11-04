package com.csei.portcrane.connector;


import com.csei.portcrane.domain.Message;
import com.csei.portcrane.domain.SensorValue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

public class RedisConnector {
    private static int expire;
    private static JedisPool pool;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
        pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
        expire = Integer.valueOf(bundle.getString("redis.expire"));
    }

    private Jedis jedis;

    public RedisConnector() {
        jedis = pool.getResource();
    }

    public void destroy() {
        pool.returnResource(jedis);
    }

    public void save(Message msg) {
        String sensor = msg.getSensor();
        long timestamp = msg.getTimestamp();
        List<SensorValue> values = msg.getValues();
        String prefix = sensor + ":" + timestamp + ":value";
        Iterator<SensorValue> it = values.iterator();
        int i = 1;
        while (it.hasNext()) {
            String key = prefix + i;
            i++;
            SensorValue sensorValue = it.next();
            saveValue(key, sensorValue);
        }
    }

    public void saveAll(Set<Message> set){
        Iterator<Message> it = set.iterator();
        while(it.hasNext()){
            save(it.next());
        }
    }

    private void saveValue(String key, SensorValue sensorValue) {
        jedis.hset(key, "type", sensorValue.getType());
        jedis.hset(key, "value", String.valueOf(sensorValue.getValue()));
        jedis.expire(key, expire);
    }

    public Set<String> getAllSensors(){
        Set<String> result = new HashSet<String>();
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String[] ss = key.split(":");
            result.add(ss[0]);
        }
        return result;
    }

    public TreeSet<Message> getMsgBySensor(String sensor) {
        TreeSet<Message> result = new TreeSet<Message>();
        Set<String> keys = jedis.keys(sensor + ":*");
        Set<String> timestamps = new HashSet<String>();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
            String key = it.next();
            String[] ss = key.split(":");
            if (timestamps.add(ss[1])) {
                Message msg = new Message();
                msg.setSensor(sensor);
                msg.setTimestamp(Long.parseLong(ss[1]));
                List<SensorValue> list = new ArrayList<SensorValue>();
                Set<String> keys1 = jedis.keys(sensor + ":" + ss[1] + "*");
                Iterator<String> it1 = keys1.iterator();
                while (it1.hasNext()) {
                    String key1 = it1.next();
                    SensorValue sensorValue = new SensorValue(jedis.hget(key1, "type"), Double.valueOf(jedis.hget(key1, "value")));
                    list.add(sensorValue);
                }
                msg.setValues(list);
                result.add(msg);
            }
        }
        return result;
    }

    // 返回时间大于等于给定时间且小于当前时间的元素集合；如果不存在这样的元素，则返回 null。
    public SortedSet<Message> getMsgBySensorAndTime(String sensor, long timestamp){
        Message msg1 = new Message();
        msg1.setTimestamp(timestamp);
        Message msg2 = new Message();
        msg2.setTimestamp(new Date().getTime());
        TreeSet<Message> tmp = getMsgBySensor(sensor);
        SortedSet<Message> result = tmp.subSet(msg1, msg2);
        return result;
    }

    // 返回时间大于等于给定时间的最小元素；如果不存在这样的元素，则返回 null。
    public Message getAMsgBySensorAndTime(String sensor, long timestamp){
        Message msg = new Message();
        msg.setTimestamp(timestamp);
        Message result = getMsgBySensor(sensor).ceiling(msg);
        return result;
    }

    public int getExpire() {
        return expire;
    }

    public boolean set(String key,String value){
       String result = jedis.set(key,value);
       if (result.equals("OK")){
           return true;
       }
        return false;
    }

    public String get(String key){
        return jedis.get(key);
    }

}
