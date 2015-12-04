package com.lb.zhiworld.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lb.zhiworld.bean.ChannelItem;

@SuppressLint("DefaultLocale")
public class ChannelDaoImpl implements ChannelDao {
	private SQLHelper sqlHelper = null;

	public ChannelDaoImpl(Context context) {
		sqlHelper = new SQLHelper(context);
	}

	@Override
	public boolean addChannelItem(ChannelItem channelItem) {
		boolean flag = false;
		SQLiteDatabase db = null;
		long ret = -1;
		try {
			db = sqlHelper.getWritableDatabase();
			db.beginTransaction();
			ContentValues values = new ContentValues();
			Class<? extends ChannelItem> clazz = channelItem.getClass();
			String tableName = clazz.getSimpleName();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				String mName = method.getName();
				if (mName.startsWith("get") && !mName.startsWith("getClass")) {
					String fieldName = mName.substring(3, mName.length())
							.toLowerCase();
					Object value = method.invoke(channelItem, null);
					if (value instanceof String) {
						values.put(fieldName, (String) value);
					}
				}
			}
			ret = db.insert(tableName, null, values);
			flag = ret != -1 ? true : false;
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
		return flag;
	}

	@Override
	public boolean deleteChannelItem(String whereClause, String[] whereArgs) {
		boolean flag = false;
		int ret = -1;
		SQLiteDatabase db = null;
		try {
			db = sqlHelper.getWritableDatabase();
			db.beginTransaction();
			ret = db.delete(SQLHelper.CHANNEL_TABLE_NAME, whereClause,
					whereArgs);
			flag = ret > 0 ? true : false;
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
		return flag;
	}

	@Override
	public void updateChannelItem(ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = sqlHelper.getWritableDatabase();
			db.beginTransaction();
			String sql = "update " + SQLHelper.CHANNEL_TABLE_NAME
					+ " set selected= " + values.getAsString("selected")
					+ " where id= " + values.getAsString("id");
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public Map<String, String> getChannelItems(String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			db = sqlHelper.getReadableDatabase();
			db.beginTransaction();
			cursor = db.query(true, SQLHelper.CHANNEL_TABLE_NAME, null,
					selection, selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String col_name = cursor.getColumnName(i);
					String col_value = cursor.getString(cursor
							.getColumnIndex(col_name));
					map.put(col_name, col_value == null ? "" : col_value);
				}
			}
			db.setTransactionSuccessful();
			return map;
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					cursor.close();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	@Override
	public List<Map<String, String>> getChannelItemsList(String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			db = sqlHelper.getReadableDatabase();
			db.beginTransaction();
			cursor = db.query(false, SQLHelper.CHANNEL_TABLE_NAME, null,
					selection, selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {
					String col_name = cursor.getColumnName(i);
					String col_value = cursor.getString(cursor
							.getColumnIndex(col_name));
					map.put(col_name, col_value == null ? "" : col_value);
				}
				list.add(map);
			}
			db.setTransactionSuccessful();
			System.out.println("++++++++++++++++++++" + list);
			return list;
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					cursor.close();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public Integer getChannelItemCounts() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int counts = 0;
		try {
			db = sqlHelper.getReadableDatabase();
			db.beginTransaction();
			cursor = db.query(SQLHelper.CHANNEL_TABLE_NAME, null, null, null,
					null, null, null);
			counts = cursor.getCount();
			db.setTransactionSuccessful();
			return counts;
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					cursor.close();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
		return null;
	}

	@Override
	public void clearFeedTable() {
		String sql = "delete from " + SQLHelper.CHANNEL_TABLE_NAME;
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.execSQL(sql);
		String sql1 = "update table_sequence set seq=0 where name='"
				+ SQLHelper.CHANNEL_TABLE_NAME + "'";
		db.execSQL(sql1);
	}

	public void addChannelLists(List<ChannelItem> list) {
		SQLiteDatabase db = null;
		try {
			db = sqlHelper.getWritableDatabase();
			db.beginTransaction();
			for (ChannelItem channelItem : list) {
				long ret = -1;
				ContentValues values = new ContentValues();
				Class<? extends ChannelItem> clazz = channelItem.getClass();
				String tableName = clazz.getSimpleName();
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					String mName = method.getName();
					if (mName.startsWith("get")
							&& !mName.startsWith("getClass")) {
						String fieldName = mName.substring(3, mName.length())
								.toLowerCase();
						Object value = method.invoke(channelItem, null);
						if (value instanceof String) {
							values.put(fieldName, (String) value);
						}
					}
				}
				ret = db.insert(tableName, null, values);
				if (ret <= 0) {
					return;
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
		} finally {
			try {
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e2) {
			}
		}
	}

}
