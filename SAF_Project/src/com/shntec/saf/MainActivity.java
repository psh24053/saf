package com.shntec.saf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.shntec.saf.SAFTransportProgressInputStream.onTransportProgressListener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		
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
