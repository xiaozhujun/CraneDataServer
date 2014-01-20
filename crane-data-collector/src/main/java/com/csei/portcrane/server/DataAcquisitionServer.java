package com.csei.portcrane.server;



import com.csei.portcrane.connector.RedisConnector;
import com.csei.portcrane.domain.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class DataAcquisitionServer implements Runnable {
	private int port = 8090;
	private Selector selector;
	private StringInfoDecoder decoder;

	public DataAcquisitionServer(InetAddress ip) throws IOException {
        System.out.println("server:server started!");
		selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel serverChannel = SelectorProvider.provider().openServerSocketChannel();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(ip, port));
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void run() {
		while (true) {
//			System.out.println("server: wait for receiving...");
			try {
				int selectedNum = selector.selectNow();
				if (selectedNum != 0) {
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey selectedKey = it.next();
						it.remove();
						if (selectedKey.isValid()) {
							if (selectedKey.isAcceptable()) {
								System.out.println("server: request accept!");
								ServerSocketChannel serverChannel = (ServerSocketChannel) selectedKey.channel();
								SocketChannel socketChannel = serverChannel.accept();
								socketChannel.configureBlocking(false);
								socketChannel.register(selector, SelectionKey.OP_READ);
							} else if (selectedKey.isReadable()) {
								System.out.println("server: receiving begin!");
								SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								socketChannel.read(buffer);
								String msg = new String(buffer.array());
                                System.out.println(msg);
								decoder = new StringInfoDecoder(msg);
								Set<Message> info = decoder.decodeAll();
								RedisConnector connector = new RedisConnector();
								connector.saveAll(info);
								System.out.println("info:"+info);
                                socketChannel.close();
								System.out.println("server: receiving end...");
							} else if (selectedKey.isWritable()) {
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}
}
