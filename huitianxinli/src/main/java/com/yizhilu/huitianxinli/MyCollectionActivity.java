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
import com.yizhilu.adapter.CollectionAdapter;
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
 * @author bin 修改人: 时间:2015-10-26 下午2:17:18 类说明:课程收藏的类
 */
public class MyCollectionActivity extends BaseActivity {
	private LinearLayout back_layout; // 返回的布局
	private PullToRefreshScrollView refreshScrollView; // 下拉刷新,上拉加载的对象
	private NoScrollListView listview; // 收藏课程的列表
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 联网获取数据显示的dialog
	private int currentPage = 1, userId; // 当前页数，用户Id
	private List<EntityCourse> collectList; // 收藏课程的集合
	private TextView collection_image_edit; // 编辑
	private LinearLayout collection_del_null, null_layout; // 删除的布局,没有数据显示的布局
	private boolean temp = true, isClear; // true代表完成状态,false代表编辑状态,是否是清空
	private CollectionAdapter collectionAdapter;
	private TextView collection_empty, collection_delete; // 清空,删除
	private List<Boolean> BooleanList; // 存放收藏是否选中的标识
	private Dialog dialog;
	private Object type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_collection);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0); // 得到用户Id
		collectList = new ArrayList<EntityCourse>(); // 收藏课程的集合
		BooleanList = new ArrayList<Boolean>(); // 存放收藏是否选中的标识
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		progressDialog = new ProgressDialog(this); // 联网获取数据显示的dialog
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView); // 下拉刷新,上拉加载的对象
		refreshScrollView.setMode(Mode.BOTH); // 设置加载模式
		listview = (NoScrollListView) findViewById(R.id.listView_collection); // 收藏课程的列表
		collection_image_edit = (TextView) findViewById(R.id.collection_image_edit); // 编辑
		collection_del_null = (LinearLayout) findViewById(R.id.collection_del_null);
		collection_empty = (TextView) findViewById(R.id.collection_empty);
		collection_delete = (TextView) findViewById(R.id.collection_delete);
		null_layout = (LinearLayout) findViewById(R.id.null_layout); // 没有数据显示的布局
		// 解析收藏课程
		getCourseCollect_list(currentPage, userId);
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		refreshScrollView.setOnRefreshListener(this); // 下拉刷新,上拉加载的对象
		collection_image_edit.setOnClickListener(this);
		collection_empty.setOnClickListener(this); // 清空
		collection_delete.setOnClickListener(this); // 删除
		listview.setOnItemClickListener(this); // 收藏课程的列表
	}

	// 解析收藏课程
	private void getCourseCollect_list(final int currentPage, int userId) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("userId", userId);
		Log.i("lala", Address.COURSE_COLLECT_LIST + params.toString());
		httpClient.post(Address.COURSE_COLLECT_LIST, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								if (publicEntity.isSuccess()) {
									PageEntity pageEntity = publicEntity
											.getEntity().getPage();
									if (pageEntity.getCurrentPage() <= currentPage) {
										refreshScrollView
												.setMode(Mode.PULL_FROM_START);
									}
									List<EntityCourse> favouriteCourses = publicEntity
											.getEntity().getFavouriteCourses();
									if (favouriteCourses != null
											&& favouriteCourses.size() > 0) {
										for (int i = 0; i < favouriteCourses
												.size(); i++) {
											collectList.add(favouriteCourses
													.get(i));
											BooleanList.add(false);
										}
									}
									if (collectionAdapter == null) {
										collectionAdapter = new CollectionAdapter(
												MyCollectionActivity.this,
												collectList);
										listview.setAdapter(collectionAdapter);
									} else {
										collectionAdapter
												.notifyDataSetChanged();
									}
									if (collectList.isEmpty()) {
										collection_del_null
												.setVisibility(View.GONE);
										temp = true;
										collection_image_edit.setText("编辑");
										null_layout.setVisibility(View.VISIBLE);
										refreshScrollView.setMode(Mode.DISABLED);
									} else {
										null_layout.setVisibility(View.GONE);
									}
									refreshScrollView.onRefreshComplete();
								} else {
									ConstantUtils.showMsg(
											MyCollectionActivity.this, message);
									refreshScrollView.onRefreshComplete();
								}

							} catch (Exception e) {
								refreshScrollView.onRefreshComplete();
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
						refreshScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			MyCollectionActivity.this.finish();
			break;
		case R.id.collection_image_edit:// 编辑
			if (collectList.isEmpty()) {
				return;
			}
			if (temp) {
				collection_image_edit.setText("完成");
				collection_del_null.setVisibility(View.VISIBLE);
				collectionAdapter.showImg(temp);
				collectionAdapter.notifyDataSetChanged();
				temp = false;
			} else {
				collection_image_edit.setText("编辑");
				collection_del_null.setVisibility(View.GONE);
				collectionAdapter.showImg(temp);
				collectionAdapter.notifyDataSetChanged();
				temp = true;
			}
			break;
		case R.id.collection_empty:// 清空
			isClear = true;
			showDeleteDiaLog(null);
			break;
		case R.id.collection_delete:// 删除
			isClear = false;
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < BooleanList.size(); i++) {
				if (BooleanList.get(i)) {
					buffer.append(collectList.get(i).getFavouriteId() + ",");
				}
			}
			if(buffer.length()>0){
				showDeleteDiaLog(buffer.toString());
			}else{
				ConstantUtils.showMsg(MyCollectionActivity.this, "请选择要删除的收藏课程!");
			}
			break;
		}
	}

	// 清空课程
	private void cleanCourse() {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		httpClient.post(Address.COLLECTION_CLEAN, params,
				new TextHttpResponseHandler() {

					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								String message = parseObject.getMessage();
								if (parseObject.isSuccess()) {
									BooleanList.clear();
									collectList.clear();
									getCourseCollect_list(currentPage, userId);
								} else {
									ConstantUtils.showMsg(
											MyCollectionActivity.this, message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}

	// 删除
	private void removeError(final String string) {
		RequestParams params = new RequestParams();
		params.put("ids", string);
		httpClient.post(Address.DELETE_COURSE_COLLECT, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								String message = parseObject.getMessage();
								if (parseObject.isSuccess()) {
									BooleanList.clear();
									collectList.clear();
									getCourseCollect_list(currentPage, userId);
								} else {
									ConstantUtils.showMsg(
											MyCollectionActivity.this, message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		type = collectList.get(position).getSellType();
		if (temp) {
//			int courseId = collectList.get(position).getCourseId();
//			Intent intent = new Intent(MyCollectionActivity.this,
//					BLCourseDetailsActivity.class);
//			intent.putExtra("courseId", courseId);
//			startActivity(intent);
			
			
			
			
			
			if (type.equals("COURSE")) {
				Intent intent = new Intent(MyCollectionActivity.this,
						BLCourseDetailsActivity.class);
				intent.putExtra("courseId", collectList.get(position).getCourseId());
				startActivity(intent);
			} else if (type.equals("PACKAGE")) {
				Intent intent = new Intent(MyCollectionActivity.this,
						ComboDetailsActivity.class);
				intent.putExtra("comboId", collectList.get(position).getCourseId());
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
	}

	// 下拉
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		currentPage = 1;
		collectList.clear();
		BooleanList.clear();
		refreshScrollView.setMode(Mode.BOTH);
		getCourseCollect_list(currentPage, userId);

	}

	// 上拉
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		currentPage++;
		BooleanList.clear();
		getCourseCollect_list(currentPage, userId);
	}

	// 刪除收藏課程時顯示的
	public void showDeleteDiaLog(final String deString) {
		View view = getLayoutInflater().inflate(
				R.layout.dialog_show, null);
		WindowManager manager = (WindowManager)getSystemService(
				Context.WINDOW_SERVICE);
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
		if(isClear){
			textView.setText("您确定要清空吗?");
		}else{
			textView.setText("您确定要刪除吗?");
		}
		linbtnsure.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				if(isClear){
					cleanCourse();
				}else{
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
}
