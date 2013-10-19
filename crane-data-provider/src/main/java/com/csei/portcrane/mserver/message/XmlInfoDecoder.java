package com.csei.portcrane.mserver.message;

import com.csei.portcrane.domain.Message;
import com.csei.portcrane.domain.SensorValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XmlInfoDecoder{
	private String msg;

	public XmlInfoDecoder(String msg) {
		this.msg = msg.trim();
	}

	public Message decode() {
		Message message = new Message();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			// 如果用builder.parse(String)的话会产生异常
			document = builder.parse(new InputSource(new ByteArrayInputStream(msg.getBytes())));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e){
            e.printStackTrace();
        }
		Element root = document.getDocumentElement();
		List<SensorValue> list = new ArrayList<SensorValue>();

		// 遍历的第一种方法
		for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
			if ("sensor".equals(node.getNodeName())) {
				message.setSensor(node.getFirstChild().getNodeValue());
			} else if ("timestamp".equals(node.getNodeName())) {
				message.setTimestamp(Long.parseLong(node.getFirstChild().getNodeValue()));
			} else if ("value".equals(node.getNodeName())) {
				list.add(new SensorValue(node.getAttributes().getNamedItem("type").getNodeValue(), Double.valueOf(node.getFirstChild().getNodeValue())));
			}
		}

		// 遍历的第二种方法
		// NodeList nodes = root.getChildNodes();
		// for (int i = 0; i < nodes.getLength(); i++) {
		// Node node = nodes.item(i);
		// if ("sensor".equals(node.getNodeName())) {
		// message.setSensor(node.getFirstChild().getNodeValue());
		// } else if ("timestamp".equals(node.getNodeName())) {
		// message.setTimestamp(Long.parseLong(node.getFirstChild().getNodeValue()));
		// } else if ("value".equals(node.getNodeName())) {
		// list.add(new
		// SensorValue(node.getAttributes().getNamedItem("type").getNodeValue(),
		// node.getFirstChild().getNodeValue()));
		// }
		// }
		message.setValues(list);
		return message;
	}

	// // 从buffer中读取字符串,length为读取字节的个数.
	// public String getString(int length) throws IOException {
	// byte[] stringBytes = new byte[length];
	// buffer.get(stringBytes);
	// String result = (new String(stringBytes).trim());
	// return result;
	// }
	//
	// // 从buffer中读取日期,length为读取字节的个数.
	// public Date getDate(int length) throws IOException {
	// String dateString = getString(length);
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// Date date = null;
	// try {
	// date = format.parse(dateString);
	// } catch (ParseException e) {
	// System.out.println("日期格式不对");
	// e.printStackTrace();
	// }
	// return date;
	// }

}
