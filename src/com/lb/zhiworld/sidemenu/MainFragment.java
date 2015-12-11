package com.lb.zhiworld.sidemenu;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.zhiworld.App;
import com.lb.zhiworld.R;
import com.lb.zhiworld.activity.MainActivity_;
import com.lb.zhiworld.adapter.NewsFragmentPagerAdapter;
import com.lb.zhiworld.bean.ChannelItem;
import com.lb.zhiworld.channel.ChannelManage;
import com.lb.zhiworld.fragment.EntertainmentFragment_;
import com.lb.zhiworld.fragment.FootballFragment_;
import com.lb.zhiworld.fragment.TopFragment_;
import com.lb.zhiworld.utils.BaseTools;
import com.lb.zhiworld.widget.ColumnHorizontalScrollView;
import com.special.ResideMenu.ResideMenu;

@EFragment(R.layout.fragment_reside_main)
public class MainFragment extends Fragment {
	@ViewById(R.id.mColumnHorizontalScrollView)
	ColumnHorizontalScrollView mColumnHorizontalScrollView;
	@ViewById(R.id.column_choose)
	LinearLayout mColumnChoose;
	@ViewById(R.id.channel_column)
	RelativeLayout channelColumn;
	@ViewById(R.id.add_more_columns)
	LinearLayout addMoreColumns;
	@ViewById(R.id.add_more_columns_btn)
	ImageView addMoreColumnBtn;
	@ViewById(R.id.left_shadow)
	ImageView leftShadow;
	@ViewById(R.id.right_shadow)
	ImageView rightShadow;
	@ViewById(R.id.mViewPager)
	ViewPager mViewPager;
	private ResideMenu resideMenu;
	// 屏幕宽度
	private int mScreenWidth;
	// column宽度
	private int mItemWidth;
	// 当前选中的column
	private int currentSelectedColumn = 0;
	// 用户选择新闻列表
	private List<ChannelItem> userChannelList;
	// 用户选择新闻列表对应的页面
	private ArrayList<Fragment> fragments;
	private Fragment mFragment;
	private NewsFragmentPagerAdapter mAdapter;

	@AfterInject
	void init() {
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		mScreenWidth = BaseTools.getWindowWidth(getActivity());
		mItemWidth = mScreenWidth / 7;
		userChannelList = new ArrayList<ChannelItem>();
		fragments = new ArrayList<Fragment>();
	}

	@AfterViews
	void initView() {
		resideMenu = ((MainActivity_) getActivity()).getResideMenu();
		disableCategoryScrollReside();
		initColumns();
		initFragments();
	}

	@SuppressWarnings("deprecation")
	private void initColumns() {
		try {
			userChannelList = ChannelManage.getInstance(
					App.getInstance().getSQLHelper()).getUserChannel();
			mColumnChoose.removeAllViews();
			int count = userChannelList.size();
			mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
					mColumnChoose, leftShadow, rightShadow, addMoreColumns,
					channelColumn);
			for (int i = 0; i < count; i++) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						mItemWidth, LayoutParams.WRAP_CONTENT);
				params.leftMargin = 5;
				params.rightMargin = 5;
				TextView columnTextView = new TextView(getActivity());
				columnTextView.setTextAppearance(getActivity(),
						R.style.news_category_column_item_text);
				columnTextView.setBackgroundResource(R.drawable.radio_btn_bg);
				columnTextView.setGravity(Gravity.CENTER);
				columnTextView.setPadding(5, 5, 5, 5);
				columnTextView.setId(i);
				columnTextView.setText(userChannelList.get(i).getName());
				columnTextView.setTextColor(getActivity().getResources()
						.getColorStateList(R.color.news_category_scroll_text));
				if (currentSelectedColumn == i) {
					columnTextView.setSelected(true);
				}
				columnTextView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < mColumnChoose.getChildCount(); i++) {
							if (mColumnChoose.getChildAt(i) != v) {
								mColumnChoose.getChildAt(i).setSelected(false);
							} else {
								v.setSelected(true);
								selectTab(i, false);
								mViewPager.setCurrentItem(i);
							}
						}
					}
				});
				mColumnChoose.addView(columnTextView, i, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void initFragments() {
		fragments.clear();
		for (int i = 0; i < userChannelList.size(); i++) {
			String channelName = userChannelList.get(i).getName();
			fragments.add(initFragment(channelName));
		}
		mAdapter = new NewsFragmentPagerAdapter(getChildFragmentManager(),
				fragments);
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mListener);
	}

	private Fragment initFragment(String channelName) {
		if (channelName.equals("头条")) {
			mFragment = new TopFragment_();
		} else if (channelName.equals("足球")) {
			mFragment = new FootballFragment_();
		} else if (channelName.equals("娱乐")) {
			mFragment = new EntertainmentFragment_();
		}
		return mFragment;
	}

	private void selectTab(int tab_index, boolean setTabStyle) {
		currentSelectedColumn = tab_index;
		View checkView = mColumnChoose.getChildAt(tab_index);
		int l = checkView.getMeasuredWidth();
		int k = checkView.getLeft();
		int ll = k + l / 2 - mScreenWidth / 2;
		mColumnHorizontalScrollView.smoothScrollTo(ll, 0);
		if (setTabStyle) {
			for (int i = 0; i < mColumnChoose.getChildCount(); i++) {
				View view = mColumnChoose.getChildAt(i);
				boolean isCheck = false;
				if (i == tab_index) {
					isCheck = true;
				}
				view.setSelected(isCheck);
			}
		}
	}

	private void disableCategoryScrollReside() {
		resideMenu.addIgnoredView(channelColumn);
	}

	private OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			selectTab(arg0, true);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg0 == 0) {
				resideMenu.removeIgnoredView(mViewPager);
			} else {
				resideMenu.addIgnoredView(mViewPager);
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}
