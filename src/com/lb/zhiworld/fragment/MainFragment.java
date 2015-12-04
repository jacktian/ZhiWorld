package com.lb.zhiworld.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import com.lb.zhiworld.App;
import com.lb.zhiworld.R;
import com.lb.zhiworld.bean.ChannelItem;
import com.lb.zhiworld.channel.ChannelManage;
import com.lb.zhiworld.db.ChannelUtils;
import com.lb.zhiworld.utils.BaseTools;
import com.lb.zhiworld.widget.ColumnHorizontalScrollView;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
		initColumns();
	}

	private void initColumns() {
		try {
			userChannelList = ChannelManage.getInstance(
					App.getInstance().getSQLHelper()).getUserChannel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
