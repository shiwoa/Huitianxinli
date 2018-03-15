package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.TeacherEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin 修改人: 时间:2016-7-16 下午3:31:37 类说明:套餐的适配器
 */
public class ComboAdapter extends BaseAdapter {
	private Context context;
	private List<EntityCourse> courseList; // 套餐的集合
	private ImageLoader imageLoader; // 加载图片
	private StringBuffer buffer; // 拼接教师

	public ComboAdapter(Context context, List<EntityCourse> courseList) {
		super();
		this.context = context;
		this.courseList = courseList;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return courseList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
	/**
	 * @author xiangyao 2016-08-09 09:14:20
	 * 说明		去掉讲师 
	 * 
	 * */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_combo, parent, false);
			holder.combo_image = (ImageView) convertView
					.findViewById(R.id.combo_image);
			holder.combo_money = (TextView) convertView
					.findViewById(R.id.combo_money);
			holder.yuan_money = (TextView) convertView
					.findViewById(R.id.yuan_money);
			holder.courseNumber = (TextView) convertView
					.findViewById(R.id.courseNumber);
			holder.playNumber = (TextView) convertView
					.findViewById(R.id.playNumber);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		EntityCourse entityCourse = courseList.get(position);
		List<TeacherEntity> list_teacher=entityCourse.getTeacherList();
		StringBuilder stringBuilder=new StringBuilder();
		for(int x=0;x<list_teacher.size();x++){
			stringBuilder.append(list_teacher.get(x).getName()+"	");
		}
		
		imageLoader.displayImage(
				Address.IMAGE_NET + entityCourse.getMobileLogo(),
				holder.combo_image, HttpUtils.getDisPlay());
		holder.combo_money.setText("￥ " + entityCourse.getCurrentprice()); // 现价
		holder.yuan_money.setText("原价 : " + entityCourse.getSourceprice()); // 原价
		holder.yuan_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 添加中间横线
		holder.courseNumber.setText("讲师 :  "+stringBuilder.toString());
		holder.title.setText(courseList.get(position).getTitle());
		holder.playNumber.setText("播放 : " +entityCourse.getPagePlaycount() 
				+ "  		" + "评论数:"+entityCourse.getPageCommentcount()+ "  		" + "购买数:"+entityCourse.getPageBuycount());
		
		return convertView;
	}

	class ViewHolder {
		private ImageView combo_image; // 套餐图片
		private TextView combo_money, yuan_money, courseNumber, playNumber,
				name, title; // 价格,原价格,课程数,讲师,播放次数(评论等),套餐名字,简介
	}
}
