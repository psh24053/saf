package com.shntec.saf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SAFSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;  
	public static final String DB_Name = "SAF_SQLITE_DB";
	
	public SAFSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public SAFSQLiteOpenHelper(Context context){
		super(context, DB_Name, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS saf_statistics");
		StringBuffer  var1 = new StringBuffer();
		var1.append("CREATE TABLE saf_statistics ");
		var1.append("  ( ");
		var1.append("     s_id        INTEGER PRIMARY KEY, ");
		var1.append("     s_key       VARCHAR(32), ");
		var1.append("     s_method    VARCHAR(32), ");
		var1.append("     s_starttime BIGINT(24), ");
		var1.append("     s_package    VARCHAR(255), ");
		var1.append("     s_remark    TEXT ");
		var1.append("  ) ");
		db.execSQL(var1.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
