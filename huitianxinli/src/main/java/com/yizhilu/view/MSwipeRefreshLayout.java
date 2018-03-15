package com.yizhilu.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MSwipeRefreshLayout extends SwipeRefreshLayout {
	private float startY;
	private float startX;
	private boolean mIsVpDragger;

	public MSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startY = ev.getY();
			startX = ev.getX();
			mIsVpDragger = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsVpDragger) {
				return false;
			}
			float endY = ev.getY();
			float endX = ev.getX();
			float distanceX = Math.abs(endX - startX);
			float distanceY = Math.abs(endY - startY);
			if (distanceX >= distanceY) {
				mIsVpDragger = true;
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mIsVpDragger = false;
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
