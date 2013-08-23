package test.client;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DeviceInfoEncoder {
	private String cardId;
	private int riskRange;
	private int riskScore;
	private String applyPlace;
	private String type;
	private String checkDate;

	public DeviceInfoEncoder(String cardId, int riskRange, int riskScore, String applyPlace, String type, String checkDate) {
		this.cardId = cardId;
		this.riskRange = riskRange;
		this.riskScore = riskScore;
		this.applyPlace = applyPlace;
		this.type = type;
		this.checkDate = checkDate;
	}

	public ByteBuffer encode() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(stringToFixedBytes(cardId, 16));
		buffer.putInt(riskRange);
		buffer.putInt(riskScore);
		buffer.put(stringToFixedBytes(applyPlace, 64));
		buffer.put(stringToFixedBytes(type, 32));
		buffer.put(stringToFixedBytes(checkDate, 10));
		buffer.flip();
		return buffer;
	}

	// 将String放入长度为length的字节数组,不够补空白,超出部分舍弃
	public byte[] stringToFixedBytes(String s, int length) {
		return Arrays.copyOf(s.getBytes(), length);
	}
}
