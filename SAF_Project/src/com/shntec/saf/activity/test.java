package com.shntec.saf.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shntec.saf.SAFException;
import com.shntec.saf.SAFHTTPTransport;
import com.shntec.saf.SAFUtils;
import com.shntec.saf.onTransportProgressListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

public class test extends Activity {

	
	private int asynctaskTotal = 0;
	private long asynctaskSize = 0;
	private int executorsTotal = 0;
	private long executorsSize = 0;
	private int s = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 测试两种多线程方法，同时执行1000次下载图片所耗的时间和结果
		
		testAsyncTask();
		testExecutors();
		
		
		
	}
	/**
	 * 测试异步任务
	 */
	public void testAsyncTask(){
		
		for(int i = 0 ; i < 1000 ; i ++){
			new abc().execute("http://sc.168g.com/scew/nRFile/FileDesk/004/1709/1709-66978b.jpg");
		}
		
		
	}
	
	private class abc extends AsyncTask<String, Integer, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			SAFHTTPTransport httptransport = new SAFHTTPTransport();
			try {
				return BitmapFactory.decodeStream(httptransport.download(params[0], new onTransportProgressListener() {
					
					@Override
					public void onProgress(long readSize, long totalSize) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onComplete() {
						// TODO Auto-generated method stub
						asynctaskTotal ++;
					}
				}));
			} catch (SAFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result != null){
				asynctaskSize += result.getByteCount();
			}
			
			
			
		}
		
	}
	
	/**
	 * 测试线程池
	 */
	public void testExecutors(){
		
		// thread数的计算公式为 cpu核心 x 2 + 2 ，由于Android的不同，所以我们再加上2
		int cpuCores = SAFUtils.getCPUcores() * 2 + 2 + 2;
		
		System.out.println(cpuCores);
		
		ExecutorService executorService = Executors.newFixedThreadPool(cpuCores);
		
		for(int i = 0 ; i < 1000 ; i ++){
			executorService.execute(new Runnable() {
				@SuppressLint("NewApi")
				public void run() {
					SAFHTTPTransport httptransport = new SAFHTTPTransport();
					Bitmap bitmap = null;
					try {
						bitmap = BitmapFactory.decodeStream(httptransport.download("http://sc.168g.com/scew/nRFile/FileDesk/004/1709/1709-66978b.jpg", new onTransportProgressListener() {
							
							@Override
							public void onProgress(long readSize, long totalSize) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete() {
								// TODO Auto-generated method stub
								executorsTotal ++;
							}
						}));
					} catch (SAFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(bitmap != null){
						executorsSize += bitmap.getByteCount();
					}
					
					
				}
			});
		}
		
		
	}
	
	
	
}
