package com.lb.zhiworld.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public NewsFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public NewsFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (fragments.size() <= position) {
			position = position % fragments.size();
		}
		return super.instantiateItem(container, position);
	}
}
