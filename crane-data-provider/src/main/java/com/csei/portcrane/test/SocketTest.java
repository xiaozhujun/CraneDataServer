package com.csei.portcrane.test;

/**
 * Created with IntelliJ IDEA.
 * User: ThinkPad
 * Date: 14-1-14
 * Time: 下午9:58
 * To change this template use File | Settings | File Templates.
 */
import com.csei.portcrane.nioclient.NIOClient;

import java.util.Date;
public class SocketTest {
    public static void main(String[] args) {
        NIOClient client=new NIOClient();
        long count = 1000000;
        StringBuffer data = new StringBuffer("");
        //Date startTime = new Date();
        for (long j=0;j<count;j++){
            for(int i=0;i<50;i++){
                if(i==0){
                    data.append(Math.round(Math.random()*300));
                }else{
                    data.append(",").append(Math.round(Math.random()*300));
                }

            }
            String documentJson = "{sensors:[{sensorNum:'1',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}]}";
            try {
                client.send(documentJson);
                data.setLength(0);
                Thread.sleep(2000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
