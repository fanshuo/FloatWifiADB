package com.fanshuo.android.floatwifiadb;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserSetting extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	ListPreference bgListPreference;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置保存位置 不设置 则默认/data/data/you_package_name
		addPreferencesFromResource(R.xml.setting);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		boolean isOpen = prefs.getBoolean("toggle", true);
		// 开启服务
		toggleService(isOpen);
		// CheckBoxPreference组件
		CheckBoxPreference toggle = (CheckBoxPreference) findPreference("toggle");
		// 添加事件
		toggle.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0, Object newValue) {
				// 点击toggle开启服务
				toggleService((Boolean) newValue);
				return true;
			}
		});
	}

	/**
	 * 开关服务
	 */
	public void toggleService(boolean isOpen) {
		Intent intent = new Intent();
		intent.setClass(this, ViewService.class);
		if (isOpen) {
			startService(intent);
		} else {
			stopService(intent);
		}
	}

	/**
	 * 关于
	 */
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals("about")) {
			// 关于
			showDialog(R.string.about, R.string.about_body);
		}
		return false;
	}

	/**
	 * 判断服务是否运行
	 */
	public boolean isWorked() {
		ActivityManager myManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.fanshuo.android.floatwifiadb.ViewService")) {
				return true;
			}
		}
		return false;
	}

	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	private long exitTime = 0;// 记录按下时间

	/**
	 * 按两次退出
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, R.string.press_again_exit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 对话框 i资源文件 设置标题 j字符串 设置要显示的内容资源文件
	 */
	public void showDialog(int i, int j) {
		final Dialog dialog = new Dialog(this, R.style.NoTitleDialog);
		dialog.setContentView(R.layout.activity_about);
		dialog.setTitle(i);
		TextView dialogTextView = (TextView) dialog
				.findViewById(R.id.textView);
		dialogTextView.setText(j);
		Button button = (Button) dialog.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
	}

}
