package com.yizhilu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class NoScrollExpandableListView extends ExpandableListView {

	public NoScrollExpandableListView(Context context) {
		super(context);
	}

	public NoScrollExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoScrollExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
}
