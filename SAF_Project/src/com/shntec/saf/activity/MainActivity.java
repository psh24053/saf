package com.shntec.saf.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.shntec.saf.R;
import com.shntec.saf.SAFCache;
import com.shntec.saf.SAFConfig;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFHTTPTransport;
import com.shntec.saf.SAFImageCompress;
import com.shntec.saf.SAFImageViewActivity;
import com.shntec.saf.SAFLoader;
import com.shntec.saf.SAFUtils;
import com.shntec.saf.R.id;
import com.shntec.saf.R.layout;
import com.shntec.saf.R.menu;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 加载SAF
		try {
			SAFLoader.loader(this);
		} catch (SAFException e) {
			e.printStackTrace();
		}
		
		System.out.println(SAFConfig.getInstance().getConfigVersion());
		
		
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SAFHTTPTransport t = new SAFHTTPTransport();
				SAFImageCompress ic = new SAFImageCompress();
				try {
					
					final Bitmap b = ic.HttpFixedCompress("http://img6.faloo.com/picture/0x0/0/444/444440.jpg",160,120);
					
					runOnUiThread(new Runnable() {
						public void run() {
							ImageView image = (ImageView) findViewById(R.id.image);
							image.setImageBitmap(b);
							image.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(MainActivity.this, SAFImageViewActivity.class);
									intent.putExtra("imageFID", SAFUtils.getMD5Str("http://img6.faloo.com/picture/0x0/0/444/444440.jpg"));
									intent.putExtra("bigImageUrl", "http://www.sucaitianxia.com/photo/pic/201001/gefnegs48.jpg");
									startActivity(intent);
									
								}
							});
							Log.i("SAF", b.getWidth()+" ,"+b.getHeight());
						}
					});
					
					
				} catch (SAFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				
			}
		}).start();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
