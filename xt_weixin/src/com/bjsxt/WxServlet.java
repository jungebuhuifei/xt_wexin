package com.bjsxt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;

/*接入*/
public class WxServlet extends HttpServlet{
  
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//设置字符集
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		//获取输出
		PrintWriter  out=resp.getWriter();
		//接入 调用方法 处理接入
		connect(req,out);
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		String str = null;
        String message=null;
		try {
			Map<String, String>map=Message.xmlToMap(req);
			//从集合中，获取XML各个节点的内容
			String ToUserName = map.get("ToUserName");
			String FromUserName = map.get("FromUserName");
			String CreateTime = map.get("CreateTime");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
			String MsgId = map.get("MsgId");
			
            if(MsgType.equals(MessageUtil.MESSAGE_TEXT)){
            	/*TestMessage msg=new TestMessage();
            	msg.setContent(Content);
            	msg.setCreateTime(new Date().getTime());
            	msg.setFromUserName(ToUserName);
            	msg.setToUserName(FromUserName);
            	msg.s
            	msg.setContent("您好，"etMsgType(MsgType);+FromUserName+"\n我是："+ToUserName
            			+"\n您发送的消息类型为："+MsgType+"\n您发送的时间为"+CreateTime
            			+"\n我回复的时间为："+msg.getCreateTime()+"您发送的内容是"+Content);
            			str = Message.objectToXml(msg); //调用Message工具类，将对象转为XML字符串*/
            	
              if(Content.equals("1")){
            	  message=MessageUtil.initText(ToUserName, FromUserName, "你很有眼光!!!");
              }else if(Content.equals("2")){
            	  message=MessageUtil.initText(ToUserName, FromUserName, "我会努力改善的!!!");  
              }else if(Content.equals("3")){
            	  message=MessageUtil.initNewsMessage(ToUserName, FromUserName);
              }else if(Content.equals("?")||Content.equals("？")){
            	  message=MessageUtil.initText(ToUserName, FromUserName,MessageUtil.menuText());  
              }else {
            	  message=MessageUtil.initText(ToUserName, FromUserName, "输入非法命令！！！");
              }
            }else if(MsgType.equals(MessageUtil.MESSAGE_EVENT)){//判断是否为时间类型
            	//从结合中获取是哪一种事件传入
            	String eventType=map.get("Event");
            	if(eventType.equals(MessageUtil.MESSAGE_SUBSCRIBE)){
            		message=MessageUtil.initText(ToUserName, FromUserName,MessageUtil.menuText());
            		
            	}else if(eventType.equals(MessageUtil.MESSAGE_UNSUBSCRIBE)){
            	}
            }
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(message);
		out.close();
	}
    /*
     * 接入
     * */
	private void connect(HttpServletRequest req, PrintWriter out) {
		// 获取四个参数
		String signature=req.getParameter("signature");
		String timestamp=req.getParameter("timestamp");
		String nonce    =req.getParameter("nonce");
		String echostr  =req.getParameter("echostr");
		
		//校验
		List<String> list=new ArrayList<String>();
		list.add("zlj");
		list.add(timestamp);
		list.add(nonce);
		//排序
		Collections.sort(list);
		//拼接字符串 --》shal加密
		StringBuffer buffer=new StringBuffer();
		for(String string :list){
			buffer.append(string);
		}
		//使用Apache commons codec加密
		String shalStr=DigestUtils.sha1Hex(buffer.toString());
		//对比
		boolean flag=shalStr.equals(signature);
		if(flag){
			System.out.println("接入成功");
			out.print(echostr);
			out.flush();
		}

	}
}
