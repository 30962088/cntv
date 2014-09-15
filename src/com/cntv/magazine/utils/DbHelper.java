package com.cntv.magazine.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DbHelper extends  SQLiteOpenHelper{

	public DbHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
		_db.execSQL(SQL_CREATE_TABLE_listitem);

	}
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
			int _newVersion) {
		_db.execSQL("DROP TABLE IF EXISTS listitem"+ SQL_CREATE_TABLE_listitem);

	}


	public static final String SQL_CREATE_TABLE_listitem= "CREATE TABLE IF NOT EXISTS listitem "
		+ "(type INTEGER NOT NULL,"
		+ " title TEXT NOT NULL," 
		+ "ctitle TEXT NOT NULL," 
		+ "thumb TEXT NOT NULL," 
		+ "mobileid TEXT NOT NULL," 
		+ "catid TEXT NOT NULL," 
		+ "inputtime TEXT NOT NULL)";

}

