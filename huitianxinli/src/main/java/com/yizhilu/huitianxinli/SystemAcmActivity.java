package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * 
 * @author lrannn 修改人: 时间:2015年10月22日 上午10:49:37 类说明: SystemAnnouncementActivity
 *         系统公告
 */
public class SystemAcmActivity extends BaseActivity {

	private TextView title;  //标题
	private LinearLayout back_layout;  //返回布局
	private PullToRefreshScrollView refreshScrollView;  //上拉加载
	private NoScrollListView mListView;  //系统公告的布局
	private ProgressDialog mProgressDialog;  //加载数据显示的dialog
	private List<EntityPublic> letters;  //公告的集合
	private int currentPage = 1,userId;  //当前页数,用户Id
	public Spanned fromHtml;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_systemacm);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",0);  //得到用户Id
		mProgressDialog = new ProgressDialog(SystemAcmActivity.this);  //加载数据显示的dialog
		letters = new ArrayList<EntityPublic>(); //公告的集合
		title = (TextView) findViewById(R.id.title_text);  //标题
		title.setText("系统公告");  //设置标题
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.systemacm_scrollview);  //上拉加载
		refreshScrollView.setMode(Mode.BOTH);  //设置加载模式
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		mListView = (NoScrollListView) findViewById(R.id.listView);  //系统公告的布局
		//联网获取系统公告的方法
		getMessageByUserId(userId, currentPage);
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回的布局
		refreshScrollView.setOnRefreshListener(this);  //下拉刷新和上拉加载的对象
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-30 下午1:44:57
	 * 方法说明:联网获取系统公告的方法
	 */
	private void getMessageByUserId(int userId, final int currentPage) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		Log.i("lala", Address.SYSTEM_ANNOUNCEMENT+params.toString());
		client.post(Address.SYSTEM_ANNOUNCEMENT, params,
				new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(mProgressDialog);
			}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						if(!TextUtils.isEmpty(data)){
							try {
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								if (parseObject.isSuccess()) {
									PageEntity pageEntity = parseObject.getEntity().getPage();
									if(pageEntity.getCurrentPage()<=currentPage){
										refreshScrollView.setMode(Mode.PULL_FROM_START);
									}
									List<EntityPublic> letterList = parseObject.getEntity()
											.getLetterList();
									if (letterList != null && letterList.size() > 0) {
										letters.addAll(letterList);
									}
									MyAdapter adapter = new MyAdapter();
									mListView.setAdapter(adapter);
								}
								refreshScrollView.onRefreshComplete();
							} catch (Exception e) {
								refreshScrollView.onRefreshComplete();
							}
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						refreshScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回的布局
			this.finish();
			break;

		default:
			break;
		}
	}
	
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return letters.size();
		}

		@Override
		public Object getItem(int position) {
			return letters.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.item_systemacm, null);
				mHolder = new ViewHolder();
				mHolder.nickName = (TextView) convertView
						.findViewById(R.id.systemacm_nick);
				mHolder.time = (TextView) convertView
						.findViewById(R.id.systemacm_time);
				mHolder.content = (TextView) convertView
						.findViewById(R.id.systemacm_content);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			if(!TextUtils.isEmpty(letters.get(position).getContent())){
				fromHtml = Html.fromHtml(letters.get(position).getContent());
			}
			
			
			
			String timeString = letters.get(position).getAddTime();
			String month = timeString.split(":")[0];
			String time = timeString.split(":")[1].split(":")[0];
			
//			mHolder.time.setText(letters.get(position).getAddTime());
			mHolder.time.setText(month+":"+time);
			mHolder.content.setText(fromHtml);
			return convertView;
		}

	}

	class ViewHolder {
		TextView nickName, time, content;
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		currentPage = 1;
		letters.clear();
		//联网获取系统公告的方法
		getMessageByUserId(userId, currentPage);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		currentPage++;
		getMessageByUserId(userId, currentPage);
	}

}
