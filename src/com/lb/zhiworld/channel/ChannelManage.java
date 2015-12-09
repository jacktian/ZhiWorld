package com.lb.zhiworld.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.SQLException;

import com.lb.zhiworld.bean.ChannelItem;
import com.lb.zhiworld.db.ChannelDaoImpl;
import com.lb.zhiworld.db.SQLHelper;

public class ChannelManage {
	private static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表 /默认的其他频道列表
	 */
	private static List<ChannelItem> defaultUserChannels, defaultOtherChannels;
	private ChannelDaoImpl channelDaoImpl;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "头条", 1, 1));
		defaultUserChannels.add(new ChannelItem(2, "足球", 2, 1));
		defaultUserChannels.add(new ChannelItem(3, "娱乐", 3, 1));
		defaultUserChannels.add(new ChannelItem(4, "体育", 4, 1));
		defaultUserChannels.add(new ChannelItem(5, "财经", 5, 1));
		defaultUserChannels.add(new ChannelItem(6, "科技", 6, 1));
		defaultUserChannels.add(new ChannelItem(7, "电影", 7, 1));
		defaultUserChannels.add(new ChannelItem(8, "NBA", 8, 1));
		defaultUserChannels.add(new ChannelItem(9, "数码", 9, 1));
		defaultUserChannels.add(new ChannelItem(10, "移动", 10, 1));
		defaultUserChannels.add(new ChannelItem(11, "彩票", 11, 1));
		defaultUserChannels.add(new ChannelItem(12, "教育", 12, 1));
		defaultUserChannels.add(new ChannelItem(13, "论坛", 13, 1));
		defaultUserChannels.add(new ChannelItem(14, "亲子", 14, 1));
		defaultOtherChannels.add(new ChannelItem(15, "CBA", 15, 0));
		defaultOtherChannels.add(new ChannelItem(16, "笑话", 16, 0));
		defaultOtherChannels.add(new ChannelItem(17, "汽车", 17, 0));
		defaultOtherChannels.add(new ChannelItem(18, "时尚", 18, 0));
		defaultOtherChannels.add(new ChannelItem(19, "北京", 19, 0));
		defaultOtherChannels.add(new ChannelItem(20, "军事", 20, 0));
		defaultOtherChannels.add(new ChannelItem(21, "房产", 21, 0));
		defaultOtherChannels.add(new ChannelItem(22, "游戏", 22, 0));
		defaultOtherChannels.add(new ChannelItem(23, "精选", 23, 0));
		defaultOtherChannels.add(new ChannelItem(24, "电台", 24, 0));
		defaultOtherChannels.add(new ChannelItem(25, "情感", 25, 0));
		defaultOtherChannels.add(new ChannelItem(26, "旅游", 26, 0));
		defaultOtherChannels.add(new ChannelItem(27, "手机", 27, 0));
		defaultOtherChannels.add(new ChannelItem(28, "博客", 28, 0));
		defaultOtherChannels.add(new ChannelItem(29, "社会", 29, 0));
		defaultOtherChannels.add(new ChannelItem(30, "家居", 30, 0));
		defaultOtherChannels.add(new ChannelItem(31, "暴雪", 31, 0));
	}

	private ChannelManage(SQLHelper sqlHelper) throws Exception {
		if (channelDaoImpl == null) {
			channelDaoImpl = new ChannelDaoImpl(sqlHelper.getContext());
			Integer counts = channelDaoImpl.getChannelItemCounts();
			if (counts == null || counts <= 0) {
				putChannelToDB();
			}
		}
	}

	/**
	 * 初始化频道管理类
	 * 
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static ChannelManage getInstance(SQLHelper sqlHelper)
			throws Exception {
		if (channelManage == null) {
			channelManage = new ChannelManage(sqlHelper);
		}
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDaoImpl.clearFeedTable();
	}

	/**
	 * 获取用户的频道
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	@SuppressWarnings("unchecked")
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDaoImpl.getChannelItemsList(
				SQLHelper.CHANNEL_SELECTED + " = ?", new String[] { "1" });
		if (cacheList != null && !((List<?>) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List<Map<String, String>>) cacheList;
			int count = maplist.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.CHANNEL_NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_SELECTED)));
				list.add(navigate);
			}
			return list;
		}

		return defaultUserChannels;
	}

	/**
	 * 获取其他的频道
	 * 
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	@SuppressWarnings("unchecked")
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDaoImpl.getChannelItemsList(
				SQLHelper.CHANNEL_SELECTED + " = ?", new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null
				&& !((List<Map<String, String>>) cacheList).isEmpty()) {
			List<Map<String, String>> maplist = (List<Map<String, String>>) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.CHANNEL_NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(
						SQLHelper.CHANNEL_SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if (userExist) {
			return list;
		}
		return defaultOtherChannels;
	}

	private void putChannelToDB() {
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		list.addAll(defaultUserChannels);
		list.addAll(defaultOtherChannels);
		channelDaoImpl.addChannelLists(list);
	}
}
