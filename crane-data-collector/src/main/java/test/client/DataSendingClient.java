package test.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class DataSendingClient implements Runnable {
	private InetSocketAddress address;
	private Selector selector;
	private String msg;

	public DataSendingClient(InetSocketAddress address) throws IOException {
		selector = SelectorProvider.provider().openSelector();
		this.address = address;
	}

	public void run() {
		System.out.println("client: started!");
		while (true) {
			try {
				int selectedNum = selector.selectNow();
				if (selectedNum != 0) {
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey selectedKey = it.next();
						it.remove();
						SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
						if (selectedKey.isValid()) {
							if (selectedKey.isConnectable()) {
								if (socketChannel.finishConnect()) {
									System.out.println("client: connected!");
									selectedKey.interestOps(SelectionKey.OP_WRITE);
								}
							} else if (selectedKey.isWritable()) {
								System.out.println("client: sending begin!");
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								buffer.put(msg.getBytes());
								buffer.flip();
								socketChannel.write(buffer);
								socketChannel.close();
								System.out.println("client: sending end!");
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String msg) throws IOException {
		this.msg = msg;
		SocketChannel socketChannel = SelectorProvider.provider().openSocketChannel();
		socketChannel.configureBlocking(false);
		socketChannel.connect(address);
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

}
