package com.yizhilu.community.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseGroupFragment extends Fragment {

	protected boolean isVisible;//当前Fragment是否可见

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
		}
	}

	protected void onVisible() {
		Load();
	}

	protected abstract void Load();
}
