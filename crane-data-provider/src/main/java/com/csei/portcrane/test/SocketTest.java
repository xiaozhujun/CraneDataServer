package com.csei.portcrane.test;

/**
 * Created with IntelliJ IDEA.
 * User: ThinkPad
 * Date: 14-1-14
 * Time: 下午9:58
 * To change this template use File | Settings | File Templates.
 */
import com.csei.portcrane.nioclient.NIOClient;

import java.io.IOException;
import java.util.Date;
public class SocketTest {
    private static NIOClient client=new NIOClient();
    public static void main(String[] args) {
        long count = 1000000;
        StringBuffer data = new StringBuffer("");
        //Date startTime = new Date();
        for (long j=0;j<count;j++){
            for(int i=0;i<10;i++){
                if(i==0){
                    data.append(Math.random()*300);
                }else{
                    data.append(",").append(Math.random()*300);
                }

            }
            String documentJson = "{sensors:[{sensorNum:'1',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}]}";
            try {
                client.noiclient(documentJson);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
