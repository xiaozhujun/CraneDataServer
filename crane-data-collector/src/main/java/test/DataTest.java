package test;

import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.domain.Message;
import com.csei.portcrane.domain.SensorValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaozhujun
 * Date: 13-10-30
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class DataTest {
    public static void main(String args[]){
        Message msg = new Message();
        msg.setSensor("zd000001");
        msg.setTimestamp(new Date().getTime());
        List<SensorValue> values = new ArrayList<SensorValue>();
        SensorValue value = new SensorValue("zhendong",Math.random());
        values.add(value);
        msg.setValues(values);

        RedisConnector connector = new RedisConnector();
        connector.save(msg);
        System.out.println("hello world!");
    }
}
