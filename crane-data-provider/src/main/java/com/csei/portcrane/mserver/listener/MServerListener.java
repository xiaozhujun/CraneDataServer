package com.csei.portcrane.mserver.listener;

import cn.bj.etung.tower.dcc.communicate.Dcc_client;
import cn.bj.etung.tower.dcc.communicate.Dcc_msg;
import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.domain.Message;
import com.csei.portcrane.mserver.message.StringInfoDecoder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: xiaozhujun
 * Date: 13-10-19
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */

public class MServerListener implements ServletContextListener,Runnable {
    private Thread mServerListenThread;
    private String mserverDomain;
    private String mserverPort;

public void contextInitialized(ServletContextEvent event) {

    System.out.println("mserverDomain:"+mserverDomain);
    System.out.println("mserverPort:"+mserverPort);
    mServerListenThread =new Thread(this);
    mServerListenThread.start();
}

//tomcat关闭时，关闭线程，释放端口
        public void contextDestroyed(ServletContextEvent event) {
            mServerListenThread.stop();
}
    @Override
    public void run() {
        ResourceBundle bundle = ResourceBundle.getBundle("mserver");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }
        mserverDomain =bundle.getString("mserver.domain");
        mserverPort =bundle.getString("mserver.port");
        SocketChannel socket = Dcc_client.dcc_Socket(mserverDomain, Integer.parseInt(mserverPort));

        //数据包格式看mserver相关手册
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
                result = Dcc_client.dcc_msg_recv(socket, msg);
                if(msg!=null && msg.getMsg_body()!=null && result>1)
                {
                    System.out.println(new String(msg.getMsg_body(),"utf-8"));
                    dealMessage(msg);
                }
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

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
            connector.destroy();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

