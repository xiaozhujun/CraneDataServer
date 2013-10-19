package cn.bj.etung.tower.dcc.communicate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

public class Dcc_client {
	public static final String ENCODING = "utf-8";
	static final byte g_hdr_id[] = { 0x7e, 0x7e, 0x7e, 0x7e };
	public static final int IMEI_LEN = 15;
	public static final int DTU_NAME = 15;
	public static final int MAX_MSG_LEN = 1492;
	static final int HEAD_ID_LEN = 4;
	private static final int DC_MSG_NEED_AUTH = 0x0D;
	private static final int MAX_AUTH_WAIT = 100;
	private static final int AUTH_RANDOM_LEN = 4;
	private static final byte DC_MSG_AUTH = 0x50;
	private static final int PASSWD_MD5_LEN = 16;
	private static final int USER_NAME_LEN = 16;
	private static final byte DC_MSG_AUTH_RESULT = 0x51;
	private static final byte AUTHRESULT_PASSED = 0x00;

	public static SocketChannel dcc_Socket(String dc_ip, int mServerPort) {
		// 返回SocketChannel实例，并绑定SocketAddress
		try {
			SocketAddress add = new InetSocketAddress(dc_ip, mServerPort);
			SocketChannel client = SocketChannel.open(add);
			client.configureBlocking(false);
			// client.connect(new InetSocketAddress("localhost", mServerPort));

			return client;
		} catch (Exception e) {
			return null;
		}
	}

	public static BufferedReader dcc_instream(Socket socket) throws IOException {
		BufferedReader Input = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		return Input;
	}

	public static PrintWriter dcc_getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(socketOut, true);
		return writer;

	}

	public static int dcc_msg_send(SocketChannel socket, Dcc_msg msg)
			throws IOException {
		ByteBuffer result = dcc_msg_encoder(msg);
		socket.write(result);
		return 0;
	}

	public static int dcc_msg_recv(SocketChannel socket, Dcc_msg msg)
			throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(HEAD_ID_LEN + IMEI_LEN + 1
				+ DTU_NAME + 1 + 4);
		int len = socket.read(buffer);
		if (len == -1) {
			return -1;
		}

		buffer.flip();
		if (!buffer.hasRemaining()) {
			return 0;
		} else {
			if (len != HEAD_ID_LEN + IMEI_LEN + 1 + DTU_NAME + 1 + 4) {
				return -2;
			}

			// 计算msg body的长�?
			int idx = HEAD_ID_LEN + IMEI_LEN + 1 + DTU_NAME + 1;
			//int recvlen = buffer.get(idx + 2) * 256 + buffer.get(idx + 2 + 1);
			byte[] byte_len=new byte[2];
			byte_len[0]=buffer.get(idx + 2);
			byte_len[1]=buffer.get(idx + 2 + 1);
			int recvlen=byteToInt(byte_len);
			// 按msg body的长度取出body
			ByteBuffer buff = ByteBuffer.allocate(recvlen);

			int bodylen = socket.read(buff);
			if (bodylen != recvlen) {
				return -2;
			}
			buff.flip();

			byte message[] = new byte[40 + recvlen];

			System.arraycopy(buffer.array(), 0, message, 0,
					buffer.array().length);
			System.arraycopy(buff.array(), 0, message, 40, buff.array().length);
			// buffer.clear();
			buffer.clear();
			// System.out.println("Reveice msg�?+new String(message));
			dcc_msg_decoder(message, msg);
		}

		return len;
	}
	
	private static int byteToInt(byte[] bytes) {
		// TODO Auto-generated method stub
		int num = bytes[1] & 0xFF;
		num |= ((bytes[0] << 8) & 0xFF00);
		return num;
	}

	public static int dcc_close(SocketChannel sock) throws IOException {
		sock.close();
		return 0;
	}

	public static ByteBuffer dcc_msg_encoder(Dcc_msg msg) throws IOException {
		// 包头
		ByteBuffer byteBuf = ByteBuffer.allocate(msg.getMsg_len() + 40);
		byteBuf.put(g_hdr_id);

		// 16位imei
		byteBuf.put(msg.getImei().getBytes(Dcc_client.ENCODING));
		byte empty = 0x00;
		byteBuf.put(empty);

		// 16位name
		byte[] names = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		byteBuf.put(names);

		// �?��type
		byteBuf.put(msg.getMsg_type());

		// �?��保留�?
		byteBuf.put(msg.getReserved());

		// 2位msg len
		byte hDate = (byte) (msg.getMsg_len() / 256);
		byte lDate = (byte) (msg.getMsg_len() % 256);
		byteBuf.put(hDate);
		byteBuf.put(lDate);

		// msg body
		if (byteBuf.hasRemaining()) {
			byteBuf.put(msg.getMsg_body());
		}
		byteBuf.flip();
		return byteBuf;
	}

	public static void dcc_msg_decoder(byte ptr[], Dcc_msg msg)
			throws IOException {
		int index = g_hdr_id.length;
		// imei
		byte[] Imeichar = new byte[IMEI_LEN];
		System.arraycopy(ptr, index, Imeichar, 0, IMEI_LEN);
		msg.setImei(new String(Imeichar));
		index += IMEI_LEN + 1;

		// name
		byte[] Namechar = new byte[DTU_NAME];
		System.arraycopy(ptr, index, Namechar, 0, DTU_NAME);
		msg.setName(new String(Namechar));
		index += DTU_NAME + 1;

		// msg type
		msg.setMsg_type(ptr[index]);
		index += 1;

		// reserved
		msg.setReserved(ptr[index]);
		index += 1;

		// length
		//int len = ptr[index] * 256 + ptr[index + 1];
		int h = 0xff & ptr[index];
		int l = 0xff & ptr[index +1];
		int len = (h <<8) | l;
		
		msg.setMsg_len((short) len);
		index += 2;

		// msg body
		byte[] Bodychar = new byte[len];
		System.arraycopy(ptr, index, Bodychar, 0, msg.getMsg_len());
		msg.setMsg_body(Bodychar);
		index += msg.getMsg_len();
	}

	public static int dcc_msg_send_auth(SocketChannel socket, String username,
			String password) throws InterruptedException, IOException,
			NoSuchAlgorithmException {
		int i, ret;
		Dcc_msg msg = new Dcc_msg();
		byte[] pass = new byte[AUTH_RANDOM_LEN + password.length()];
		// 从mServer接收认证许可
		for (i = 0; i < 100; i++) {
			ret = dcc_msg_recv(socket, msg);
			if (ret < 0)
				return -1;
			else if (ret == 0)
				Thread.sleep(100);
			else if (msg.getMsg_type() != DC_MSG_NEED_AUTH)
				continue;
			else
				break;
		}
		if (i == MAX_AUTH_WAIT)
			return -1;
		else if (msg.getMsg_type() != DC_MSG_NEED_AUTH
				|| msg.getMsg_len() < AUTH_RANDOM_LEN)
			return -1;
		// 连接随机数和密码
		System.arraycopy(msg.getMsg_body(), 0, pass, 0, AUTH_RANDOM_LEN);
		System.arraycopy(password.getBytes(), 0, pass, AUTH_RANDOM_LEN,
				password.length());
		// 连接用户名和密码的MD5校验
		byte[] bodybuf = new byte[32];
		int len = username.length() > (USER_NAME_LEN - 1) ? (USER_NAME_LEN - 1)
				: username.length();
		System.arraycopy(username.getBytes(), 0, bodybuf, 0, len);
		// 获取MD5校验
		java.security.MessageDigest md = java.security.MessageDigest
				.getInstance("MD5");
		md.update(pass);
		byte pass_MD5[] = md.digest();

		System.arraycopy(pass_MD5, 0, bodybuf, USER_NAME_LEN, pass_MD5.length);

		msg.setMsg_type(DC_MSG_AUTH);
		msg.setMsg_len(USER_NAME_LEN + PASSWD_MD5_LEN);
		msg.setMsg_body(bodybuf);
		dcc_msg_send(socket, msg);

		for (i = 0; i < 100; i++) {
			ret = dcc_msg_recv(socket, msg);
			if (ret < 0)
				return -1;
			else if (ret == 0)
				Thread.sleep(100);
			else if (msg.getMsg_type() != DC_MSG_AUTH_RESULT)
				continue;
			else
				break;
		}

		if (i == MAX_AUTH_WAIT)
			return -1;
		if (msg.getMsg_type() == DC_MSG_AUTH_RESULT
				&& msg.getMsg_body()[0] == AUTHRESULT_PASSED)
			return 0;
		else
			return -1;

	}

}
