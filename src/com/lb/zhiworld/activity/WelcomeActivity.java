package com.lb.zhiworld.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.content.Intent;
import android.view.View;

import com.lb.zhiworld.R;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
	@Click(R.id.button1)
	void openMain(View view) {
		// openActivity(MainActivity_.class);
		Intent intent = new Intent(this, MainActivity_.class);
		startActivity(intent);
	}
}
