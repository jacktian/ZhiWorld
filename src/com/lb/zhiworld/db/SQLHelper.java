package com.lb.zhiworld.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "database.db";
	public static final int VERSION = 1;
	public static final String CHANNEL_TABLE_NAME = "ChannelItem";
	public static final String CHANNEL_ID = "id";
	public static final String CHANNEL_NAME = "name";
	public static final String CHANNEL_ORDERID = "orderId";
	public static final String CHANNEL_SELECTED = "selected";

	private final Context context;

	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + CHANNEL_TABLE_NAME
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + CHANNEL_ID
				+ " INTEGER, " + CHANNEL_NAME + " TEXT, " + CHANNEL_ORDERID
				+ " INTEGER, " + CHANNEL_SELECTED + " TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
