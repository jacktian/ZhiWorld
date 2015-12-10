package com.lb.zhiworld.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.lb.zhiworld.bean.ChannelItem;
import com.lb.zhiworld.channel.ChannelManage;
import com.lb.zhiworld.utils.BaseTools;
import com.lb.zhiworld.widget.ColumnHorizontalScrollView;
import com.special.ResideMenu.ResideMenu;

@EFragment(R.layout.fragment_main)
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
	// 屏幕宽度
	private int mScreenWidth;
	// column宽度
	private int mItemWidth;
	// 当前选中的column
	private int currentSelectedColumn = 0;
	// 用户选择新闻列表
	private List<ChannelItem> userChannelList;

	@AfterInject
	void init() {
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		mScreenWidth = BaseTools.getWindowWidth(getActivity());
		mItemWidth = mScreenWidth / 7;
		userChannelList = new ArrayList<ChannelItem>();
	}

	@AfterViews
	void initView() {
		disableCategoryScrollReside();
		initColumns();
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

	private void disableCategoryScrollReside() {
		ResideMenu resideMenu = ((MainActivity_) getActivity()).getResideMenu();
		resideMenu.addIgnoredView(channelColumn);
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
}
