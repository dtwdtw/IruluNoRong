package com.wf.irulu.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.common.bean.Exittion;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @描述:  数据库操作帮助类
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.db
 * @类名:DbHelper
 * @作者: 左西杰
 * @创建时间:2015-5-28 上午10:44:28
 *
 */
public class DbHelper {

	public static final String DATABASE_NAME = "irulu.db";

	private static DbHelper instance;

	private DatabaseHelper dbHelper = null;
	private SQLiteDatabase db = null;

	private DbHelper() {
		open();
	}

	public static synchronized DbHelper getInstance() {
		if (instance == null) {
			instance = new DbHelper();
		}
		return instance;
	}

	public boolean isDBOpen() {
		if (null == db) {
			return false;
		} else {
			return db.isOpen();
		}
	}
	/**
	 * 打开数据库
	 */
	public void open() {
		dbHelper = new DatabaseHelper(IruluApplication.getInstance());
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取数据库操作对象
	 */
	public SQLiteDatabase getSQLiteDatabase() {
		if (db != null) {
			return db;
		} else {
			open();
			return db;
		}
	}

	/**
	 * 返回只读的数据库实例
	 * 注意：由于底层返回的数据库实例和可写的是同一个实例，所以只能由调用者自己来保证不用于写操作。
	 * 
	 * @return 只读的数据库实例
	 */
	public SQLiteDatabase getReadOnlySQLiteDatabase() {
		if (db != null) {
			return db;
		} else {
			open();
			return db;
		}
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (null != db) {
			db.close();
		}
		if (null != dbHelper) {
			dbHelper = null;
		}

	}

	public List<Exittion> queryAll() {
		List<Exittion> exittions = null;
		Cursor cursor = null;

		try {
			cursor = db.rawQuery("select * from exition", null);
		} catch (SQLiteException e) {
			e.printStackTrace();

			createExition();
		} finally {
			if (null != cursor) {
				exittions = new ArrayList<Exittion>();
				while (cursor.moveToNext()) {
					Exittion exittion = new Exittion();
					exittion.setNo(cursor.getInt(cursor.getColumnIndex("no")));
					exittion.setCurrentPage(cursor.getString(cursor.getColumnIndex("current_page")));
					exittion.setExitPage(cursor.getString(cursor.getColumnIndex("exit_page")));
					exittion.setStatus(cursor.getInt(cursor.getColumnIndex("status")));

					exittions.add(exittion);
				}
				cursor.close();
			}
			return exittions;
		}
	}

	public void insert(int no) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("no", no);

		db.insert("exition", null, contentValues);
	}

	public void updateStatus(int no, int status) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("status", status);

		db.update("exition", contentValues, "no = ?", new String[]{String.valueOf(no)});
	}

	public void updateCurrentPage(int no, String current_page) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("current_page", current_page);

		db.update("exition", contentValues, "no = ?", new String[]{ String.valueOf(no) });
	}

	public void deleteExition(int no) {
		db.delete("person", "no = ?", new String[]{ String.valueOf(no) });
	}

	public void deleteExition(Exittion exittion) {
		deleteExition(exittion.getNo());
	}

	public void deleteExitions(List<Exittion> exittions) {
		if (null != exittions) {
			for (Exittion exittion : exittions) {
				deleteExition(exittion);
			}
		}
	}

	public void updateExitPage(int no, String exit_page) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("exit_page", exit_page);

		db.update("exition", contentValues, "no = ?", new String[]{ String.valueOf(no) });
	}

	public boolean createExition() {
		if (null != db) {
			String sql = "create table exition(no integer not null primary key, current_page text, exit_page text, status int not null default 0)";
			db.execSQL(sql);

			return true;
		}

		return false;
	}

	public class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		public void onCreate(SQLiteDatabase db) {
			db.beginTransaction();
			try {
				db.execSQL(RecentSearchDbHelper.CREATE_TABLE_SQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			db.execSQL("delete from " +UserDbHelper.TABLE_NAME);
		}
	}

}
