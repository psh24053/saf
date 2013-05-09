package com.shntec.saf;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		SAFHTTP safhttp = new SAFHTTP();
		try {
			String res = safhttp.GET("http://www.baidu.com");
			Log.i("SAF", "耗时 "+safhttp.getLastTime()+" 毫秒");
		} catch (SAFException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
