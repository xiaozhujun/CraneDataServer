package com.csei.data;

import com.csei.portcrane.service.SensorDataService;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xiaozhujun
 * Date: 13-11-4
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
public class MogoTest {
    public static void main(String args[]){
        String data = "";
        for(int i=0;i<300;i++){
            if(i==0){
                data+=Math.random()*300;
            }else{
                data+=","+Math.random()*300;
            }

        }
        String documentJson = "{sensorNum:'zd000001',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}";
//        SensorDataService sensorDataService = new SensorDataService("sensorDB","sensorCollection");
//        String objectID = sensorDataService.insertDocument(documentJson);
//        ArrayList data =  sensorDataService.getDocumentData(objectID);
        SensorDataService sensorDataService = new SensorDataService();
        String result = sensorDataService.saveMessage(documentJson);
        System.out.println(result);
    }
}
