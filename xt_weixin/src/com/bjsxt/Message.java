package com.bjsxt;



import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.*;
import org.dom4j.io.SAXReader;


import com.thoughtworks.xstream.XStream;

public class Message extends HttpServlet{

	/**

	* 新建方法，将接收到的XML格式，转化为Map对象

	* @param request 将request对象，通过参数传入

	* @return 返回转换后的Map对象

	*/

	public static Map xmlToMap(HttpServletRequest request) throws IOException, DocumentException{

	Map map = new HashMap();

	//从dom4j的jar包中，拿到SAXReader对象。

	SAXReader reader = new SAXReader();

	InputStream is = request.getInputStream();//从request中，获取输入流

	Document doc = reader.read(is);//从reader对象中,读取输入流

	Element root = doc.getRootElement();//获取XML文档的根元素

	List<Element> list = root.elements();//获得根元素下的所有子节点

	for (Element e : list) {

	map.put(e.getName(), e.getText());//遍历list对象，并将结果保存到集合中

	}

	is.close();

	return map;

	}

	
	/**

	* 将文本消息对象转化成XML格式

	* @param message 文本消息对象

	* @return 返回转换后的XML格式

	*/

	public static String objectToXml(TestMessage message){

	XStream xs = new XStream();
	//由于转换后xml根节点默认为class类，需转化为
	xs.alias("xml", message.getClass());
	return xs.toXML(message);

	}
    
	
}
