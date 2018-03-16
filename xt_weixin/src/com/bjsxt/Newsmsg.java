package com.bjsxt;

import java.util.List;

public class Newsmsg extends BaseMsg {
  private int ArticleCount;
  private List<News> Articles;
public int getArticlecount() {
	return ArticleCount;
}
public void setArticleCount(int articlecount) {
	this.ArticleCount = articlecount;
}
public List<News> getArticle() {
	return Articles;
}
public void setArticle(List<News> article) {
	this.Articles = article;
}

}
