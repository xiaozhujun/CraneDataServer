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
        for(int i=0;i<500;i++){
            if(i==0){
                data+=Math.random()*300;
            }else{
                data+=","+Math.random()*300;
            }

        }
        String dataObjectJson="{sensors:[{sensorNum:1,dateType:'Vibration',time:'2013-11-05 10:29:24',data:[58,7,8,7,8,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58]},{sensorNum:2,dateType:'Vibration',time:'2013-11-05 10:29:24',data:[58,8,10,8,10,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58]},{sensorNum:3,dateType:'Route',time:'2013-11-05 10:29:24',data:[0.1,0.1]}]}";
        String documentJson = "{sensorNum:'zd000001',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}";
//        SensorDataService sensorDataService = new SensorDataService("sensorDB","sensorCollection");
//        String objectID = sensorDataService.insertDocument(documentJson);
//        ArrayList data =  sensorDataService.getDocumentData(objectID);
        SensorDataService sensorDataService = new SensorDataService();
        String result = sensorDataService.saveMessage(dataObjectJson);
        System.out.println(result);
    }
}
