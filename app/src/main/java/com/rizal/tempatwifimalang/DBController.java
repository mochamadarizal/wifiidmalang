package com.rizal.tempatwifimalang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBController extends SQLiteOpenHelper {
	private static final String LOGCAT = null;

	public DBController(Context applicationcontext) {
        super(applicationcontext, "wifi.db", null, 1);
        Log.d(LOGCAT, "Created");
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE wifi (_ID INTEGER PRIMARY KEY AUTOINCREMENT , nama TEXT, alamat TEXT,keterangan TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT, "wifi Created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS wifi";
		database.execSQL(query);
        onCreate(database);
	}
	
	public void addWifi(HashMap<String, String> queryValues) {

		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nama", queryValues.get("nama"));
		values.put("alamat", queryValues.get("alamat"));
		values.put("keterangan", "");
		database.insert("wifi", null, values);
		database.close();
	}
	
	public int updateWifi(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
		String nama1= queryValues.get("nama1");
		values.put("nama", queryValues.get("nama"));
		values.put("alamat", queryValues.get("alamat"));
		values.put("keterangan", queryValues.get("keterangan"));

		return database.update("wifi", values, "nama" + " = ?", new String[] { queryValues.get("nama1") });
	}
	
	public void deleteWifi(String nama) {
		Log.d(LOGCAT, "delete");
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM  wifi where nama='"+ nama +"'";
		Log.d("query", deleteQuery);
		database.execSQL(deleteQuery);
	}
	
	public ArrayList<HashMap<String, String>> getAllWifi() {
		ArrayList<HashMap<String, String>> wifiList;
		wifiList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM wifi";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("_ID", cursor.getString(0));
	        	map.put("nama", cursor.getString(1));
	        	map.put("alamat", cursor.getString(2));
				map.put("keterangan", cursor.getString(3));
	            wifiList.add(map);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return wifiList;
	}

	public HashMap<String, String> getWifiInfo(String id) {
		HashMap<String, String> wifiList = new HashMap<String, String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM wifi where nama='"+id+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
	        do {
					//HashMap<String, String> map = new HashMap<String, String>();
				wifiList.put("_ID", cursor.getString(0));
				wifiList.put("nama", cursor.getString(1));
	        	wifiList.put("alamat", cursor.getString(2));
				wifiList.put("keterangan", cursor.getString(3));
				   //wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	return wifiList;
	}

	public boolean cekData(String id) {
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM wifi where nama='"+id+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			return true;
		}else{
			return false;
		}


	}

}
