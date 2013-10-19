package cn.bj.etung.tower.dcc.communicate;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException{
		SocketChannel socket = Dcc_client.dcc_Socket("www.chuankoutong.com", 9091);
		
		//数据包格式看mserver相关手册
		Dcc_msg msg = new Dcc_msg();
//		msg.setMsg_type((byte) 0x03);
//		msg.setMsg_body("hello".getBytes());
//		msg.setMsg_len("hello".getBytes().length);
//		msg.setImei("240305002028308");
//		Dcc_client.dcc_msg_send(socket, msg);
		
		
		
		//接受数据
		msg = new Dcc_msg();
		int result;
		while(true)
		{
			result = Dcc_client.dcc_msg_recv(socket, msg);
			if(msg!=null && msg.getMsg_body()!=null && result>1)
			{
				System.out.println(new String(msg.getMsg_body(),"utf-8"));
			}
			Thread.sleep(1000);
		}
		
	}
}
