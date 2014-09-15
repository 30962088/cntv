package com.cntv.magazine.entity;

public class KindItem {
	private int catid;//分类ID
	private int pid;//父级分类ID
	private int level;//分类层级，1为第1级，2为第2级，以此类推
	private String catname;//分类名称
	public int getCatid() {
		return catid;
	}
	public void setCatid(int catid) {
		this.catid = catid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCatname() {
		return catname;
	}
	public void setCatname(String catname) {
		this.catname = catname;
	}
	
	
}
