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
        long count = 1000000;
        StringBuffer data = new StringBuffer("");
        Date startTime = new Date();
        for (long j=0;j<count;j++){
            for(int i=0;i<500;i++){
                if(i==0){
                    data.append(Math.random()*300);
                }else{
                    data.append(",").append(Math.random()*300);
                }

            }
            String dataObjectJson="{sensors:[{sensorNum:1,dateType:'Vibration',time:'2013-11-05 10:29:24',data:[58,7,8,7,8,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58]},{sensorNum:2,dateType:'Vibration',time:'2013-11-05 10:29:24',data:[58,8,10,8,10,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58,58]},{sensorNum:3,dateType:'Route',time:'2013-11-05 10:29:24',data:[0.1,0.1]}]}";
            String documentJson = "{sensors:[{sensorNum:'zd000001',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}]}";
            SensorDataService sensorDataService = new SensorDataService();
            String result = sensorDataService.saveMessage(documentJson);
            System.out.println(j);
            data.delete(0,data.length());
        }
        Date endTime = new Date();
        System.out.println("startTime: "+startTime.getTime());
        System.out.println("endTime: "+endTime.getTime());
        System.out.println("时间差："+(endTime.getTime()-startTime.getTime()));
    }
}
