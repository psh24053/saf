package com.shntec.saf;

import cn.jpush.android.api.InstrumentedActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * 基础activity，用于实现各种功能，为开发提供便利
 * @author Panshihao
 *
 */
public class SAFBaseActivity extends InstrumentedActivity {

	/**
	 * 返回对象本身，在内部匿名类中很有用
	 * @return
	 */
	public Context This(){
		return this;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SAFStatistics.onDestroy(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SAFStatistics.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SAFStatistics.onResume(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SAFStatistics.onCreate(this);
	}
}
