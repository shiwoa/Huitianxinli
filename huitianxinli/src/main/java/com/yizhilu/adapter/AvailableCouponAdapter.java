package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-27 上午10:34:21
 * 类说明:购买时可用优惠券的适配器
 */
public class AvailableCouponAdapter extends BaseAdapter {
	private Context context; 
	private List<EntityPublic> entityPublics;
	private boolean isSelect;
	private int mSelect = -1; 
	public void changeSelected(int positon){ 
			if(positon != mSelect){
			 mSelect = positon;
			 notifyDataSetChanged();
			}
	}
	
	public AvailableCouponAdapter(Context context,
			List<EntityPublic> entityPublics) {
		super();
		this.context = context;
		this.entityPublics = entityPublics;
	}

	@Override
	public int getCount() {
		return entityPublics.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.copy_of_item_discount_coupon, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.couponTitle = (TextView) convertView.findViewById(R.id.coupon_title);
			holder.couponNumber = (TextView) convertView.findViewById(R.id.coupon_number);
			holder.couponTime = (TextView) convertView.findViewById(R.id.limited_text);
			holder.coupon_info = (TextView) convertView.findViewById(R.id.coupon_info);
			holder.discount_text = (TextView) convertView.findViewById(R.id.discount_text);
			holder.money_image = (TextView) convertView.findViewById(R.id.money_image);
			holder.title_view = (View) convertView.findViewById(R.id.title_view);
			holder.discount_select = (ImageView) convertView.findViewById(R.id.discount_select);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		EntityPublic entityPublic = entityPublics.get(position);
		holder.couponTitle.setText(entityPublic.getTitle());
		holder.couponNumber.setText(entityPublic.getCouponCode());
		
		String timeString = entityPublic.getEndTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		
//		holder.couponTime.setText(entityPublic.getEndTime());
		holder.couponTime.setText(month+":"+time);
		holder.coupon_info.setText("(消费满"+entityPublic.getLimitAmount()+"可用)");
		String amount = entityPublic.getAmount();
		String retainOne = ConstantUtils.getRetainOneDecimal(1, amount);
		if(entityPublic.getType() == 1){  //折扣券
			holder.title_view.setBackgroundResource(R.drawable.voucher_you);
			holder.title.setText("优惠券");
			holder.title.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
			holder.money_image.setVisibility(View.GONE);
			holder.discount_text.setText(retainOne+" 折");
			holder.discount_text.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
			holder.coupon_info.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
		}else if(entityPublic.getType() == 2){ //定额券
			holder.title.setText("代金券");
			holder.title.setTextColor(context.getResources().getColor(R.color.color_FD6464));
			holder.money_image.setVisibility(View.VISIBLE);
			holder.money_image.setTextColor(context.getResources().getColor(R.color.color_FD6464));
			holder.discount_text.setText(""+retainOne);
			holder.discount_text.setTextColor(context.getResources().getColor(R.color.color_FD6464));
		}
		if(mSelect == position){    

				holder.discount_select.setBackgroundResource(R.drawable.collect_button_select);
				context.getSharedPreferences("selectPosition", context.MODE_PRIVATE)
				.edit().putInt("selectPosition", position).commit();
				Intent intent = new Intent();
				intent.setAction("inform");
				context.sendBroadcast(intent);
			
	    }else{
	        holder.discount_select.setBackgroundResource(R.drawable.collect_button);
	    }
		return convertView;
	}

	class ViewHolder{
		private TextView title,money_image;  //类型
		private View title_view;
		private ImageView discount_select; //选中图片
		private TextView couponTitle,couponNumber,couponTime,coupon_info,discount_text;
	}
}
