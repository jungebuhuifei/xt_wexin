package com.bjsxt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

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
