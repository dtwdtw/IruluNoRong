package com.wf.irulu.logic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.wf.irulu.logic.IruluController;

import java.util.ArrayList;

/**
 * 
 * @描述: 最近搜索数据库帮助类
 * 
 * @项目名: irulu
 * @包名:com.wf.irulu.logic.db
 * @类名:RecentSearchDbHelper
 * @作者: Yuki
 * @创建时间:2015-7-10 上午11:03:02
 * 
 */
public class RecentSearchDbHelper {

	public static final String _ID = "_id";
	public static final String KEYWORD = "keyword";
	public static final String TIME = "time";
	public static final String TABLE_NAME = "recent_search";
	private IruluController controller = null;
	private DbHelper dbHelper = null;
	public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + "," + KEYWORD
			+ " TEXT " + "," + TIME + " INTEGER )";
	public static final String QUERY_KEYWORD_BY_TIME = "SELECT * FROM "
			+ TABLE_NAME + " ORDER BY " + TIME + " DESC";

	public RecentSearchDbHelper(IruluController instance) {
		super();
		this.controller = instance;
		this.dbHelper = DbHelper.getInstance();
	}

	/**
	 * 增加最近搜索
	 * 
	 * @param keyword
	 */
	public synchronized void insertKeyWord(String keyword) {
		try {
			if (!TextUtils.isEmpty(keyword)) {
				SQLiteDatabase db = dbHelper.getSQLiteDatabase();
				if (queryRecentSearch(keyword)) {
					return;
				}
				ContentValues cv = getRecentSearchCv(keyword);
				db.insert(TABLE_NAME, null, cv);
//				cv.clear();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 更新当前列表
	 * @param _id
	 * @param keyword
	 */
	public synchronized void updateKeyWord(int _id,String keyword) {
		SQLiteDatabase db = dbHelper.getSQLiteDatabase();
		ContentValues cv = getRecentSearchCv(keyword);
		db.update(TABLE_NAME, cv, _ID + "= ?", new String[]{_id + ""});
	}

	/**
	 * 创建当前键值对
	 * @param keyword
	 * @return
	 */
	private ContentValues getRecentSearchCv(String keyword) {
		ContentValues cv = new ContentValues();
		cv.put(KEYWORD, keyword);
		cv.put(TIME, System.currentTimeMillis());
		return cv;
	}

	/**
	 * 查询最近搜索
	 * 
	 * @return 关键字列表
	 */
	public ArrayList<String> queryRecentSearch() {
		SQLiteDatabase db = dbHelper.getReadOnlySQLiteDatabase();
		Cursor cr = db.rawQuery(QUERY_KEYWORD_BY_TIME, null);
		ArrayList<String> item = new ArrayList<String>();
		String keyword = null;
		try {
			while (cr.moveToNext()) {
                keyword = cr.getString(cr.getColumnIndex(KEYWORD));
                item.add(keyword);
            }
			keyword = null;
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 查询最近搜索
	 * 
	 * @return 关键字列表
	 */
	public Boolean queryRecentSearch(String keyword) {
		try {
			SQLiteDatabase db = dbHelper.getSQLiteDatabase();
			Cursor cr = db.rawQuery(QUERY_KEYWORD_BY_TIME, null);
			int i = 1;
			String word = null;
			while (cr.moveToNext()) {
                word = cr.getString(cr.getColumnIndex(KEYWORD));
                if (word.equals(keyword)) {
                    // if db has update the time
                    int _id = cr.getInt(cr.getColumnIndex(_ID));
                    updateKeyWord(_id, keyword);
                    return true;
                }
                i++;
            }
			word = null;
			cr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void removeKeyWord(String keyword){
		try {
			SQLiteDatabase db = dbHelper.getSQLiteDatabase();
			db.delete(TABLE_NAME, KEYWORD + " = ?", new String[]{keyword});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清理数据
	 */
	public synchronized void clear() {
		try {
			String sql = "DELETE FROM " + TABLE_NAME;
			SQLiteDatabase db =  dbHelper.getSQLiteDatabase();
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}