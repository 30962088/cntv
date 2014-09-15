package com.cntv.magazine.entity;

public class VideoGralleryItem {
	private int id;//id号
	private int drawableId;//图片资源索引
	private String videoText;//文字说明
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
	public String getVideoText() {
		return videoText;
	}
	public void setVideoText(String videoText) {
		this.videoText = videoText;
	}
	
	
}
