package com.huige.mines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * data base
 */
public class DataBase extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "minus.db";
    private SQLiteDatabase database = null;
	
	public DataBase(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String history_table_sql = "create table history (_id integer primary key autoincrement,time text,spend_time integer,type integer)" ;
		try{
			//执行创建表
			db.execSQL(history_table_sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public long insert(String time, int spend_time, int type){
		if(null==database || !database.isOpen()){
			database = getWritableDatabase();
		}
		
		ContentValues values = new ContentValues();
		values.put("time", time);
		values.put("spend_time", spend_time);
		values.put("type", type);
		long result = database.insert("history", null, values);
		
		Cursor cursor = database.query("history",
				new String[]{"_id", "time", "spend_time","type"},
				"type=?",
				new String[]{String.valueOf(type)},
				null,
				null,
				"spend_time asc, time desc");
		
		if(cursor.getCount() > 5 ){
			cursor.moveToLast();
			Log.v("count>5", ""+cursor.getString(0));
			database.delete("history", "_id=?", new String[]{cursor.getString(0)});
		}
		
		database.close();
		return result;
	}
	
	public Cursor query(){
		// TODO Auto-generated method stub 
		if(null==database || !database.isOpen()){
			database = getWritableDatabase();
		}
		
		Cursor cursor = database.query("history",
				new String[]{"_id", "time", "spend_time","type"},
				null,
				null,
				null,
				null,
				"type desc,spend_time asc, time desc",
				null);
		
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}
