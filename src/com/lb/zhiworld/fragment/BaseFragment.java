package com.lb.zhiworld.fragment;

import android.support.v4.app.Fragment;

import com.lb.zhiworld.activity.BaseActivity;

public class BaseFragment extends Fragment {
	// 当前页码
	public int currentPage = 1;

	public BaseActivity getBaseActivity() {
		return (BaseActivity) getActivity();
	}
}
