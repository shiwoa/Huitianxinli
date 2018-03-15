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

public class CollectionAdapter extends BaseAdapter {
	private List<EntityCourse> list;  //存放收藏课程的集合
	private Context context;
	private ImageLoader imageLoader;//加载图片的对象
	private boolean flag;  //判断选中按钮时候显示
	public CollectionAdapter(Context context,List<EntityCourse> list) {
		super();
		this.context = context;
		this.list = list;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
		viewHolder holder = null;
		if(convertView == null){ 
			holder=new viewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_my_collection, null);
			holder.image=(ImageView) convertView.findViewById(R.id.image);
			holder.collection_titleText=(TextView) convertView.findViewById(R.id.collection_titleText);
			holder.collection_riqi=(TextView) convertView.findViewById(R.id.collection_keshifenlei);
			holder.imagecheck=(ImageView) convertView.findViewById(R.id.imagecheck);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder) convertView.getTag();
		}
		holder.imagecheck.setBackgroundResource(R.drawable.collect_button);
		if (flag) {
			holder.imagecheck.setVisibility(View.VISIBLE);
		} else {
			holder.imagecheck.setVisibility(View.GONE);
		}
		holder.collection_titleText.setText(list.get(position).getName());
		int losetype = list.get(position).getLosetype();
//		if(losetype == 0){
//			
//			String timeString = list.get(position).getLoseAbsTime();
//			String month = timeString.split(":")[0];
//			String time = timeString.split(":")[1].split(":")[0];
//			
////			holder.collection_riqi.setText("收藏时间: "+list.get(position).getLoseAbsTime());
//			holder.collection_riqi.setText("收藏时间: "+month+":"+time);
//		}else if(losetype == 1){
//			holder.collection_riqi.setText("有效期: 从购买之日起"+list.get(position).getLoseTime()+"天");
//		}
		
		String timeString = list.get(position).getAddTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		
		holder.collection_riqi.setText("收藏时间: "+month+":"+time);
		imageLoader.displayImage(Address.IMAGE_NET+list.get(position).getMobileLogo(), holder.image,HttpUtils.getDisPlay());
		return convertView;
		
	}
	
	class viewHolder{
		ImageView image,imagecheck;
		TextView collection_titleText,collection_riqi;
	}
}
