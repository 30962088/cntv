package com.cntv.magazine.entity;

public class NewsGralleryItem {
	private int id;//id号
	private int drawableId;//图片资源索引
	private String NewsText;//文字说明
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
	public String getNewsText() {
		return NewsText;
	}
	public void setNewsText(String newsText) {
		NewsText = newsText;
	}

}
