package com.bjsxt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bjsxt.*;
import com.thoughtworks.xstream.XStream;



public class MessageUtil {
public static final String MESSAGE_TEXT = "text";
public static final String MESSAGE_IMAGE = "image";
public static final String MESSAGE_VOICE = "voice";
public static final String MESSAGE_VIDEO = "video";
public static final String MESSAGE_SHORTVIDEO = "shortvideo";
public static final String MESSAGE_LINK = "link";
public static final String MESSAGE_LOCATION = "location";
public static final String MESSAGE_EVENT = "event";
public static final String MESSAGE_SUBSCRIBE = "subscribe";
public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
public static final String MESSAGE_CLICK = "CLICK";
public static final String MESSAGE_VIEW = "VIEW";
public static final String MESSAGE_SCAN = "SCAN";
public static final String MESSAGE_NEWS = "news";
private static final String APPID = "wx6a5e28110bf134da";
private static final String APPSECRET = "fa7fb235e3930a97c8587847ddb79dc8";
private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";



/**

* 将XML转为MAP集合

* @param request

* @return

* @throws IOException

* @throws DocumentException

*/

public static Map xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
	Map map = new HashMap();
	SAXReader reader = new SAXReader();
	//从request对象中获取输入流
	InputStream ins = request.getInputStream();
	//使用reader对象读取输入流,解析为XML文档
	Document doc = reader.read(ins);
	//获取XML根元素
	Element root = doc.getRootElement();
	//将根元素的所有节点，放入列表中
	List<Element> list = root.elements();
	//遍历list对象，并保存到集合中
	for (Element element : list) {
	  map.put(element.getName(), element.getText());
    }

ins.close();

return map;

}
/**

* 将文本消息对象转成XML

* @param text

* @return

*/

public static String textMessageToXml(TestMessage textMessage){

XStream xstream = new XStream();
//将xml的根节点替换成  默认为TextMessage的包名
xstream.alias("xml", textMessage.getClass());
return xstream.toXML(textMessage);

}
public static String newsMsgToXml(Newsmsg Newsmsg){

XStream xstream = new XStream();
//将xml的根节点替换成  默认为TextMessage的包名
xstream.alias("xml",Newsmsg.getClass());
//同理，将每条图文信息news类的报名，提花为item
xstream.alias("item", new News().getClass());
return xstream.toXML(Newsmsg);

}
/**

* 拼接关注主菜单

*/

public static String menuText(){

	StringBuffer sb = new StringBuffer();
	sb.append("欢迎关注该公众号，请选择:\n\n");
	sb.append("1、这个公众号很好\n");
	sb.append("2、这个公众号不好。\n\n");
	sb.append("回复？调出主菜单。\n\n");
	
	return sb.toString();

}
/**

* 初始化回复消息

*/

public static String initText(String toUSerName,String fromUserName,String content){
  TestMessage text = new TestMessage();
  text.setFromUserName(toUSerName);
  text.setToUserName(fromUserName);
  text.setMsgType(MESSAGE_TEXT);
  text.setCreateTime(new Date().getTime());
  text.setContent(content);

  return MessageUtil.textMessageToXml(text);

}

public static String initNewsMessage(String toUSerName,String fromUserName){

	List<News> newsList = new ArrayList();
  Newsmsg newsMessage=new Newsmsg();
  News newsitem=new News();
  newsitem.setTitle("图片");
  newsitem.setDescription("我的图片");
  newsitem.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521132401877&di=c3e8e4015983709dfab3f1d7b2638217&imgtype=0&src=http%3A%2F%2Fimg.sj33.cn%2Fuploads%2Fallimg%2F201205%2F20120516084851711.jpg");
  newsitem.setUrl("http://image.baidu.com");
  newsList.add(newsitem);
  //组建一条图文↓ ↓ ↓
//组装图文消息相关信息

  newsMessage.setToUserName(fromUserName);
  newsMessage.setFromUserName(toUSerName);
  newsMessage.setCreateTime(new Date().getTime());
  newsMessage.setMsgType(MESSAGE_NEWS);
  newsMessage.setArticle(newsList);
  newsMessage.setArticleCount(newsList.size());

//调用newsMessageToXml将图文消息转化为XML结构并返回

return MessageUtil.newsMsgToXml(newsMessage);
}
/**

* 编写Get请求的方法。但没有参数传递的时候，可以使用Get请求

*

* @param url 需要请求的URL

* @return 将请求URL后返回的数据，转为JSON格式，并return

*/

public static JSONObject doGetStr(String url) throws ClientProtocolException, IOException {

DefaultHttpClient client = new DefaultHttpClient();//获取DefaultHttpClient请求

HttpGet httpGet = new HttpGet(url);//HttpGet将使用Get方式发送请求URL

JSONObject jsonObject = null;

HttpResponse response = client.execute(httpGet);//使用HttpResponse接收client执行httpGet的结果

HttpEntity entity = response.getEntity();//从response中获取结果，类型为HttpEntity

if(entity != null){

String result = EntityUtils.toString(entity,"UTF-8");//HttpEntity转为字符串类型

jsonObject = JSONObject.fromObject(result);//字符串类型转为JSON类型

}

return jsonObject;

}

/**

* 编写Post请求的方法。当我们需要参数传递的时候，可以使用Post请求

*

* @param url 需要请求的URL

* @param outStr  需要传递的参数

* @return 将请求URL后返回的数据，转为JSON格式，并return

*/

public static JSONObject doPostStr(String url,String outStr) throws ClientProtocolException, IOException {

DefaultHttpClient client = new DefaultHttpClient();//获取DefaultHttpClient请求

HttpPost httpost = new HttpPost(url);//HttpPost将使用Get方式发送请求URL

JSONObject jsonObject = null;

httpost.setEntity(new StringEntity(outStr,"UTF-8"));//使用setEntity方法，将我们传进来的参数放入请求中

HttpResponse response = client.execute(httpost);//使用HttpResponse接收client执行httpost的结果

String result = EntityUtils.toString(response.getEntity(),"UTF-8");//HttpEntity转为字符串类型

jsonObject = JSONObject.fromObject(result);//字符串类型转为JSON类型

return jsonObject;

}

/**

* 获取AccessToken

* @return 返回拿到的access_token及有效期

*/

public static AccessToken getAccessToken() throws ClientProtocolException, IOException{

AccessToken token = new AccessToken();

String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);//将URL中的两个参数替换掉

JSONObject jsonObject = doGetStr(url);//使用刚刚写的doGet方法接收结果

if(jsonObject!=null){ //如果返回不为空，将返回结果封装进AccessToken实体类
String tokenString=jsonObject.getString("access_token");
token.setToken(jsonObject.getString("access_token"));//取出access_token

token.setExpiresIn(jsonObject.getInt("expires_in"));//取出access_token的有效期

}

return token;

}

}




