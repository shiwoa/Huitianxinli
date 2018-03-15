package com.yizhilu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;


public class HotGroupListAdapter extends RecyclerView.Adapter<HotGroupListAdapter.MyViewHolder> {

	private List<EntityPublic> hotGroupList;
	private Context context;
	private OnItemClickLitener onItemClickLitener;
	private int number=0;

	
	
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
	}

	public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
		this.onItemClickLitener = onItemClickLitener;
	}

	public HotGroupListAdapter(List<EntityPublic> hotGroupList, Context context) {
		this.hotGroupList = hotGroupList;
		this.context = context;
	}

	@Override
	public HotGroupListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(
				LayoutInflater.from(context).inflate(R.layout.item_hot_group_list_new, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		if(number==position){
			holder.hot_group_name.setTextColor(context.getResources().getColor(R.color.Blue));
			holder.xian.setVisibility(View.VISIBLE);
		}else{
			holder.hot_group_name.setTextColor(context.getResources().getColor(R.color.tabText));
			holder.xian.setVisibility(View.GONE);
		}
		holder.hot_group_name.setText(hotGroupList.get(position).getName());
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
		private ImageView xian;
		public MyViewHolder(View itemView) {
			super(itemView);
			hot_group_name = (TextView) itemView.findViewById(R.id.hot_group_name);
			xian=(ImageView) itemView.findViewById(R.id.xian);
		}
	}
	
	/**
	 * 通知适配器点击位置
	 * 
	 * */
	public void setPosition(int number){
		this.number=number;
	}
}
