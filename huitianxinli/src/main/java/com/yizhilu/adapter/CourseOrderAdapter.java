package com.yizhilu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2015-12-1 上午10:46:31
 * 类说明:课程订单的适配器
 */
public class CourseOrderAdapter extends BaseAdapter {
	private Context context;  //上下文对象
//	private OrderEntity orderEntity; //课程订单的实体
	private PublicEntity publicEntity;
	private ImageLoader imageLoader;
	public CourseOrderAdapter(Context context, PublicEntity publicEntity) {
		super();
		this.context = context;
//		this.orderEntity = orderEntity;
		this.publicEntity = publicEntity;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
//		return orderEntity.getTrxorderDetailList().size();
		return publicEntity.getEntity().getDetailList().size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_course_order, null);
			holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);
			holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
			holder.playNum = (TextView) convertView.findViewById(R.id.playNum);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		EntityPublic entity = publicEntity.getEntity();
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<entity.getDetailList().get(position).getTeacherList().size();i++){
			buffer.append(entity.getDetailList().get(position).getTeacherList().get(i).getName()+" ");
		}
		holder.courseName.setText(entity.getDetailList().get(position).getCourseName());
		holder.playNum.setText("讲师 : "+buffer.toString());
		imageLoader.displayImage(Address.IMAGE_NET+entity.getDetailList().get(position).getCourseLogo(), holder.courseImage,HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder{
		private ImageView courseImage;  //課程圖片
		private TextView courseName,playNum;  //課程名，播放量
	}
}
