package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

public class TeacherDetailsAdapter extends BaseAdapter{

	private Context context;
	private List<EntityCourse> entitieCourses;
	private ImageLoader imageLoader;
	
	public TeacherDetailsAdapter(Context context,List<EntityCourse> entitieCourses) {
		super();
		this.context = context;
		this.entitieCourses = entitieCourses;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return entitieCourses.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vhHolder = null;
		if (convertView == null) {
			vhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_lecturer_details, null);
			vhHolder.courseImage = (ImageView) convertView.findViewById(R.id.lecturer_courseImage);
			vhHolder.courseName = (TextView) convertView.findViewById(R.id.lecturer_courseName);
			vhHolder.courseNumber = (TextView) convertView.findViewById(R.id.lecturer_number);
			vhHolder.recommendMoney = (TextView) convertView.findViewById(R.id.recommendMoney);
			vhHolder.recommend_currentPrice = (LinearLayout) convertView.findViewById(R.id.recommend_currentPrice);
			vhHolder.recommend_freePrice = (LinearLayout) convertView.findViewById(R.id.recommend_freePrice);
			convertView.setTag(vhHolder);
		}else {
			vhHolder = (ViewHolder) convertView.getTag(); 
		}
		  imageLoader.displayImage(Address.IMAGE_NET+entitieCourses.get(position).getMobileLogo(), vhHolder.courseImage, HttpUtils.getDisPlay());
		  vhHolder.courseName.setText(entitieCourses.get(position).getName());
		  
		  int ispay = entitieCourses.get(position).getIsPay();
		  float price = entitieCourses.get(position).getCurrentprice();
			if (ispay == 0 || price <=0) {
				vhHolder.recommend_currentPrice.setVisibility(View.GONE);
				vhHolder.recommend_freePrice.setVisibility(View.VISIBLE);
			}else {
				vhHolder.recommendMoney.setText(price+"");
			}
		  
		  vhHolder.courseNumber.setText("播放量 : "+entitieCourses.get(position).getPlayNum());
		return convertView;
	}
	class ViewHolder{
		private ImageView courseImage;
		private TextView courseName,courseNumber,recommendMoney;
		private LinearLayout recommend_currentPrice,recommend_freePrice; //价格lin ,免费lin
	}

}
