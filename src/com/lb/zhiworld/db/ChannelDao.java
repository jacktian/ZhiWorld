package com.lb.zhiworld.db;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;

import com.lb.zhiworld.bean.ChannelItem;

public interface ChannelDao {
	public boolean addChannelItem(ChannelItem channelItem);

	public boolean deleteChannelItem(String whereClause, String[] whereArgs);

	public void updateChannelItem(ContentValues values, String whereClause,
			String[] whereArgs);

	public Map<String, String> getChannelItems(String selection,
			String[] selectionArgs);

	public List<Map<String, String>> getChannelItemsList(String selection,
			String[] selectionArgs);

	public void clearFeedTable();
}
