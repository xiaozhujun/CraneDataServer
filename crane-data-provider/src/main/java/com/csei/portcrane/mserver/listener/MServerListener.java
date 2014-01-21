package com.csei.portcrane.mserver.listener;

import cn.bj.etung.tower.dcc.communicate.Dcc_client;
import cn.bj.etung.tower.dcc.communicate.Dcc_msg;
import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.domain.Message;
import com.csei.portcrane.mserver.message.StringInfoDecoder;
import com.csei.portcrane.nioserver.NIOServer;
import com.csei.portcrane.service.SensorDataService;
import com.csei.portcrane.test.SocketTest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * 用于对mserver进行监听
 * User: xiaozhujun
 * Date: 13-10-19
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */

public class MServerListener implements ServletContextListener,Runnable {
    private Thread mServerListenThread;
    private Thread socketServerListenThread;
    private String mserverDomain;
    private String mserverPort;
    private  SocketChannel mserverSocket;
    private SensorDataService sensorDataService;
    private SocketTest socketTest;

    public void contextInitialized(ServletContextEvent event) {
        /**/
        mServerListenThread =new Thread(this);
        mServerListenThread.start();

        NIOServer server = new NIOServer();
        socketServerListenThread = new Thread(server);
        socketServerListenThread.start();

    }

            //tomcat关闭时，关闭线程，释放端口
    public void contextDestroyed(ServletContextEvent event) {
        if(mserverSocket!=null)
        {
            if(mserverSocket.isOpen())
            {
                try {
                    mserverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        mServerListenThread.stop();
    }

    //是否是我们的imei号
    private boolean isOurImei(String imei){
        if(imei!=null && imei.trim().equals("240305002028307")){
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        ResourceBundle bundle = ResourceBundle.getBundle("mserver");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }
        mserverDomain =bundle.getString("mserver.domain");
        mserverPort =bundle.getString("mserver.port");
        System.out.println("mserverDomain:"+mserverDomain);
        System.out.println("mserverPort:"+mserverPort);
        mserverSocket = Dcc_client.dcc_Socket(mserverDomain, Integer.parseInt(mserverPort));
        sensorDataService = new SensorDataService();

        String msgBuffer = "";

        //数据包格式看mserver相关手册
        //一个字符串消息格式
        //|zd000001#100#zhendong|zd000002#200#zhengdong|wd000001#50#wendu|time#2013-10-19 15:28:30
        Dcc_msg msg = new Dcc_msg();
//		msg.setMsg_type((byte) 0x03);
//		msg.setMsg_body("hello".getBytes());
//		msg.setMsg_len("hello".getBytes().length);
//		msg.setImei("240305002028308");
//		Dcc_client.Dcc_msg_send(socket, msg);

        //接受数据
        msg = new Dcc_msg();
        int result;
        while(true)
        {
            try {
                result = Dcc_client.dcc_msg_recv(mserverSocket, msg);
                if(result>1){
                    System.out.println(msg.getImei()+" : "+new String(msg.getMsg_body(),"utf-8"));
                }

                if(msg!=null && msg.getMsg_body()!=null && result>1 && isOurImei(msg.getImei()))
                {
                    msgBuffer +=new String(msg.getMsg_body(),"utf-8");

                    int startIndex = msgBuffer.indexOf("{sensors:[{");
                    int endIndex = msgBuffer.indexOf("}]}",startIndex);
                    if(endIndex>0){
                       if(startIndex>=0&&endIndex>startIndex){
                           String temp = msgBuffer.substring(startIndex,endIndex+3);
                           if(temp.lastIndexOf("{sensors:[{")>1){
                              temp=temp.substring(temp.lastIndexOf("{sensors:[{"));
                           }
                           msgBuffer = msgBuffer.substring(endIndex+3);
                           System.out.println("parse: "+temp);
                           dealMessageForMongo(temp);
                       }else{
                           msgBuffer = msgBuffer.substring(endIndex+3);
                       }

                    }
                }
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    private void dealMessageForMongo(String msgBody){
        try {
            if(sensorDataService.saveMessage(msgBody)==null){
                System.out.println("ERROR:SAVE FAILED: "+msgBody);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //消息处理方法
    private void dealMessage(Dcc_msg msg)
    {
        StringInfoDecoder decoder = null;
        try {
            decoder = new StringInfoDecoder(new String(msg.getMsg_body(),"utf-8"));
            Set<Message> info = decoder.decodeAll();
            RedisConnector connector = new RedisConnector();
            connector.saveAll(info);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}

