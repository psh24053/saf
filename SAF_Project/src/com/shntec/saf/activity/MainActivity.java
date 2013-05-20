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
import com.shntec.saf.SAFRunner;
import com.shntec.saf.SAFRunnerAdapter;
import com.shntec.saf.SAFUtils;
import com.shntec.saf.onTransportProgressListener;
import com.shntec.saf.R.id;
import com.shntec.saf.R.layout;
import com.shntec.saf.R.menu;

import android.os.AsyncTask;
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

	private int aa = 0;
	
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
		
		
		new abc().execute("");

		
	}

	private class abc extends SAFRunnerAdapter<String, Integer, Bitmap>{

		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			System.out.println("onPreExecute");
			
		}
		@Override
		public void onProgressUpdate(Integer... progress) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(progress);
			
			System.out.println("progress -> "+progress[0]);
			
		}
		@Override
		public Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			
//			SAFHTTPTransport httptransport = new SAFHTTPTransport();
//			InputStream in = null;
//			try {
//				in = httptransport.download("http://down.ruanmei.com/tweakcube3/tweakcubesetup_3.39.exe", new onTransportProgressListener() {
//					
//					@Override
//					public void onProgress(long readSize, long totalSize) {
//						// TODO Auto-generated method stub
//						int pro = (int) ((float) readSize / (float) totalSize * 100);
//						publishProgress(pro);
//					}
//					
//					@Override
//					public void onComplete() {
//						// TODO Auto-generated method stub
//						System.out.println("onComplete");
//					}
//				});
//			} catch (SAFException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			SAFUtils.readInputStreamToString(in);
			
			SAFImageCompress ic = new SAFImageCompress(new onTransportProgressListener() {
				
				@Override
				public void onProgress(long readSize, long totalSize) {
					// TODO Auto-generated method stub
					int pro = (int) ((float) readSize / (float) totalSize * 100);
					publishProgress(pro);
				}
				
				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					System.out.println("onComplete");
				}
			});
			
			try {
				return ic.HttpFixedCompress("http://img6.faloo.com/picture/0x0/0/444/444440.jpg",160,120);
			} catch (SAFException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		public void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			ImageView image = (ImageView) findViewById(R.id.image);
			image.setImageBitmap(result);
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
			Log.i("SAF", result.getWidth()+" ,"+result.getHeight());
			
			aa ++;
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
