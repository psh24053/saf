package com.shntec.saf.activity;

import com.shntec.saf.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class test extends Activity {

	private ImageView imageview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_bitmapbrowser);
		
		imageview = (ImageView) findViewById(R.id.bitmap);
		
		imageview.setImageResource(R.drawable.abc);
		
		imageview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int width = arg0.getWidth();
				int height = arg0.getHeight();
				LayoutParams params = new LayoutParams(width+20, height+20);
				arg0.setLayoutParams(params);
				
			}
		});
		
		imageview.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				int width = arg0.getWidth();
				int height = arg0.getHeight();
				LayoutParams params = new LayoutParams(width-20, height-20);
				arg0.setLayoutParams(params);
				return false;
			}
		});
		
		
	}
}
