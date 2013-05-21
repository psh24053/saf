package com.shntec.saf;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

/**
 * 应用程序入口
 * @author panshihao 
 *
 */
public class SAFApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		JPushInterface.init(this);
		JPushInterface.setDebugMode(true);
		JPushInterface.setAliasAndTags(this, "SAF_TEST", null);
	}
	
}
