package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.entity.CouponEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

public class CouponAdapter extends BaseAdapter {
	private Context context;
	private List<CouponEntity> allCouponList;
	public CouponAdapter(Context context,List<CouponEntity> list) {
		super();
		this.context = context;
		this.allCouponList = list;
	}

	@Override
	public int getCount() {
		return allCouponList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_discount_coupon, null);
			holder.title_view = convertView.findViewById(R.id.title_view);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.title_layout = (RelativeLayout) convertView.findViewById(R.id.title_layout);
			holder.couponTitle = (TextView) convertView.findViewById(R.id.coupon_title);
			holder.couponNumber = (TextView) convertView.findViewById(R.id.coupon_number);
			holder.couponTime = (TextView) convertView.findViewById(R.id.limited_text);
			holder.coupon_info = (TextView) convertView.findViewById(R.id.coupon_info);
			holder.discount_text = (TextView) convertView.findViewById(R.id.discount_text);
			holder.money_image = (TextView) convertView.findViewById(R.id.money_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CouponEntity couponEntity = allCouponList.get(position);
		holder.couponNumber.setText(couponEntity.getCouponCode());
		
		String timeString = couponEntity.getEndTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		
//		holder.couponTime.setText(couponEntity.getEndTime());
		holder.couponTime.setText(month+":"+time);
		holder.coupon_info.setText("(消费满"+couponEntity.getLimitAmount()+"元可用)");
		int type = couponEntity.getType();
		String amount = couponEntity.getAmount();
		String retainOne = ConstantUtils.getRetainOneDecimal(1, amount);
		if(type == 1){  //折扣券(优惠券)
			holder.title_view.setBackgroundResource(R.drawable.voucher_you);
			holder.title.setText("优惠券");
			holder.title.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
			holder.couponTitle.setText(couponEntity.getTitle()+"优惠券");
			holder.money_image.setVisibility(View.GONE);
			holder.discount_text.setText(retainOne+" 折");
			holder.discount_text.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
			holder.coupon_info.setTextColor(context.getResources().getColor(R.color.color_56B7B9));
		}else if(type == 2){  //定额券(代金券)
			holder.title_view.setBackgroundResource(R.drawable.voucher);
			holder.title.setText("代金券");
			holder.title.setTextColor(context.getResources().getColor(R.color.color_FD6464));
			holder.couponTitle.setText(couponEntity.getTitle());
			holder.money_image.setVisibility(View.VISIBLE);
			holder.money_image.setTextColor(context.getResources().getColor(R.color.color_FD6464));
			holder.discount_text.setText(""+retainOne);
			holder.discount_text.setTextColor(context.getResources().getColor(R.color.color_FD6464));
			holder.coupon_info.setTextColor(context.getResources().getColor(R.color.color_FD6464));
		}else if(type == 3){ //会员优惠券
			holder.couponTitle.setText(couponEntity.getTitle());
			holder.discount_text.setText(retainOne+" 折");
		}
		if(couponEntity.getStatus() == 2||couponEntity.getStatus() == 3||couponEntity.getStatus() == 4){
			holder.title_view.setBackgroundResource(R.drawable.voucher_past);
			holder.title.setTextColor(context.getResources().getColor(R.color.color_b2));
			holder.money_image.setTextColor(context.getResources().getColor(R.color.color_b2));
			holder.discount_text.setTextColor(context.getResources().getColor(R.color.color_b2));
			holder.coupon_info.setTextColor(context.getResources().getColor(R.color.color_b2));
		}
		return convertView;
	}

	class ViewHolder{
		private View title_view;  
		private TextView title,couponTitle,couponNumber,couponTime,coupon_info,discount_text,money_image;
		private RelativeLayout title_layout;  //标题的布局
	}
	
}
