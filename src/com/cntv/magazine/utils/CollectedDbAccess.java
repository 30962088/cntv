package com.cntv.magazine.utils;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cntv.magazine.entity.ListItem;

public class CollectedDbAccess {
	public static void insertDish(Context context, ListItem listitem) {
			ContentValues newValues = new ContentValues();
			newValues.put("type", listitem.getType());
			newValues.put("title", listitem.getTitle());
			newValues.put("ctitle", listitem.getCtitle());
			newValues.put("thumb", listitem.getThumb());
			newValues.put("mobileid", listitem.getMobileid());
			newValues.put("inputtime",listitem.getInputtime());
			newValues.put("catid",listitem.getCatid());
			GetDateBase.getWriteDb(context).insert("listitem", null, newValues);
			close(context);
	}

	public static ArrayList<ListItem> query_All_ListItem(Context context,int type) {
		Cursor results = GetDateBase.getReadDb(context).query("listitem", null,"type" + "=" + type, null, null, null, null);
		ArrayList<ListItem> list = convertToDishs001(context,results);
		close(context);
		return list;
	}
	

	public static int deleteCollect(Context context,String MobileId){
		String[] whereArgs = {String.valueOf(MobileId)};
		//Log.i("zhuo", "-+++++++++++++="+whereArgs);
		int k = GetDateBase.getWriteDb(context).delete("listitem", "mobileId" + "=?" , whereArgs);
		close(context);
		return k;
	}

	/*db.delete(TABLE_GoodQuestion, KEY_G_QuestionID + "=" + questionID,
			null);*/
	private static ArrayList<ListItem> convertToDishs001(Context context,Cursor cursor) {
		/** 获得所有数据 **/
		int resultCounts = cursor.getCount();
		/** 做数据不存在时的操作 **/
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		/** 把数据封装到实体类中 **/
		ArrayList<ListItem> records = new ArrayList<ListItem>();
		for (int i = 0; i < resultCounts; i++) {
			ListItem listitem = new ListItem();
			
			
			listitem.setType(cursor.getInt(cursor
					.getColumnIndex("type")));
			listitem.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			listitem.setCtitle(cursor.getString(cursor.getColumnIndex("ctitle")));
			listitem.setCatid(cursor.getString(cursor.getColumnIndex("catid")));
			listitem.setInputtime(cursor.getString(cursor.getColumnIndex("inputtime")));
			listitem.setMobileid(cursor.getString(cursor.getColumnIndex("mobileid")));
			listitem.setThumb(cursor.getString(cursor.getColumnIndex("thumb")));
			records.add(listitem);
			cursor.moveToNext();
		}
		/** 关闭游标 **/
		cursor.close();
		close(context);
		return records;
	}

	public static void close(Context context){
		if(GetDateBase.getWriteDb(context) !=null || GetDateBase.getReadDb(context)!=null){
			GetDateBase.getWriteDb(context).close();
			GetDateBase.getReadDb(context).close();
		}
	}
}
