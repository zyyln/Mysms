/**
 * <p>Title: BarcodeBindMysqlite.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: XSuper</p>
 * @author XS-PC011
 * @date 2015-9-7
 *
 */
package com.xuesi.sms.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Title: BarcodeBindMysqlite——条码绑定本地数据库
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: XSuper
 * </p>
 * 
 * @author XS-PC011
 * @date 2015-9-7
 */
public class BarcodeBindMysqlite extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "olddata_db";// 数据库的名字
	public static final int DATABASEVERSION = 1;// 版本号
	public static final String TABLE_NAME = "olddata_table";// 表名

	public static final String TAG = "MyDataBase";
	// 字段
	public static final String ID = "_id";
	public static final String ASSOSIATION_DATA = "assosiation_data";
	public static final String DATA1_DATA = "data1_data";
	public static final String DATA2_DATA = "data2_data";
	public static final String REMARK_DATA = "remark_data";

	public BarcodeBindMysqlite(Context context) {
		super(context, DATABASE_NAME, null, DATABASEVERSION);
		// 打开或新建数据库（第一次创建）获得SQLiteDataBase对象，为了读取和写入数据
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table if not exists "
				+ TABLE_NAME
				+ " (" // 调用execSQL方法创建表
				+ ID + " integer primary key autoincrement," + ASSOSIATION_DATA
				+ " varchar," + DATA1_DATA + " varchar," + DATA2_DATA
				+ " varchar," + REMARK_DATA + " varchar)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// 插入数据
	public long insertData(String[] strArray) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BarcodeBindMysqlite.ASSOSIATION_DATA, strArray[0]);
		values.put(BarcodeBindMysqlite.DATA1_DATA, strArray[1]);
		values.put(BarcodeBindMysqlite.DATA2_DATA, strArray[2]);
		values.put(BarcodeBindMysqlite.REMARK_DATA, strArray[3]);
		long count = db.insert(TABLE_NAME, null, values);
		db.close();
		return count;
	}

	// 插入指定行数据
	public long insertDataColumn(String column, String strarray) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column, strarray);
		long count = db.insert(TABLE_NAME, null, values);
		db.close();
		return count;
	}

	// 删除数据
	public void deleteData(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, BarcodeBindMysqlite.ID + "=?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	// 删除全部数据库
	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	// 修改数据
	public long upDatainfo(String[] strArray, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BarcodeBindMysqlite.ASSOSIATION_DATA, strArray[0]);
		values.put(BarcodeBindMysqlite.DATA1_DATA, strArray[1]);
		values.put(BarcodeBindMysqlite.DATA2_DATA, strArray[2]);
		values.put(BarcodeBindMysqlite.REMARK_DATA, strArray[3]);
		long count = db.update(TABLE_NAME, values, ID + "=?", new String[] { id
				+ "" });
		db.close();
		return count;

	}

	// 查询数据(按照id的降序排序)
	public Cursor searchAllData(SQLiteDatabase db, String[] strArray) {
		Cursor c = db.query(TABLE_NAME, strArray, null, null, null, null, ID
				+ " DESC");
		return c;
	}

}
