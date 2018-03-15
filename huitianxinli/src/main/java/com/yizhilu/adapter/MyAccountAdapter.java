package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.entity.EntityAccList;
import com.yizhilu.huitianxinli.R;

public class MyAccountAdapter extends BaseAdapter {

	private Context context;
	private List<EntityAccList> accouList;

	public MyAccountAdapter(Context context, List<EntityAccList> accouList) {
		super();
		this.context = context;
		this.accouList = accouList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accouList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return accouList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView hv = null;
		if (convertView == null) {
			hv = new HolderView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_myaccount, null);
			hv.tv_account_xiaofei = (TextView) convertView
					.findViewById(R.id.tv_account_xiaofei);
			hv.tv_account_time = (TextView) convertView
					.findViewById(R.id.tv_account_time);
			hv.tv_account_money = (TextView) convertView
					.findViewById(R.id.tv_account_money);
			convertView.setTag(hv);
		} else {
			hv = (HolderView) convertView.getTag();
		}
		
		String timeString = accouList.get(position).getCreateTime();
		String month = timeString.split(":")[0];
		String time = timeString.split(":")[1].split(":")[0];
		
//		hv.tv_account_time.setText(accouList.get(position).getCreateTime());
		hv.tv_account_time.setText(month+":"+time);
		String historyType = accouList.get(position).getActHistoryType();
		if("SALES".equals(historyType)){
			hv.tv_account_xiaofei.setText("消费");
			hv.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_dd3214));
			hv.tv_account_money.setText("- "+accouList.get(position).getTrxAmount()+ "");
		}else if("REFUND".equals(historyType)){
			hv.tv_account_xiaofei.setText("退款");
			hv.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
			hv.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
		}else if("CASHLOAD".equals(historyType)){
			hv.tv_account_xiaofei.setText("现金充值");
			hv.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
			hv.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
		}else if("VMLOAD".equals(historyType)){
			hv.tv_account_xiaofei.setText("充值卡充值");
			hv.tv_account_money.setTextColor(context.getResources().getColor(R.color.color_11af2d));
			hv.tv_account_money.setText("+ "+accouList.get(position).getTrxAmount()+ "");
		}
		return convertView;
	}

	class HolderView {
		private TextView tv_account_xiaofei, tv_account_time, tv_account_money;
	}

}
