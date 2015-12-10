package com.lb.zhiworld.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lb.zhiworld.R;
import com.lb.zhiworld.fragment.MainFragment_;
import com.lb.zhiworld.fragment.PictureFragment_;
import com.lb.zhiworld.fragment.VideoFragment_;
import com.lb.zhiworld.fragment.WeatherFragment_;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {
	private ResideMenu resideMenu;
	private MainActivity mContext;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemPicture;
	private ResideMenuItem itemVideo;
	private ResideMenuItem itemWeather;

	@AfterInject
	void init() {
		mContext = this;
	}

	@AfterViews
	void initView() {
		setUpMenu();
		changeFragment(new MainFragment_());
	}

	@SuppressWarnings("deprecation")
	private void setUpMenu() {
		resideMenu = new ResideMenu(mContext);
		resideMenu.setBackground(R.drawable.pc_menu_bg);
		resideMenu.attachToActivity(mContext);
		resideMenu.setMenuListener(menuListener);
		resideMenu.setScaleValue(0.6f);

		itemHome = new ResideMenuItem(mContext, R.drawable.icon_home,
				R.string.reside_main);
		itemPicture = new ResideMenuItem(mContext, R.drawable.icon_pics,
				R.string.reside_pics);
		itemVideo = new ResideMenuItem(mContext, R.drawable.icon_video,
				R.string.reside_video);
		itemWeather = new ResideMenuItem(mContext, R.drawable.icon_weather,
				R.string.reside_weather);

		itemHome.setOnClickListener(this);
		itemPicture.setOnClickListener(this);
		itemVideo.setOnClickListener(this);
		itemWeather.setOnClickListener(this);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemPicture, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemVideo, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemWeather, ResideMenu.DIRECTION_LEFT);

		resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {

		@Override
		public void openMenu() {

		}

		@Override
		public void closeMenu() {

		}
	};

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	};

	@Override
	public void onClick(View v) {
		if (v == itemHome) {
			changeFragment(new MainFragment_());
		} else if (v == itemPicture) {
			changeFragment(new PictureFragment_());
		} else if (v == itemVideo) {
			changeFragment(new VideoFragment_());
		} else if (v == itemWeather) {
			changeFragment(new WeatherFragment_());
		}
		resideMenu.closeMenu();
	}

	public ResideMenu getResideMenu() {
		return resideMenu;
	}
}
