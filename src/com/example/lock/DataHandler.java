package com.example.lock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Idb.db";
    public static final String TABLE_NAME1 = "AppName";
    public static final String TABLE_NAME2 = "Image";
    public static final String NAME = "Name";
    public static final String IMAGE = "Image";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String TABLE_CREATE1 = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME1 + " ("+NAME +" VARCHAR PRIMARY KEY)";
    public static final String TABLE_CREATE2 = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME2 + " ( "+NAME + " VARCHAR PRIMARY KEY, "+ IMAGE+ " BLOB , " +X+" int, "+ Y +" int)";
    
    Context context;
    DataBaseHelper dbhelper;
    SQLiteDatabase db;
    public DataHandler(Context context){
    	this.context = context;
    	dbhelper = new DataBaseHelper(context);
    }
    
    public static class DataBaseHelper extends SQLiteOpenHelper{

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try{
				
			db.execSQL(TABLE_CREATE1);
			db.execSQL(TABLE_CREATE2);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
			onCreate(db);
			
		}
    }

    public DataHandler open()
	{
		db = dbhelper.getWritableDatabase();
		return this;
	}
    
    public void close()
    {
    	dbhelper.close();
    }
    
    public long insert(String name, byte[] img,int x,int y)
    {
    	ContentValues values = new ContentValues();
    	values.put(NAME, name);
    	values.put(IMAGE, img);
    	values.put(X, x);
    	values.put(Y, y);
    	long newRowId;
    	newRowId = db.insert(TABLE_NAME2,null,values);
    	return newRowId;
    }
    
    public long insert(String name)
    {
    	ContentValues values = new ContentValues();
    	values.put(NAME, name);
    	long newRowId;
    	newRowId = db.insert(TABLE_NAME1,null,values);
    	return newRowId;
    }
    
    public Cursor returnDataImg(){
    	return db.query(TABLE_NAME2, new String[]{NAME,IMAGE,X,Y},null , null, null, null, null);
    }
    
    public Cursor returnData(){
    	return db.query(TABLE_NAME1, new String[]{NAME},null , null, null, null, null);
    }
}
