package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin 修改人: 时间:2015-10-22 下午10:00:28 类说明:播放记录的方法
 */
public class PlayHistoryAdapter extends BaseAdapter {
	private Context context; // 上下文对象
	private List<EntityCourse> historyList; // 播放记录的集合
	private ImageLoader imageLoader;  //加载图片的对象
	private boolean flag;  //判断选中按钮时候显示
	public PlayHistoryAdapter(Context context, List<EntityCourse> historyList) {
		super();
		this.context = context;
		this.historyList = historyList;
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return historyList.size();
	}

	@Override
	public Object getItem(int position) {
		return historyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void showImg(boolean flag){
		this.flag=flag;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_my_collection, null);
			mHolder.logo = (ImageView) convertView.findViewById(R.id.image);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.collection_titleText);
			mHolder.mTime = (TextView) convertView
					.findViewById(R.id.collection_keshifenlei);
			mHolder.imagecheck=(ImageView) convertView.findViewById(R.id.imagecheck);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.imagecheck.setBackgroundResource(R.drawable.collect_button);
		if (flag) {
			mHolder.imagecheck.setVisibility(View.VISIBLE);
		} else {
			mHolder.imagecheck.setVisibility(View.GONE);
		}
		mHolder.mName.setText(historyList.get(position).getCourseName());
	    String timeString = historyList.get(position).getUpdateTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		
//		mHolder.mTime.setText(historyList.get(position).getUpdateTime() + " 学习");
		mHolder.mTime.setText(month+":"+time+" 学习");
		imageLoader.displayImage(Address.IMAGE_NET+ historyList.get(position).getMobileLogo(), mHolder.logo,HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder {
		private ImageView logo,imagecheck;
		private TextView mName;
		private TextView mTime;
	}
}
