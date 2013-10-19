package cn.bj.etung.tower.dcc.communicate;

public class Dcc_msg {
	private String imei;//[] = new char[IMEI_LEN + 1];
	private String name;//[] = new char[DTU_NAME + 1];
	private byte msg_type; /* DC_MSG_DATA, DC_MSG_ONLINE, DC_MSG_OFFLINE */
	private byte reserved;
	private int msg_len;
	private byte msg_body[];
	
	
	public static final int IMEI_LEN = 15;
	public static final int DTU_NAME = 15;
	public static final int MAX_MSG_LEN = 1492;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(byte msg_type) {
		this.msg_type = msg_type;
	}
	public byte getReserved() {
		return reserved;
	}
	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
	public int getMsg_len() {
		return msg_len;
	}
	public void setMsg_len(int msg_len) {
		this.msg_len = msg_len;
	}
	public byte[] getMsg_body() {
		return msg_body;
	}
	public void setMsg_body(byte[] msg_body) {
		this.msg_body = msg_body;
	}
	public static int getIMEI_LEN() {
		return IMEI_LEN;
	}
	public static int getDTU_NAME() {
		return DTU_NAME;
	}
	public static int getMAX_MSG_LEN() {
		return MAX_MSG_LEN;
	}

}
