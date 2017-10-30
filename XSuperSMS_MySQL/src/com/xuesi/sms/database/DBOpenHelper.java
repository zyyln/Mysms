package com.xuesi.sms.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, null, null, 0);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context, String name) {
		super(context, name, null, 0);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context, String name, CursorFactory factory) {
		super(context, name, factory, 0);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public DBOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
		// TODO Auto-generated method stub

	}

}
