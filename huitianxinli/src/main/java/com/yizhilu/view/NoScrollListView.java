package com.yizhilu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author 杨财宾
 * 修改人:
 * 时间:2015-8-29 下午1:46:25
 * 类说明:不能滑动的ListView
 */
public class NoScrollListView extends ListView {
	
	public NoScrollListView(Context context) {
		
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
	
	
}
