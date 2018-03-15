package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
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
 * 
 * @author liuchangqi 修改人: 时间:2016-7-19 下午3:00:04 类说明:套餐课程
 */
public class PackageCourseAdapter extends BaseAdapter {
	private Context context;// 上下文对象
	private List<EntityCourse> packageCourseList;// 数据源
	private ImageLoader imageLoader; // 加载图片的对象

	public PackageCourseAdapter(Context context,
			List<EntityCourse> packageCourseList) {
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		this.packageCourseList = packageCourseList;
	}

	@Override
	public int getCount() {
		return packageCourseList.size();
	}

	@Override
	public EntityCourse getItem(int position) {
		return packageCourseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	/**
	 * @author xaingyao 2016-08-08 16:19:53
	 * 说明  播放量 修改为 xx人学习
	 * 
	 * */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_package_course, null);
			holder.recommendImage = (ImageView) convertView
					.findViewById(R.id.recommendImage);
			holder.recommendTitle = (TextView) convertView
					.findViewById(R.id.recommendTitle);
			holder.recommend = (TextView) convertView
					.findViewById(R.id.recommend);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String mobileLogo = packageCourseList.get(position).getMobileLogo();
		if (!TextUtils.isEmpty(mobileLogo)) {
			imageLoader.displayImage(Address.IMAGE_NET + mobileLogo,
					holder.recommendImage, HttpUtils.getDisPlay());
		} else {
			imageLoader.displayImage(
					Address.IMAGE_NET
							+ packageCourseList.get(position).getLogo(),
					holder.recommendImage, HttpUtils.getDisPlay());
		}
		holder.recommendTitle
				.setText(packageCourseList.get(position).getName());
		holder.recommend.setText(packageCourseList.get(position)
				.getPageBuycount() + "");
		return convertView;
	}

	class ViewHolder {
		private ImageView recommendImage; // 图片
		private TextView recommendTitle, recommend; // 推荐课程的标题,推荐课程的播放量

	}
}
