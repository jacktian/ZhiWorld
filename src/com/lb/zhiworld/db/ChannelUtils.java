package com.lb.zhiworld.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChannelUtils {
	private static ChannelUtils channelUtils;
	private SQLHelper mSqlHelper;
	private SQLiteDatabase mSqLiteDatabase;

	public ChannelUtils(Context context) {
		super();
		mSqlHelper = new SQLHelper(context);
		mSqLiteDatabase = mSqlHelper.getWritableDatabase();
	}

	public static ChannelUtils getInstance(Context context) {
		if (channelUtils == null) {
			channelUtils = new ChannelUtils(context);
		}
		return channelUtils;
	}

	// 关闭数据库
	public void close() {
		mSqlHelper.close();
		mSqlHelper = null;
		mSqLiteDatabase.close();
		mSqLiteDatabase = null;
		channelUtils = null;
	}

	/**
	 * 插入频道数据
	 * 
	 * @param values
	 */
	public void insertChannelData(ContentValues values) {
		mSqLiteDatabase.insert(SQLHelper.CHANNEL_TABLE_NAME, null, values);
	}

	/**
	 * 更新频道数据
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void updateChannelData(ContentValues values, String whereClause,
			String[] whereArgs) {
		mSqLiteDatabase.update(SQLHelper.CHANNEL_TABLE_NAME, values,
				whereClause, whereArgs);
	}

	/**
	 * 删除频道数据
	 * 
	 * @param whereClause
	 * @param whereArgs
	 */
	public void deleteChannelData(String whereClause, String[] whereArgs) {
		mSqLiteDatabase.delete(SQLHelper.CHANNEL_TABLE_NAME, whereClause,
				whereArgs);
	}

	/**
	 * 检索频道数据
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor selectChannelData(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = mSqLiteDatabase.query(SQLHelper.CHANNEL_TABLE_NAME,
				columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
}
