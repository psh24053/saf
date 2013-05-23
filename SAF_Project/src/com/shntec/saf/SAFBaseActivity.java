package com.shntec.saf;

import cn.jpush.android.api.InstrumentedActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * 基础activity，用于实现各种功能，为开发提供便利
 * @author Panshihao
 *
 */
public class SAFBaseActivity extends InstrumentedActivity implements OnGestureListener,OnDoubleTapListener {

	
	private SAFStatistics safStatistics;
	private GestureDetector mGestureDetector;
	
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
		safStatistics.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		safStatistics.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		safStatistics.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mGestureDetector = new GestureDetector(this, this);
		safStatistics = SAFStatistics.getInstance(this);
		safStatistics.onCreate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onDown(e);
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		safStatistics.onFling(e1, e2, velocityX, velocityY);
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onLongPress(e);
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		safStatistics.onScroll(e1, e2, distanceX, distanceY);
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onShowPress(e);
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onSingleTapUp(e);
		return false;
	}
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onDoubleTap(e);
		return false;
	}
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onDoubleTapEvent(e);
		return false;
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		safStatistics.onSingleTapConfirmed(e);
		return false;
	}
	
}
