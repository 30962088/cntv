package com.cntv.magazine.entity;

public class Pictures_entity {
	private int id;//Í¼Æ¬Id
	private String pictures_title;//Í¼Æ¬±êÌâ
	private int pictures_smallid;//Ğ¡Í¼Æ¬Id
	private int pictures_bigid;//´óÍ¼Æ¬Id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPictures_smallid() {
		return pictures_smallid;
	}

	public void setPictures_smallid(int pictures_smallid) {
		this.pictures_smallid = pictures_smallid;
	}

	public int getPictures_bigid() {
		return pictures_bigid;
	}

	public void setPictures_bigid(int pictures_bigid) {
		this.pictures_bigid = pictures_bigid;
	}

	public String getPictures_title() {
		return pictures_title;
	}

	public void setPictures_title(String pictures_title) {
		this.pictures_title = pictures_title;
	}

}
