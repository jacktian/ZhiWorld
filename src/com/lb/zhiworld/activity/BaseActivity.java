package com.lb.zhiworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.lb.zhiworld.utils.CacheUtil;
import com.lb.zhiworld.utils.StringUtils;
import com.lb.zhiworld.widget.slidingactivity.IntentUtils;
import com.lb.zhiworld.widget.slidingactivity.SlidingActivity;

public class BaseActivity extends SlidingActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 工具类打开acitvity
	 */
	public void openActivity(Class<?> pClass) {
		openActivity(pClass, null, 0);
	}

	public void openActivityForResult(Class<?> pClass, int requestCode) {
		openActivity(pClass, null, requestCode);
	}

	/**
	 * 工具类打开acitvity,并携带参数
	 */
	public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		if (requestCode == 0) {
			IntentUtils.startPreviewActivity(this, intent, 0);
		} else {
			IntentUtils.startPreviewActivity(this, intent, requestCode);
		}
	}

	/**
	 * 设置缓存数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheStr(String key, String value) {
		if (!StringUtils.isEmpty(value)) {
			CacheUtil.get(this).put(key, value);
		}
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 */
	public String getCacheStr(String key) {
		return CacheUtil.get(this).getAsString(key);
	}

	/**
	 * 返回上一页
	 * 
	 * @param view
	 */
	public void doBack(View view) {
		onBackPressed();
	}
}
