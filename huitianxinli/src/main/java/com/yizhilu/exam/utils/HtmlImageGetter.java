package com.yizhilu.exam.utils;

import java.io.InputStream;

import com.yizhilu.utils.PhoneUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

public class HtmlImageGetter implements ImageGetter {
	private TextView _htmlText;
	private String _imgPath;
	private Drawable _defaultDrawable;
	private Context context;
	public HtmlImageGetter(Context context,TextView htmlText, String imgPath, Drawable defaultDrawable){
		this.context = context;
	    _htmlText = htmlText;
	    _imgPath = imgPath;
	    _defaultDrawable = defaultDrawable;
	  }
	@Override
	public Drawable getDrawable(String imgUrl) {
		String imgKey = String.valueOf(imgUrl.hashCode());
	    String path = Environment.getExternalStorageDirectory() + _imgPath;
	    FileUtil.createPath(path);
	    String[] ss = imgUrl.split("\\.");
	    String imgX = ss[ss.length-1];
	    imgKey = path+"/" + imgKey+"."+imgX;
	    if(FileUtil.exists(imgKey)){
	      Drawable drawable = FileUtil.getImageDrawable(imgKey);
	      if(drawable != null){
	    	  float aa = context.getResources().getDisplayMetrics().density;  
	        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*aa), (int)(drawable.getIntrinsicHeight()*aa));
	        return drawable;
	      }else{
	      }
	    }
		URLDrawable urlDrawable = new URLDrawable(_defaultDrawable);
	    new AsyncThread(urlDrawable).execute(imgKey, imgUrl);
	    return urlDrawable;
	}
	
	private class AsyncThread extends AsyncTask<String, Integer, Drawable> {
	    private String imgKey;
	    private URLDrawable _drawable;
	    public AsyncThread(URLDrawable drawable){
	      _drawable = drawable;
	    }
	    @Override
	    protected Drawable doInBackground(String... strings) {
	      imgKey = strings[0];
	      InputStream inps = NetWork.getInputStream(strings[1]);
	      if(inps == null) return _drawable;
	      FileUtil.saveFile(imgKey, inps);
	      Drawable drawable = Drawable.createFromPath(imgKey);
	      return drawable;
	    }
	    public void onProgressUpdate(Integer... value) {
	    }
	    @Override
	    protected void onPostExecute(Drawable result) {
	      _drawable.setDrawable(result);
	      _htmlText.setText(_htmlText.getText());
	    }
	  }

	public class URLDrawable extends BitmapDrawable {
	    private Drawable drawable;
	    @SuppressWarnings("deprecation")
		public URLDrawable(Drawable defaultDraw){
	      setDrawable(defaultDraw);
	    }
	    private void setDrawable(Drawable ndrawable){
	      drawable = ndrawable;
	      float aa = context.getResources().getDisplayMetrics().density;  
	      drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*aa), (int)(drawable
	          .getIntrinsicHeight()*aa));
	      setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*aa), (int)(drawable
		          .getIntrinsicHeight()*aa));
	    }
	    @Override
	    public void draw(Canvas canvas) {
	      drawable.draw(canvas);
	    }
	  }
}
