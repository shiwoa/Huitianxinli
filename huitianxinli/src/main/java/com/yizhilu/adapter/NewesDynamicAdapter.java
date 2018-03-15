package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
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
 * @author bin
 * 修改人:
 * 时间:2015-10-16 下午3:14:08
 * 类说明:最新动态的适配器
 */
public class NewesDynamicAdapter extends BaseAdapter {
	private Context context;  //上下文对象
	private List<EntityCourse> informationList;  //文章的实体
	private ImageLoader imageLoader;  //加载图片的对象
	public NewesDynamicAdapter(Context context,List<EntityCourse> informationList) {
		super();
		this.context = context;
		this.informationList = informationList;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return informationList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_article, null);
			holder.articleTitle = (TextView) convertView.findViewById(R.id.article_title);
			holder.articleTime = (TextView) convertView.findViewById(R.id.article_time);
			holder.articleNum = (TextView) convertView.findViewById(R.id.article_num);
			holder.articleImage = (ImageView) convertView.findViewById(R.id.article_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.articleTitle.setText(informationList.get(position).getTitle());
		String timeString = informationList.get(position).getUpdateTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		holder.articleTime.setText(month+":"+time);
		holder.articleNum.setText(informationList.get(position).getClickTimes()+"");
		imageLoader.displayImage(Address.IMAGE_NET+informationList.get(position).getPicture(),holder.articleImage,HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder{
		private ImageView articleImage;
		private TextView articleTitle,articleTime,articleNum;
	}
}
