package com.yizhilu.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.huitianxinli.ComboDetailsActivity;

/**
 * @ClassName: MeViewPager
 */
public class MeViewPager extends ViewPager {
	private Context context = null;
	PointF downP = new PointF();
	PointF curP = new PointF();
	private int courseId;
	private String sellType;

	public MeViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		curP.x = event.getX(); 
		curP.y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downP.x = event.getX();
			downP.y = event.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (downP.x == curP.x && downP.y == curP.y) {
				onSingleTouch();
				return false;
			}
		}
		return super.onTouchEvent(event);
	}

	public void onSingleTouch(Object... psotion) {
		if(this.courseId !=0&&sellType!=null){
			if(sellType.equals("COURSE")){
				Intent intent = new Intent(context,BLCourseDetailsActivity.class);
				intent.putExtra("courseId", this.courseId);
				context.startActivity(intent);
			}else if(sellType.equals("PACKAGE")){
				
				Intent intent = new Intent(context,ComboDetailsActivity.class);
				intent.putExtra("comboId", this.courseId);
				context.startActivity(intent);
			}
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setMyContext(Context con) {
		this.context = con;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public void setSelltype(String sellType) {
		this.sellType = sellType;
	}

}