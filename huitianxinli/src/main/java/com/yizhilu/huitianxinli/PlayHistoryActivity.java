package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.yizhilu.adapter.PlayHistoryAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * 
 * @author lrannn 修改人: 时间:2015年10月20日 上午9:42:43 类说明:播放历史
 */
public class PlayHistoryActivity extends BaseActivity {
	private LinearLayout back_layout, playhistory_del_null, null_layout; // 返回的布局,删除的布局,空布局
	private PullToRefreshScrollView mScrollView; // 上拉加载和下拉刷新的对象
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private NoScrollListView mListView; // 播放记录的列表
	private int currentPage = 1, userId; // 当前页数,用户Id
	private ProgressDialog mProgressDialog; // 加载数据显示的dialog
	private List<EntityCourse> historyList; // 放置数据的总集合
	private PlayHistoryAdapter historyAdapter; // 播放记录的适配器
	private TextView playhistory_image_edit, playhistory_empty,
			playhistory_delete; // 编辑,清空,删除
	private boolean temp = true, isClear; // true代表完成状态,false代表编辑状态,是否是清空
	private List<Boolean> BooleanList; // 存放收藏是否选中的标识
	private Dialog dialog; // 提示框
	private Object type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_playhistory);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0); // 获取用户的Id
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		mProgressDialog = new ProgressDialog(PlayHistoryActivity.this); // 加载数据显示的dialog
		historyList = new ArrayList<EntityCourse>(); // 播放记录的集合
		BooleanList = new ArrayList<Boolean>(); // 存放收藏是否选中的标识
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.history_scrollview); // 上拉加载和下拉刷新的对象
		mScrollView.setMode(Mode.BOTH); // 设置加载模式
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		playhistory_image_edit = (TextView) findViewById(R.id.playhistory_image_edit); // 编辑
		mListView = (NoScrollListView) findViewById(R.id.history_listview); // 播放记录的列表
		playhistory_del_null = (LinearLayout) findViewById(R.id.playhistory_del_null); // 删除的布局
		playhistory_empty = (TextView) findViewById(R.id.playhistory_empty); // 清空
		playhistory_delete = (TextView) findViewById(R.id.playhistory_delete); // 删除
		null_layout = (LinearLayout) findViewById(R.id.null_layout); // 空布局
		// 获取播放记录的方法
		getPlayHistoryByUserId(currentPage, userId);

	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回
		playhistory_image_edit.setOnClickListener(this); // 编辑
		mScrollView.setOnRefreshListener(this); // 刷新
		mListView.setOnItemClickListener(this); // 播放记录的列表
		playhistory_delete.setOnClickListener(this); // 删除
		playhistory_empty.setOnClickListener(this); // 清空
	}

	/**
	 * @author lrannn 修改人: 时间:2015年10月20日 上午10:07:29 方法说明: 获取播放记录的方法
	 */
	private void getPlayHistoryByUserId(final int currentPage, int userId) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("userId", userId);
		Log.i("wulala", Address.PLAY_HISTORY + "?" + params.toString());
		httpClient.get(Address.PLAY_HISTORY, params,
				new TextHttpResponseHandler() {

					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(mProgressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								if (publicEntity.isSuccess()) {
									PageEntity pageEntity = publicEntity
											.getEntity().getPage();
									if (pageEntity.getTotalPageSize() <= currentPage) {
										mScrollView
												.setMode(Mode.PULL_FROM_START);
									}
									List<EntityCourse> studyList = publicEntity
											.getEntity().getStudyList();
									if (studyList != null
											&& studyList.size() > 0) {
										for (int i = 0; i < studyList.size(); i++) {
											historyList.add(studyList.get(i));
											BooleanList.add(false);
										}
										// historyList.addAll(studyList);
									}
									if (historyAdapter == null) {
										historyAdapter = new PlayHistoryAdapter(
												PlayHistoryActivity.this,
												historyList);
										mListView.setAdapter(historyAdapter);
									} else {
										historyAdapter.notifyDataSetChanged();
									}
									if (historyList.isEmpty()) {
										playhistory_del_null
												.setVisibility(View.GONE);
										temp = true;
										playhistory_image_edit.setText("编辑");
										null_layout.setVisibility(View.VISIBLE);
										mScrollView.setMode(Mode.DISABLED);
									} else {
										null_layout.setVisibility(View.GONE);
									}
									mScrollView.onRefreshComplete();
								}
							} catch (Exception e) {
								mScrollView.onRefreshComplete();
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						mScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			this.finish();
			break;
		case R.id.playhistory_image_edit: // 编辑按钮
			if (historyList.isEmpty()) {
				return;
			}
			if (temp) {
				playhistory_image_edit.setText("完成");
				playhistory_del_null.setVisibility(View.VISIBLE);
				historyAdapter.showImg(temp);
				historyAdapter.notifyDataSetChanged();
				temp = false;
			} else {
				playhistory_image_edit.setText("编辑");
				playhistory_del_null.setVisibility(View.GONE);
				historyAdapter.showImg(temp);
				historyAdapter.notifyDataSetChanged();
				temp = true;
			}
			break;
		case R.id.playhistory_empty:// 清空
			isClear = true;
			showDeleteDiaLog(null);
			break;
		case R.id.playhistory_delete:// 删除
			isClear = false;
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < BooleanList.size(); i++) {
				if (BooleanList.get(i)) {
					buffer.append(historyList.get(i).getId() + ",");
				}
			}
			if (buffer.length() > 0) {
				showDeleteDiaLog(buffer.toString());
			} else {
				ConstantUtils.showMsg(PlayHistoryActivity.this, "请选择要删除的收藏课程!");
			}
			break;
		default:
			break;
		}
	}

	// 刪除收藏課程時顯示的
	public void showDeleteDiaLog(final String deString) {
		View view = getLayoutInflater().inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(this, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
		TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
		LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
		TextView textView = (TextView) view.findViewById(R.id.texttitles);
		if (isClear) {
			textView.setText("您确定要清空吗?");
		} else {
			textView.setText("您确定要刪除吗?");
		}
		linbtnsure.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				if (isClear) {
					// 清空的方法
					cleanCourse();
				} else {
					// 删除的方法
					removeError(deString);
				}
				dialog.cancel();
			}
		});
		TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
		LinearLayout linbtncancle= (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
		linbtncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	// 清空课程
	private void cleanCourse() {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		httpClient.post(Address.PLAYHISTORY_CLEAN, params,
				new TextHttpResponseHandler() {

					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(mProgressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								String message = parseObject.getMessage();
								if (parseObject.isSuccess()) {
									BooleanList.clear();
									historyList.clear();
									getPlayHistoryByUserId(currentPage, userId);
								} else {
									ConstantUtils.showMsg(
											PlayHistoryActivity.this, message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(mProgressDialog);
					}
				});
	}

	// 删除
	private void removeError(final String string) {
		RequestParams params = new RequestParams();
		params.put("ids", string);
		httpClient.post(Address.DELETE_COURSE_PLAYHISTORY, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(mProgressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								String message = parseObject.getMessage();
								if (parseObject.isSuccess()) {
									BooleanList.clear();
									historyList.clear();
									getPlayHistoryByUserId(currentPage, userId);
								} else {
									ConstantUtils.showMsg(
											PlayHistoryActivity.this, message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(mProgressDialog);
					}
				});
	}
	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		currentPage = 1;
		historyList.clear();
		BooleanList.clear();
		mScrollView.setMode(Mode.BOTH);
		getPlayHistoryByUserId(currentPage, userId);
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		currentPage++;
		getPlayHistoryByUserId(currentPage, userId);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		type = historyList.get(position).getSellType();
		switch (parent.getId()) {
		case R.id.history_listview: // 播放记录
			if (temp) {
				
				
				if (type.equals("COURSE")) {
					Intent intent = new Intent(PlayHistoryActivity.this,
							BLCourseDetailsActivity.class);
					intent.putExtra("courseId", historyList.get(position).getCourseId());
					startActivity(intent);
				} else if (type.equals("PACKAGE")) {
					Intent intent = new Intent(PlayHistoryActivity.this,
							ComboDetailsActivity.class);
					intent.putExtra("comboId", historyList.get(position).getCourseId());
					startActivity(intent);
				} else if (type.equals("")) {

				}
			} else {
				ImageView imageView = (ImageView) view
						.findViewById(R.id.imagecheck);
				if (BooleanList.get(position)) {
					imageView.setBackgroundResource(R.drawable.collect_button);
					BooleanList.set(position, false);
				} else {
					imageView
							.setBackgroundResource(R.drawable.collect_button_select);
					BooleanList.set(position, true);
				}
			}
			break;

		default:
			break;
		}
	}
}
