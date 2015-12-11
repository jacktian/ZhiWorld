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
import com.lb.zhiworld.fragment.BLZFragment_;
import com.lb.zhiworld.fragment.BeiJingFragment_;
import com.lb.zhiworld.fragment.BlogsFragment_;
import com.lb.zhiworld.fragment.CBAFragment_;
import com.lb.zhiworld.fragment.CarFragment_;
import com.lb.zhiworld.fragment.DigitalFragment_;
import com.lb.zhiworld.fragment.EconomicFragment_;
import com.lb.zhiworld.fragment.EducationFragment_;
import com.lb.zhiworld.fragment.EmotionFragment_;
import com.lb.zhiworld.fragment.EntertainmentFragment_;
import com.lb.zhiworld.fragment.EstateFragment_;
import com.lb.zhiworld.fragment.FashionFragment_;
import com.lb.zhiworld.fragment.FootballFragment_;
import com.lb.zhiworld.fragment.ForumFragment_;
import com.lb.zhiworld.fragment.GameFragment_;
import com.lb.zhiworld.fragment.HomeFurnishingFragment_;
import com.lb.zhiworld.fragment.JokeFragment_;
import com.lb.zhiworld.fragment.LotteryFragment_;
import com.lb.zhiworld.fragment.MilitaryFragment_;
import com.lb.zhiworld.fragment.MobileFragment_;
import com.lb.zhiworld.fragment.MovieFragment_;
import com.lb.zhiworld.fragment.NBAFragment_;
import com.lb.zhiworld.fragment.ParentChildrenFragment_;
import com.lb.zhiworld.fragment.PhoneFragment_;
import com.lb.zhiworld.fragment.RadioFragment_;
import com.lb.zhiworld.fragment.SelectionFragment_;
import com.lb.zhiworld.fragment.SocietyFragment_;
import com.lb.zhiworld.fragment.SportFragment_;
import com.lb.zhiworld.fragment.TechnologyFragment_;
import com.lb.zhiworld.fragment.TopFragment_;
import com.lb.zhiworld.fragment.TravelFragment_;
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
		} else if (channelName.equals("体育")) {
			mFragment = new SportFragment_();
		} else if (channelName.equals("财经")) {
			mFragment = new EconomicFragment_();
		} else if (channelName.equals("科技")) {
			mFragment = new TechnologyFragment_();
		} else if (channelName.equals("电影")) {
			mFragment = new MovieFragment_();
		} else if (channelName.equals("NBA")) {
			mFragment = new NBAFragment_();
		} else if (channelName.equals("数码")) {
			mFragment = new DigitalFragment_();
		} else if (channelName.equals("移动")) {
			mFragment = new MobileFragment_();
		} else if (channelName.equals("彩票")) {
			mFragment = new LotteryFragment_();
		} else if (channelName.equals("教育")) {
			mFragment = new EducationFragment_();
		} else if (channelName.equals("论坛")) {
			mFragment = new ForumFragment_();
		} else if (channelName.equals("亲子")) {
			mFragment = new ParentChildrenFragment_();
		} else if (channelName.equals("CBA")) {
			mFragment = new CBAFragment_();
		} else if (channelName.equals("笑话")) {
			mFragment = new JokeFragment_();
		} else if (channelName.equals("汽车")) {
			mFragment = new CarFragment_();
		} else if (channelName.equals("时尚")) {
			mFragment = new FashionFragment_();
		} else if (channelName.equals("北京")) {
			mFragment = new BeiJingFragment_();
		} else if (channelName.equals("军事")) {
			mFragment = new MilitaryFragment_();
		} else if (channelName.equals("房产")) {
			mFragment = new EstateFragment_();
		} else if (channelName.equals("游戏")) {
			mFragment = new GameFragment_();
		} else if (channelName.equals("精选")) {
			mFragment = new SelectionFragment_();
		} else if (channelName.equals("电台")) {
			mFragment = new RadioFragment_();
		} else if (channelName.equals("情感")) {
			mFragment = new EmotionFragment_();
		} else if (channelName.equals("旅游")) {
			mFragment = new TravelFragment_();
		} else if (channelName.equals("手机")) {
			mFragment = new PhoneFragment_();
		} else if (channelName.equals("博客")) {
			mFragment = new BlogsFragment_();
		} else if (channelName.equals("社会")) {
			mFragment = new SocietyFragment_();
		} else if (channelName.equals("家居")) {
			mFragment = new HomeFurnishingFragment_();
		} else if (channelName.equals("暴雪")) {
			mFragment = new BLZFragment_();
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
			if (arg0 == 0) {
				resideMenu.removeIgnoredView(mViewPager);
			} else {
				resideMenu.removeIgnoredView(mViewPager);
				resideMenu.addIgnoredView(mViewPager);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}
