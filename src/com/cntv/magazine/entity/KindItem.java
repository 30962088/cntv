package com.cntv.magazine.entity;

public class KindItem {
	private int catid;//����ID
	private int pid;//��������ID
	private int level;//����㼶��1Ϊ��1����2Ϊ��2�����Դ�����
	private String catname;//��������
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
