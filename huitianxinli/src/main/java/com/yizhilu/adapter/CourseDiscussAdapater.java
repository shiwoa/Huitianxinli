package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;

public class CourseDiscussAdapater extends BaseAdapter {

	private Context context;
	private List<EntityPublic> entityPublics; //课程评论实体
	private ImageLoader imageLoader;
	
	public CourseDiscussAdapater(Context context,List<EntityPublic> entityPublics) {
		super();
		this.context = context;
		this.entityPublics = entityPublics;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return entityPublics.size();
	}

	@Override
	public Object getItem(int position) {
		return entityPublics.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_course_discuss, null);
			vhHolder.userHead = (CircleImageView) convertView.findViewById(R.id.discuss_head);
			vhHolder.userName = (TextView) convertView.findViewById(R.id.discuss_userName);
			vhHolder.content = (TextView) convertView.findViewById(R.id.discuss_content);
		    vhHolder.time = (TextView) convertView.findViewById(R.id.discuss_time);
		    convertView.setTag(vhHolder);
		}else {
			vhHolder = (ViewHolder) convertView.getTag();
		}
		String nickname = entityPublics.get(position).getNickname();
		String email = entityPublics.get(position).getEmail();
		String mobile = entityPublics.get(position).getMobile();
		if(!TextUtils.isEmpty(nickname)){
			vhHolder.userName.setText(nickname);
		}else if(!TextUtils.isEmpty(email)){
			vhHolder.userName.setText(email);
		}else{
			vhHolder.userName.setText(mobile);
		}
		
		vhHolder.content.setText(entityPublics.get(position).getContent());
		
		String timeString = entityPublics.get(position).getCreateTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		vhHolder.time.setText(month+":"+time);
		imageLoader.displayImage(Address.IMAGE_NET+entityPublics.get(position).getAvatar(), vhHolder.userHead,HttpUtils.getDisPlay());
		return convertView;
	}
	class ViewHolder{
		TextView userName, content,time;
		CircleImageView userHead;
	}

}
