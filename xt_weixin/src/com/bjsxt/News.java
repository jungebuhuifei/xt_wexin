package com.bjsxt;

public class News {
	//定义的名字必须与xml文件一样。（包括大小写）
  private String Title;
  private String PicUrl;
  private String Description;
  private String Url;
public String getTitle() {
	return Title;
}
public void setTitle(String title) {
	this.Title = title;
}
public String getPicUrl() {
	return PicUrl;
}
public void setPicUrl(String picUrl) {
	PicUrl = picUrl;
}
public String getDescription() {
	return Description;
}
public void setDescription(String description) {
	Description = description;
}
public String getUrl() {
	return Url;
}
public void setUrl(String url) {
	Url = url;
}
}
