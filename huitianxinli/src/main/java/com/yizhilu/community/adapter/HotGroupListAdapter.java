package com.yizhilu.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;

/**
 * Created by ming on 2016/6/24 18:32. Explain: 热点推荐小组适配器
 */
public class HotGroupListAdapter extends RecyclerView.Adapter<HotGroupListAdapter.MyViewHolder> {

	private List<EntityPublic> hotGroupList;
	private Context context;
	private ImageLoader imageLoader;
	private OnItemClickLitener onItemClickLitener;

	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
	}

	public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
		this.onItemClickLitener = onItemClickLitener;
	}

	public HotGroupListAdapter(List<EntityPublic> hotGroupList, Context context) {
		this.hotGroupList = hotGroupList;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public HotGroupListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(
				LayoutInflater.from(context).inflate(R.layout.item_hot_group_list, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		holder.hot_group_name.setText(hotGroupList.get(position).getName());
		imageLoader.displayImage(Address.IMAGE + hotGroupList.get(position).getImageUrl(), holder.hot_group_avatar,
				LoadImageUtil.loadImageRound());

		if (onItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClickLitener.onItemClick(holder.itemView, position);
				}
			});
		}

	}

	@Override
	public int getItemCount() {
		return hotGroupList.size();
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {

		private TextView hot_group_name;
		private ImageView hot_group_avatar;

		public MyViewHolder(View itemView) {
			super(itemView);
			hot_group_name = (TextView) itemView.findViewById(R.id.hot_group_name);
			hot_group_avatar = (ImageView) itemView.findViewById(R.id.hot_group_avatar);
		}
	}
}
