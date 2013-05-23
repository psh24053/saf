package com.shntec.saf;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MotionEvent;

/**
 * 统计工具
 * 
 * 每个方法中需要获取
 * 1.当前的时间
 * 2.触发的事件（oncreate,ondestroy..）
 * 3.当前activity的包名
 * 4.当前activity本次使用统计所生产的key
 * 
 * 当destroy事件被触发时，将会记录的各种touch事件一同写入数据库
 * 
 * @author Panshihao
 *
 */
public class SAFStatistics {

	private static SAFStatistics safStatistics = new SAFStatistics();
	private SAFStatistics(){}
	private String key;
	private Context context;
	private SAFSQLiteOpenHelper openHelper;
	private Map<String, Integer> eventMap;
	
	/**
	 * 导出数据库为JSON对象
	 * @return JSONObject
	 * @throws SAFException 
	 */
	public static JSONObject ExportDB() throws SAFException{
		SQLiteDatabase db = safStatistics.openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from saf_statistics", null);
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		while(cursor.moveToNext()){
			JSONObject item = new JSONObject();
			try {
				item.put("s_id", cursor.getInt(0));
				item.put("s_key", cursor.getString(1));
				item.put("s_method", cursor.getString(2));
				item.put("s_starttime", cursor.getLong(3));
				item.put("s_package", cursor.getString(4));
				item.put("s_remark", cursor.getString(5));
				array.put(item);
			} catch (JSONException e) {
				throw new SAFException(0, e.getMessage(), e);
			}
		}
		try {
			json.put("array", array);
			json.put("count", array.length());
		} catch (JSONException e) {
			throw new SAFException(0, e.getMessage(), e);
		}
		cursor.close();
		db.close();
		
		return json;
	}
	/**
	 * 清理数据库
	 */
	public static void ClearDB(){
		SQLiteDatabase db = safStatistics.openHelper.getWritableDatabase();
		db.execSQL("delete from saf_statistics");
		db.close();
	}
	/**
	 * 获取一个SAFStatistics的实例，用于专门监听当前activity的各种行为事件
	 * @param context
	 * @return
	 */
	public static SAFStatistics getInstance(Context context){
		if(safStatistics.key == null){
			safStatistics.key = SAFUtils.getMD5Str(System.currentTimeMillis()+"");
		}
		safStatistics.context = context;
		safStatistics.openHelper = new SAFSQLiteOpenHelper(context);
		safStatistics.eventMap = new HashMap<String, Integer>();
		return safStatistics;
	}
	/**
	 * 当前页面的统计入口
	 */
	public void onCreate(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("s_key", key);
		values.put("s_method", "oncreate");
		values.put("s_starttime", System.currentTimeMillis());
		values.put("s_package", context.getClass().getName());
		values.put("s_remark", "");
		db.insert("saf_statistics", null, values);
		db.close();
		
	}
	/**
	 * 当前页面的统计出口
	 */
	public void onDestroy(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("s_key", key);
		values.put("s_method", "ondestroy");
		values.put("s_starttime", System.currentTimeMillis());
		values.put("s_package", context.getClass().getName());
		values.put("s_remark", eventMap.toString());
		System.out.println(eventMap.toString());
		db.insert("saf_statistics", null, values);
		db.close();
	}
	/**
	 * 当前页面被暂停时
	 */
	public void onPause(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("s_key", key);
		values.put("s_method", "onpause");
		values.put("s_starttime", System.currentTimeMillis());
		values.put("s_package", context.getClass().getName());
		values.put("s_remark", "");
		db.insert("saf_statistics", null, values);
		db.close();
	}
	/**
	 * 当前页面继续开始时
	 */
	public void onResume(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("s_key", key);
		values.put("s_method", "onresume");
		values.put("s_starttime", System.currentTimeMillis());
		values.put("s_package", context.getClass().getName());
		values.put("s_remark", "");
		db.insert("saf_statistics", null, values);
		db.close();
	}
	/**
	 * 当onDown事件被触发时
	 * @param e
	 */
	public void onDown(MotionEvent e){
		int ondown = 0;
		if(eventMap.containsKey("ondown")){
			ondown = eventMap.get("ondown");
		}
		ondown ++;
		
		eventMap.put("ondown", ondown);
	}
	/**
	 * 当onFling事件被触发时
	 * @param e1
	 * @param e2
	 * @param velocityX
	 * @param velocityY
	 */
	public void onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY){
		int onFling = 0;
		if(eventMap.containsKey("onFling")){
			onFling = eventMap.get("onFling");
		}
		onFling ++;
		
		eventMap.put("onFling", onFling);
	}
	/**
	 * 当onLongPress事件被触发时
	 * @param e
	 */
	public void onLongPress(MotionEvent e) {
		int onLongPress = 0;
		if(eventMap.containsKey("onLongPress")){
			onLongPress = eventMap.get("onLongPress");
		}
		onLongPress ++;
		
		eventMap.put("onLongPress", onLongPress);
	}
	/**
	 * 当onScroll事件被触发时
	 * @param e1
	 * @param e2
	 * @param distanceX
	 * @param distanceY
	 */
	public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		int onScroll = 0;
		if(eventMap.containsKey("onScroll")){
			onScroll = eventMap.get("onScroll");
		}
		onScroll ++;
		
		eventMap.put("onScroll", onScroll);
	}
	/**
	 * 当onShowPress事件被触发时
	 * @param e
	 */
	public void onShowPress(MotionEvent e) {
		int onShowPress = 0;
		if(eventMap.containsKey("onShowPress")){
			onShowPress = eventMap.get("onShowPress");
		}
		onShowPress ++;
		
		eventMap.put("onShowPress", onShowPress);
	}
	/**
	 * 当onSingleTapUp事件被触发时
	 * @param e
	 */
	public void onSingleTapUp(MotionEvent e) {
		int onSingleTapUp = 0;
		if(eventMap.containsKey("onSingleTapUp")){
			onSingleTapUp = eventMap.get("onSingleTapUp");
		}
		onSingleTapUp ++;
		
		eventMap.put("onSingleTapUp", onSingleTapUp);
	}
	/**
	 * 当onDoubleTap事件被触发时
	 * @param e
	 */
	public void onDoubleTap(MotionEvent e) {
		int onDoubleTap = 0;
		if(eventMap.containsKey("onDoubleTap")){
			onDoubleTap = eventMap.get("onDoubleTap");
		}
		onDoubleTap ++;
		
		eventMap.put("onDoubleTap", onDoubleTap);
	}
	/**
	 * 当onDoubleTapEvent事件被触发时
	 * @param e
	 */
	public void onDoubleTapEvent(MotionEvent e) {
		int onDoubleTapEvent = 0;
		if(eventMap.containsKey("onDoubleTapEvent")){
			onDoubleTapEvent = eventMap.get("onDoubleTapEvent");
		}
		onDoubleTapEvent ++;
		
		eventMap.put("onDoubleTapEvent", onDoubleTapEvent);
	}
	/**
	 * 当onSingleTapConfirmed事件被触发时
	 * @param e
	 */
	public void onSingleTapConfirmed(MotionEvent e) {
		int onSingleTapConfirmed = 0;
		if(eventMap.containsKey("onSingleTapConfirmed")){
			onSingleTapConfirmed = eventMap.get("onSingleTapConfirmed");
		}
		onSingleTapConfirmed ++;
		
		eventMap.put("onSingleTapConfirmed", onSingleTapConfirmed);
	}
}
