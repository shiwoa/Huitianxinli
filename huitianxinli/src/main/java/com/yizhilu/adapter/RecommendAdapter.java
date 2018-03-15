package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
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

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-16 上午10:11:24
 * 类说明:推荐课程的适配器
 */
public class RecommendAdapter extends BaseAdapter {
	private Context context;  //上下文对象
	private List<EntityCourse> recommendCourseList;  //推荐课程的集合
	private ImageLoader imageLoader;  //加载图片的对象
	public RecommendAdapter(Context context,List<EntityCourse> recommendCourseList) {
		super();
		this.context = context;
		this.recommendCourseList = recommendCourseList;
		imageLoader = ImageLoader.getInstance();  //实例化imageLoader
	}

	@Override
	public int getCount() {
		return recommendCourseList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend, null);
			holder.recommendImage = (ImageView) convertView.findViewById(R.id.recommendImage);
			holder.recommendTitle = (TextView) convertView.findViewById(R.id.recommendTitle);
			holder.recommendNum = (TextView) convertView.findViewById(R.id.recommendNum);
			holder.recommendMoney = (TextView) convertView.findViewById(R.id.recommendMoney);
			holder.recommend_currentPrice = (LinearLayout) convertView.findViewById(R.id.recommend_currentPrice);
			holder.recommend_freePrice = (LinearLayout) convertView.findViewById(R.id.recommend_freePrice);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.recommendTitle.setText(recommendCourseList.get(position).getCourseName());
		holder.recommendNum.setText(recommendCourseList.get(position).getPlayCount()+"");
		int ispay = recommendCourseList.get(position).getIsPay();
		float price = recommendCourseList.get(position).getCurrentPrice();
		if (ispay == 0 || price <=0) {
			holder.recommend_currentPrice.setVisibility(View.GONE);
			holder.recommend_freePrice.setVisibility(View.VISIBLE);
		}else {
			holder.recommendMoney.setText(price+"");
		}
		
		imageLoader.displayImage(Address.IMAGE_NET + recommendCourseList.get(position).getMobileLogo(), holder.recommendImage,HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder {
		private ImageView recommendImage;  //推荐课程的图片
		private TextView recommendTitle,recommendNum,recommendMoney;  //推荐课程的标题,推荐课程的播放量 ,价格
		private LinearLayout recommend_currentPrice,recommend_freePrice; //价格lin ,免费lin
		
	}
}
