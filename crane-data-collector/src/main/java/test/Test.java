package test;

import test.client.DataSendingClient;
import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.server.DataAcquisitionServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Test {
	public static void main(String[] args) {
		try {
//            RedisConnector conn = new RedisConnector();
			InetAddress address = InetAddress.getByName("127.0.0.1");
//			DataAcquisitionServer server = new DataAcquisitionServer(address);
//			new Thread(server).start();
			DataSendingClient client1 = new DataSendingClient(new InetSocketAddress(address, 8090));
			new Thread(client1).start();
            String msg1,msg2,msg3,msg4;
            msg1 = "|zd000001#1#zhendong|zd000002#2#zhengdong|wd000001#3#wendu|time#2013-08-15 13:24:60";
            client1.send(msg1);
            Thread.sleep(2000);
            msg2 = "|zd000001#4#zhendong|zd000002#5#zhengdong|wd000001#6#wendu|time#2013-08-15 13:25:60";
            client1.send(msg2);
            Thread.sleep(2000);
            msg3 = "|zd000001#7#zhendong|zd000002#8#zhengdong|wd000001#9#wendu|time#2013-08-15 13:26:60";
            client1.send(msg3);
            Thread.sleep(2000);
            msg4 = "|zd000001#10#zhendong|zd000002#11#zhengdong|wd000001#12#wendu|time#2013-08-15 13:27:60";
            client1.send(msg4);
            Thread.sleep(2000);
            msg1 = "|zd000001#1#zhendong|zd000002#2#zhengdong|wd000001#3#wendu|time#2013-08-16 13:24:60";
            client1.send(msg1);
            Thread.sleep(2000);
            msg2 = "|zd000001#4#zhendong|zd000002#5#zhengdong|wd000001#6#wendu|time#2013-08-16 13:25:60";
            client1.send(msg2);
            Thread.sleep(2000);
            msg3 = "|zd000001#7#zhendong|zd000002#8#zhengdong|wd000001#9#wendu|time#2013-08-16 13:26:60";
            client1.send(msg3);
            Thread.sleep(2000);
            msg4 = "|zd000001#10#zhendong|zd000002#11#zhengdong|wd000001#12#wendu|time#2013-08-16 13:27:60";
            client1.send(msg4);
            Thread.sleep(2000);
            msg1 = "|zd000001#1#zhendong|zd000002#2#zhengdong|wd000001#3#wendu|time#2013-08-17 13:24:60";
            client1.send(msg1);
            Thread.sleep(2000);
            msg2 = "|zd000001#4#zhendong|zd000002#5#zhengdong|wd000001#6#wendu|time#2013-08-17 13:25:60";
            client1.send(msg2);
            Thread.sleep(2000);
            msg3 = "|zd000001#7#zhendong|zd000002#8#zhengdong|wd000001#9#wendu|time#2013-08-17 13:26:60";
            client1.send(msg3);
            Thread.sleep(2000);
            msg4 = "|zd000001#10#zhendong|zd000002#11#zhengdong|wd000001#12#wendu|time#2013-08-17 13:27:60";
            client1.send(msg4);
            Thread.sleep(2000);
            msg1 = "|zd000001#1#zhendong|zd000002#2#zhengdong|wd000001#3#wendu|time#2013-08-18 13:24:60";
            client1.send(msg1);
            Thread.sleep(2000);
            msg2 = "|zd000001#4#zhendong|zd000002#5#zhengdong|wd000001#6#wendu|time#2013-08-18 13:25:60";
            client1.send(msg2);
            Thread.sleep(2000);
            msg3 = "|zd000001#7#zhendong|zd000002#8#zhengdong|wd000001#9#wendu|time#2013-08-18 13:26:60";
            client1.send(msg3);
            Thread.sleep(2000);
            msg4 = "|zd000001#10#zhendong|zd000002#11#zhengdong|wd000001#12#wendu|time#2013-08-18 13:27:60";
            client1.send(msg4);
            Thread.sleep(2000);
            msg1 = "|zd000001#1#zhendong|zd000002#2#zhengdong|wd000001#3#wendu|time#2013-08-19 13:24:60";
            client1.send(msg1);
            Thread.sleep(2000);
            msg2 = "|zd000001#4#zhendong|zd000002#5#zhengdong|wd000001#6#wendu|time#2013-08-19 13:25:60";
            client1.send(msg2);
            Thread.sleep(2000);
            msg3 = "|zd000001#7#zhendong|zd000002#8#zhengdong|wd000001#9#wendu|time#2013-08-19 13:26:60";
            client1.send(msg3);
            Thread.sleep(2000);
            msg4 = "|zd000001#10#zhendong|zd000002#11#zhengdong|wd000001#12#wendu|time#2013-08-19 13:27:60";
            client1.send(msg4);
            Thread.sleep(2000);
//            System.out.println(conn.getMsgBySensor("zd000001"));
//            System.out.println(conn.getMsgBySensor("zd000002"));
//            System.out.println(conn.getMsgBySensor("wd000001"));
//			conn.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
