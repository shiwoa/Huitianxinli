package com.yizhilu.baoli;

import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PolyvDBservice {
	private static final String TAG = "DBservice";
	private PolyvDBOpenHepler dbOpenHepler;
	private SQLiteDatabase db;

	public PolyvDBservice(Context context) {
		// 1 -> database version
		dbOpenHepler = PolyvDBOpenHepler.getInstance(context, 3);
	}

	public void addDownloadFile(PolyvDownloadInfo info) {
		db = dbOpenHepler.getWritableDatabase();
		String sql = "insert into downloadlist(vid,title,duration,filesize,bitrate,image) values(?,?,?,?,?,?)";
		db.execSQL(
				sql,
				new Object[] { info.getVid(),info.getTitle(), info.getDuration(),
						info.getFilesize(),info.getBitrate(), info.getImage() });
	}
	public void deleteDownloadFile(PolyvDownloadInfo info) {
		db = dbOpenHepler.getWritableDatabase();
		String sql = "delete from downloadlist where vid=? and bitrate=?";
		db.execSQL(
				sql,
				new Object[] { info.getVid(),info.getBitrate() });
	}
	public void updatePercent(PolyvDownloadInfo info,long percent,long total) {
		db = dbOpenHepler.getWritableDatabase();
		String sql = "update downloadlist set percent=?,total=? where vid=? and bitrate=?";
		db.execSQL(
				sql,
				new Object[] { percent,total,info.getVid(),info.getBitrate() });
	}
	public boolean isAdd(PolyvDownloadInfo info) {
		db = dbOpenHepler.getWritableDatabase();
		String sql = "select vid ,duration,filesize,bitrate from downloadlist where vid=? and bitrate=" + info.getBitrate();
		Cursor cursor = db.rawQuery(sql, new String[] { info.getVid() });
		if (cursor.getCount() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public LinkedList<PolyvDownloadInfo> getDownloadFiles(){
		LinkedList<PolyvDownloadInfo> infos = new LinkedList<PolyvDownloadInfo>();
		db = dbOpenHepler.getWritableDatabase();
//		String sql ="select vid,title,duration,filesize,bitrate,percent,total,image from downloadlist";
		String sql ="select * from downloadlist";
		Cursor cursor= db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String vid=cursor.getString(cursor.getColumnIndex("vid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String duration=cursor.getString(cursor.getColumnIndex("duration"));
			long filesize=cursor.getInt(cursor.getColumnIndex("filesize"));
			int bitrate=cursor.getInt(cursor.getColumnIndex("bitrate"));
			long percent=cursor.getInt(cursor.getColumnIndex("percent"));
			long total=cursor.getInt(cursor.getColumnIndex("total"));
			String image = cursor.getString(cursor.getColumnIndex("image"));
			PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize,bitrate,image);
			info.setPercent(percent);
			info.setTitle(title);
			info.setTotal(total);
			info.setImage(image);
			infos.addLast(info);
			
		}
		return infos;
	}
}
