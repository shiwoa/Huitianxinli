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
 * @author bin
 * 修改人:
 * 时间:2015-11-2 下午3:33:30
 * 类说明:我的课程的适配器
 */
public class MyCourseAdapater extends BaseAdapter {
	private Context context;  //上下文对象
	private List<EntityCourse> datas; // 放置数据的总集合
	private ImageLoader imageLoader;  //加载图片的对象
	public MyCourseAdapater(Context context, List<EntityCourse> datas) {
		super();
		this.context = context;
		this.datas = datas;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return datas.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_my_course, null);
			holder.course_image = (ImageView) convertView.findViewById(R.id.course_image);
			holder.course_title = (TextView) convertView.findViewById(R.id.course_title);
			holder.course_teacher = (TextView) convertView.findViewById(R.id.course_teacher);
			holder.course_lessionnum = (TextView) convertView.findViewById(R.id.course_lessionnum);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		StringBuffer buffer = new StringBuffer();
		holder.course_title.setText(datas.get(position).getName());
		for(int i=0;i<datas.get(position).getTeacherList().size();i++){
			buffer.append(datas.get(position).getTeacherList().get(i).getName());
		}
		holder.course_teacher.setText("讲师 : "+buffer.toString());
		holder.course_lessionnum.setText("课时 : "+datas.get(position).getLessionnum());
		imageLoader.displayImage(Address.IMAGE_NET+datas.get(position).getMobileLogo(), holder.course_image,HttpUtils.getDisPlay());
		return convertView;
	}
	
	class ViewHolder{
		private ImageView course_image;
		private TextView course_title,course_teacher,course_lessionnum;
	}
}
