package com.cntv.magazine.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class GetDateBase {
	private static SQLiteDatabase db;
	private static String DATABASE_NAME="magazine.db";
	private static int DATABASE_VERSION=1;
	private static DbHelper myOpenHelper;
	
    public static SQLiteDatabase getWriteDb(Context context)
    {
		if(db==null)
		{
			Log.i("get db context", context.toString());
			myOpenHelper= new DbHelper(context, DATABASE_NAME, null,DATABASE_VERSION);
    	    db=myOpenHelper.getWritableDatabase();   
    	    
    	}else
    	{
    		if(!db.isOpen())
    		{
    			open(context);
    		}
    	}
		return db;
    }
    public static SQLiteDatabase getReadDb(Context context)
    {
		if(db==null)
		{
			Log.i("get db context", context.toString());
			myOpenHelper= new DbHelper(context, DATABASE_NAME, null,DATABASE_VERSION);
    	    db=myOpenHelper.getReadableDatabase();   	    
    	}else if(!db.isOpen())
		{
    		open(context);
		}
		return db;
    }
    /** �����ݿ� **/
	public static void open(Context context) {
		myOpenHelper = new DbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		try {
			db = myOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			ex.printStackTrace();
			db = myOpenHelper.getReadableDatabase();
		}
	}
	/**�û��������ݿ��¼��֤
	 * 
	 * �����֤�ɹ������û�ID�����򷵻�-1
	 * */
	/** Close the database */
	public static void close() {
		if (db != null) {
			if(db.isOpen())
			   db.close();
		}
	}
}
