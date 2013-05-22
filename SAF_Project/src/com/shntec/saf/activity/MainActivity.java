package com.shntec.saf.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.InstrumentedActivity;

import com.shntec.saf.R;
import com.shntec.saf.SAFBaseActivity;
import com.shntec.saf.SAFCache;
import com.shntec.saf.SAFConfig;
import com.shntec.saf.SAFException;
import com.shntec.saf.SAFHTTP;
import com.shntec.saf.SAFHTTPTransport;
import com.shntec.saf.SAFImageCompress;
import com.shntec.saf.SAFImageViewActivity;
import com.shntec.saf.SAFLoader;
import com.shntec.saf.SAFRunner;
import com.shntec.saf.SAFRunnerAdapter;
import com.shntec.saf.SAFUpdateManager;
import com.shntec.saf.SAFUtils;
import com.shntec.saf.SAFWebBrowserActivity;
import com.shntec.saf.onTransportProgressListener;
import com.shntec.saf.R.id;
import com.shntec.saf.R.layout;
import com.shntec.saf.R.menu;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends SAFBaseActivity {

	private int aa = 0;
	SAFUpdateManager safupdatemanager = null;
	ProgressBar bar = null;
	Button button = null;
	Tencent mTencent = null;
	
	String access_token = null;
	String pay_token = null;
	String openID = null;
	String expires_in = null;
	String appID = "100450590";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(mTencent != null){
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
		
		if(resultCode == 200){
			Toast.makeText(this, "登陆QQ成功！", 500).show();
			mTencent = Tencent.createInstance(appID, this.getApplicationContext());
			mTencent.setAccessToken(access_token, expires_in);
			mTencent.setOpenId(openID);
			
			SAFRunner.execute(new Runnable() {
				public void run() {
					Bundle bundle = new Bundle();
					JSONObject json = mTencent.request(Constants.GRAPH_SIMPLE_USER_INFO, bundle, Constants.HTTP_GET);
					System.out.println(json.toString());
				}
			});
		}
//		mTencent.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		SAFWebBrowserActivity.webViewListener = new SAFWebBrowserActivity.WebViewListener() {
			
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, String url) {
				// TODO Auto-generated method stub
				System.out.println(url);
				
				if(url.contains("#access_token")){
					String params = url.substring(url.indexOf("?")+2);
					
					String[] strArray = params.split("&");
					
					for(int i = 0 ; i < strArray.length ; i ++){
						String str = strArray[i];
						
						String[] item = str.split("=");
						
						String key = item[0];
						String value = item[1];
						
						if(key.equals("access_token")){
							access_token = value;
						}
						if(key.equals("pay_token")){
							pay_token = value;
						}
						if(key.equals("expires_in")){
							expires_in = String.valueOf(System.currentTimeMillis() + Long.parseLong(value) * 1000);
						}
					}
					
					if(access_token != null){
						SAFRunner.execute(new Runnable() {
							public void run() {
								SAFHTTP http = new SAFHTTP();
								try {
									openID = http.GETtoString("https://openmobile.qq.com/oauth2.0/m_me?access_token="+access_token);
									openID = openID.substring(openID.indexOf("{"), openID.lastIndexOf("}")+1);
									openID = new JSONObject(openID).getString("openid");
								} catch (SAFException e) {
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								System.out.println("openID -> "+openID);
								System.out.println("access_token -> "+access_token);
								System.out.println("pay_token -> "+pay_token);
								((Activity)view.getContext()).setResult(200);
								((Activity)view.getContext()).finish();
								
							}
						});
						return true;
					}
					
					
				}
				

				
				view.loadUrl(url);
				return true;
			}
		};
		
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SAFWebBrowserActivity.class);
				intent.putExtra("url", "https://openmobile.qq.com/oauth2.0/m_authorize?response_type=token&client_id=100450590&state=abc&redirect_uri=http%3a%2f%2fwww.panshihao.cn");
				startActivityForResult(intent, 100);
				
				
//				String SCOPE = "get_simple_userinfo,add_topic";
//				
//				
//				mTencent = Tencent.createInstance("100450590", MainActivity.this.getApplicationContext());	
//				mTencent.login(MainActivity.this, SCOPE, new IUiListener() {
//					
//					@Override
//					public void onError(UiError arg0) {
//						// TODO Auto-generated method stub
//						System.out.println("onError -> "+arg0.toString());
//					}
//					
//					@Override
//					public void onComplete(JSONObject arg0) {
//						// TODO Auto-generated method stub
//						System.out.println("onComplete -> "+arg0.toString());
//					}
//					
//					@Override
//					public void onCancel() {
//						// TODO Auto-generated method stub
//						System.out.println("onCancel");
//					}
//				});
				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		bar = (ProgressBar) findViewById(R.id.progressBar1bb);
//		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						try {
//							safupdatemanager = SAFUpdateManager.getInstance(MainActivity.this);
//							JSONObject newverison = safupdatemanager.checkNewVersion();
//							
//							System.out.println(newverison.toString());
//							if(!newverison.getBoolean("res")){
//								return;
//							}
//							
//							// 如果远程md5与本地md5相同则直接开始安装，否则开始下载
//							if(!safupdatemanager.checkLocalAPK(newverison.getString("md5"))){
//								System.out.println("local not exists!");
//								SAFHTTPTransport httptransport = new SAFHTTPTransport();
//								safupdatemanager.writeLocal(httptransport.download(newverison.getString("apkurl"), new onTransportProgressListener() {
//									
//									private int p = 0;
//									
//									@Override
//									public void onProgress(long readSize, long totalSize) {
//										// TODO Auto-generated method stub
//										int pro = (int) ((float) readSize / (float) totalSize * 100);
//										if(p != pro){
//											p = pro;
//											runOnUiThread(new Runnable() {
//												public void run() {
//													bar.setProgress(p);
//												}
//											});
//											System.out.println(pro);
//										}
//										
//										
//									}
//									
//									@Override
//									public void onComplete() {
//										// TODO Auto-generated method stub
//										System.out.println("onComplete");
//										safupdatemanager.install();
//									}
//								}));
//							}else{
//								System.out.println("local exists!");
//								safupdatemanager.install();
//							}
//							
//							
//							
//							
//						} catch (SAFException e) {
//							e.printStackTrace();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}).start();
//			}
//		});
		
		
		
		
//		new abc().execute("");

		
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
