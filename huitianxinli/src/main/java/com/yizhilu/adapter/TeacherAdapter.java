package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.TeacherEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin 修改人: 时间:2015-10-19 下午3:15:05 类说明:教师列表的适配器
 */
public class TeacherAdapter extends BaseAdapter {
	private Context context;
	private List<TeacherEntity> teacherEntities;
	private ImageLoader imageLoader;

	public TeacherAdapter(Context context, List<TeacherEntity> teacherEntities) {
		super();
		this.context = context;
		this.teacherEntities = teacherEntities;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return teacherEntities.size();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher, null);
			holder.teacherImage = (ImageView) convertView.findViewById(R.id.teacher_image);
			holder.teacherName = (TextView) convertView.findViewById(R.id.teacherName);
			holder.gradeTitle = (TextView) convertView.findViewById(R.id.gradeTitle);
			holder.teacherContent = (TextView) convertView.findViewById(R.id.teacher_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.teacherName.setText(teacherEntities.get(position).getName());
		if (teacherEntities.get(position).getIsStar() == 0) {
			holder.gradeTitle.setText("高级讲师");
		} else {
			holder.gradeTitle.setText("首席讲师");
		}
//		holder.teacherContent.setText(teacherEntities.get(position).getCareer());
		imageLoader.displayImage(Address.IMAGE_NET + teacherEntities.get(position).getPicPath(), holder.teacherImage,
				HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder {
		private ImageView teacherImage;
		private TextView teacherName, gradeTitle, teacherContent;
	}
}
