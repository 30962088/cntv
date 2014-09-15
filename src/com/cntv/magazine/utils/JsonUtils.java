package com.cntv.magazine.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.entity.NewsContent;
import com.cntv.magazine.entity.PicturesContent;
import com.cntv.magazine.entity.VideosContent;
import com.google.gson.stream.JsonReader;

public class JsonUtils {
	//解析接口列表
	public ArrayList<ListItem> parseListJson(String s,String catid){
		JsonReader reader = new JsonReader(new StringReader(s));
		ArrayList<ListItem> list = new ArrayList<ListItem>();
		try {
			reader.beginObject();
			while(reader.hasNext()){
				String s1 = reader.nextName();
				if(s1.equals(catid)){
					reader.beginObject();
					while(reader.hasNext()){
						String s2 = reader.nextName();
						if(s2!=null){
							
						}
						ListItem  item = new ListItem();
						if(true){
							reader.beginObject();
							while(reader.hasNext()){
								String tagName = reader.nextName();
								
								if(tagName.equals("title")){
									item.setTitle(reader.nextString());
								}else if(tagName.equals("ctitle")){
									item.setCtitle(reader.nextString());
								}else if(tagName.equals("thumb")){
									item.setThumb(reader.nextString());
								}else if(tagName.equals("mobileid")){
									item.setMobileid(reader.nextString());
								}else if(tagName.equals("catid")){
									item.setCatid(reader.nextString());
								}else if(tagName.equals("inputtime")){
									item.setInputtime(reader.nextString());
								}
							}
							list.add(item);
							reader.endObject();
						}
					}
					reader.endObject();
				}
			}
			reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
	//解析内容接口
	public Object parseContent(String s,int type){
		try {
			JSONObject json = new JSONObject(s);
			if(type==1){
				NewsContent nc = new NewsContent();
				nc.setTitle(json.optString("title"));
				nc.setCtitle(json.optString("ctitle"));
				nc.setContent(json.optString("content"));
				nc.setInputtime(json.optString("inputtime"));
				return nc;
			}else if(type==2){
				VideosContent vc = new VideosContent();
				vc.setBigimg(json.optString("bigimg"));
				vc.setContent(json.optString("content"));
				vc.setInputtime(json.optString("inputtime"));
				vc.setPlayurl(json.optString("playurl"));
				vc.setThumb(json.optString("thumb"));
				vc.setTitle(json.optString("title"));
				return vc;
			}else if(type==3){
				PicturesContent pc = new PicturesContent();
				pc.setCtitle(json.optString("ctitle"));
				pc.setInputtime(json.optString("inputtime"));
				pc.setPicurl(json.optString("picurl"));
				pc.setTitle(json.optString("title"));
				return pc;
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	//解析分类信息接口
	public ArrayList<KindItem> parseKindList(String s){
		ArrayList<KindItem> list = new ArrayList<KindItem>();
		try {
			JSONObject result = new JSONObject(s).getJSONObject("catinfo");
			   Iterator<?> it = result.keys();
			   while(it.hasNext()){
				   String key = (String) it.next().toString();
				   JSONObject json = result.getJSONObject(key);
				   KindItem item = new KindItem();
				   item.setCatid(json.optInt("catid"));
				   item.setCatname(json.optString("catname"));
				   item.setLevel(json.optInt("level"));
				   item.setPid(json.optInt("pid"));
				   list.add(item);
			   }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0 ;i<list.size();i++){
			for(int j = 0 ;j<list.size();j++){
				int a = list.get(i).getCatid();
				int b = list.get(j).getCatid();
				if(a < b){
					KindItem ki = list.get(i);
					list.set(i, list.get(j));
					list.set(j, ki);
					
				}
			}
		}
		Log.i("hua2", "c-----------"+list);
		return list;	
	}
}
