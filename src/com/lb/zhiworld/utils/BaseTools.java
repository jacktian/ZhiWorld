package com.lb.zhiworld.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {
	/**
	 * 获取屏幕宽度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getWindowWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
