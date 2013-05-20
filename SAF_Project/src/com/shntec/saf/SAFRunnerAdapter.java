package com.shntec.saf;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public abstract class SAFRunnerAdapter<Params, Progress, Result> {

	private Params[] params;
	private Progress[] progress;
	private Result result; 
	private DataHandler handler;
	
	public static final int MSG_ONPRE = 0x000001;
	public static final int MSG_ONPOST = 0x000002;
	public static final int MSG_ONPROGRESS = 0x000003;
	
	public SAFRunnerAdapter(){
		this.handler = new DataHandler(SAFRunner.getLooper());
	}
	
	public abstract Result doInBackground(Params... params);
	public void onPreExecute(){};
	public void onPostExecute(Result result){};
	public void onProgressUpdate(Progress... progress){
		
	};
	public void execute(Params... p){
		this.params = p;
		
		handler.obtainMessage(MSG_ONPRE);
		
		
		SAFRunner.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				result = doInBackground(params);
				
				handler.obtainMessage(MSG_ONPOST, result);
				
			}
		});
	};
	public void publishProgress(Progress... pro){
		this.progress = pro;
		
		handler.obtainMessage(MSG_ONPROGRESS, pro);
		
	};
	
	/**
	 * 数据handler
	 * @author panshihao
	 *
	 */
	private class DataHandler extends Handler{
		
		public DataHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			System.out.println("handleMessage");
			
			switch (msg.what) {
			case MSG_ONPRE:
				onPreExecute();
				break;
			case MSG_ONPOST:
				onPostExecute((Result) msg.obj);
				break;
			case MSG_ONPROGRESS:
				onProgressUpdate((Progress[]) msg.obj);
				break;
			default:
				break;
			}
			
			
		}
	}
	
}
