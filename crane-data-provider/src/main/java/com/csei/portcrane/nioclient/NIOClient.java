package com.csei.portcrane.nioclient;

/**
 * Created with IntelliJ IDEA.
 * User: ThinkPad
 * Date: 14-1-14
 * Time: 下午9:56
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
public class NIOClient {
    /*标识数字*/
    private  int flag = 0;
    /*缓冲区大小*/
    private  int BLOCK = 4096*10;
    /*接受数据缓冲区*/
    private  ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲区*/
    private  ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
    /*服务器端地址*/
    private SocketChannel client;
    private SelectionKey selectionKey;
    private Selector selector;
    private boolean canWrite = false;

    private InetSocketAddress SERVER_ADDRESS;
    /*public static void main(String[] args) throws IOException {  */
    public NIOClient(){
        try{
            SERVER_ADDRESS = new InetSocketAddress(
                    "192.168.100.38", 9092);
            // TODO Auto-generated method stub
            // 打开socket通道
            SocketChannel socketChannel= SocketChannel.open();
            // 设置为非阻塞方式
            socketChannel.configureBlocking(false);
            // 打开选择器
            selector = Selector.open();
            // 注册连接服务端socket动作
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            // 连接
            socketChannel.connect(SERVER_ADDRESS);
            // 分配缓冲区大小内存
            Set<SelectionKey> selectionKeys;
            Iterator<SelectionKey> iterator;


            String receiveText;
            /*String sendText;  */
            int count=0;

            while (true) {
                //选择一组键，其相应的通道已为 I/O 操作准备就绪。
                //此方法执行处于阻塞模式的选择操作。
                selector.select();
                //返回此选择器的已选择键集。
                selectionKeys = selector.selectedKeys();
                //System.out.println(selectionKeys.size());
                iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    if (selectionKey.isConnectable()) {
                        System.out.println("client connect");
                        client = (SocketChannel) selectionKey.channel();
                        // 判断此通道上是否正在进行连接操作。
                        // 完成套接字通道的连接过程。
                        if (client.isConnectionPending()) {
                            client.finishConnect();
                            System.out.println("完成连接!");
                            sendbuffer.clear();
                            sendbuffer.put("Hello,Server".getBytes());
                            sendbuffer.flip();
                            client.write(sendbuffer);
                        }
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        client = (SocketChannel) selectionKey.channel();
                        //将缓冲区清空以备下次读取
                        receivebuffer.clear();
                        //读取服务器发送来的数据到缓冲区中
                        count=client.read(receivebuffer);
                        if(count>0){
                            receiveText = new String( receivebuffer.array(),0,count);
                            //System.out.println("客户端接受服务器端数据--:"+receiveText);
                            client.register(selector, SelectionKey.OP_WRITE);
                        }
                    } else if (selectionKey.isWritable()) {
                        canWrite = true;
                        break;
                    }
                }
                selectionKeys.clear();
                if(canWrite){
                    break;
                }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(String sendText){
        try{
            if(canWrite){
                sendbuffer.clear();
                client = (SocketChannel) selectionKey.channel();
                    /*sendText = "message from client--" + (flag++);  */
                //sendText="{sensors:[{sensorNum:'1',dataType:'振动',time:"+new Date().getTime()+",data:["+data+"]}]}";;
                sendbuffer.put(sendText.getBytes());
                //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                Buffer flip = sendbuffer.flip();
                client.write(sendbuffer);
                System.out.println("客户端向服务器端发送数据--："+sendText);
                client.register(selector, SelectionKey.OP_READ);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        NIOClient client=new NIOClient();
		/*client.noiclient();*/
    }
}
