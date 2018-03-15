package com.yizhilu.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.HotSearchAdapter;
import com.yizhilu.adapter.SearchResultsArticleAdapter;
import com.yizhilu.adapter.SearchResultsCourseAdapter;
import com.yizhilu.adapter.SearchResultsGroupAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.InformationDetailsActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.view.NoScrollListView;

/**
 * @author ming 修改人: 时间:2015年10月21日 上午11:45:38 类说明:搜索的类——小组
 */
public class SearchActivityCommuntiy extends BaseActivity {

	private TextView tv_cancel, tv_sure;
	private ListView lv_search;
	private NoScrollListView lv_kecheng, lv_wenzhang;
	private EditText ed_search;
	private String content;
	private LinearLayout layout_search, layout_search_ing;
	private List<EntityCourse> courses;
	private List<EntityCourse> myList;
	private List<EntityCourse> courseSearch;
	private List<EntityCourse> courseSearchList;
	private List<EntityCourse> articleSearch;
	private List<EntityCourse> articleSearchList;
	private List<EntityPublic> groupList;
	private List<EntityPublic> topicList;
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private LinearLayout back, show_course_img, show_article_img; // 返回 课程默认图片
																	// 文章默认图片
	private TextView title; // 标题
	private boolean groupSearsh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_search_communtiy);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		groupSearsh = intent.getBooleanExtra("group", false);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		lv_search = (ListView) findViewById(R.id.lv_search);
		lv_kecheng = (NoScrollListView) findViewById(R.id.lv_kecheng);
		lv_wenzhang = (NoScrollListView) findViewById(R.id.lv_wenzhang);
		ed_search = (EditText) findViewById(R.id.ed_search);
		layout_search = (LinearLayout) findViewById(R.id.layout_search);
		layout_search_ing = (LinearLayout) findViewById(R.id.layout_search_ing);
		layout_search.setVisibility(View.VISIBLE);
		layout_search_ing.setVisibility(View.GONE);
		httpClient = new AsyncHttpClient();
		myList = new ArrayList<EntityCourse>();
		courseSearchList = new ArrayList<EntityCourse>();
		articleSearchList = new ArrayList<EntityCourse>();
		groupList = new ArrayList<EntityPublic>();
		topicList = new ArrayList<EntityPublic>();
		back = (LinearLayout) findViewById(R.id.back_layout); // 返回
		title = (TextView) findViewById(R.id.title_text); // 标题
		title.setText("搜索");
		show_course_img = (LinearLayout) findViewById(R.id.show_course_linear);// 课程默认图片
		show_article_img = (LinearLayout) findViewById(R.id.show_article_linear); // 文章默认图片
		// 联网获取热门搜索的课程
		if (groupSearsh) {
			ed_search.setHint("话题");
		}
		getHotSearch();
	}

	@Override
	public void addOnClick() {
		tv_cancel.setOnClickListener(this);
		tv_sure.setOnClickListener(this);
		lv_search.setOnItemClickListener(this);
		lv_kecheng.setOnItemClickListener(this);
		lv_wenzhang.setOnItemClickListener(this);
		back.setOnClickListener(this);
		// ed_search.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (hasFocus) {
		//// layout_search.setVisibility(View.GONE);
		//// layout_search_ing.setVisibility(View.VISIBLE);
		// } else {
		// layout_search_ing.setVisibility(View.GONE);
		// layout_search.setVisibility(View.VISIBLE);
		// tv_cancel.setVisibility(View.VISIBLE);
		// tv_sure.setVisibility(View.GONE);
		// courseSearchList.clear();
		// articleSearchList.clear();
		// }
		// }
		// });
		layout_search_ing.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				layout_search_ing.setFocusable(true);
				layout_search_ing.setFocusableInTouchMode(true);
				layout_search_ing.requestFocus();
				return false;
			}
		});
		ed_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_cancel.setVisibility(View.GONE);
				tv_sure.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(ed_search.getText().toString())) {
					tv_cancel.setVisibility(View.VISIBLE);
					tv_sure.setVisibility(View.GONE);
					layout_search.setVisibility(View.VISIBLE);
					layout_search_ing.setVisibility(View.GONE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_cancel:
			this.finish();
			break;
		case R.id.tv_sure:
			courseSearchList.clear();
			articleSearchList.clear();
			topicList.clear();
			groupList.clear();
			content = ed_search.getText().toString();

			if (groupSearsh) {
				getSearchGroupResults(content);
			} else {
				// 获取搜索的课程
				getSearchCoursesResults(content);
				// 获取搜索的文章
				getSearchArticleResults(content);
			}

			layout_search.setVisibility(View.GONE);
			layout_search_ing.setVisibility(View.VISIBLE);
			break;
		case R.id.back_layout: // 返回
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent();
		switch (parent.getId()) {
		case R.id.lv_search:
			intent.setClass(SearchActivityCommuntiy.this, BLCourseDetailsActivity.class);
			intent.putExtra("courseId", myList.get(position).getCourseId());
			startActivity(intent);
			break;
		case R.id.lv_kecheng:
			if (groupSearsh) {
				intent.setClass(SearchActivityCommuntiy.this, GroupDetailActivity.class);
				intent.putExtra("GroupId", groupList.get(position).getId());
				intent.putExtra("position", position);
				startActivity(intent);
			} else {
				intent.setClass(SearchActivityCommuntiy.this, BLCourseDetailsActivity.class);
				intent.putExtra("courseId", courseSearchList.get(position).getId());
				startActivity(intent);
			}

			break;
		case R.id.lv_wenzhang:
			if (groupSearsh) {
				intent.setClass(SearchActivityCommuntiy.this, TopicDetailsActivity.class);
				intent.putExtra("topicId", topicList.get(position).getId());
				intent.putExtra("isTop", topicList.get(position).getTop());
				intent.putExtra("isEssence", topicList.get(position).getEssence());
				intent.putExtra("isFiery", topicList.get(position).getFiery());
				startActivity(intent);
			} else {
				intent.setClass(SearchActivityCommuntiy.this, InformationDetailsActivity.class);
				intent.putExtra("informationTitle", "搜索文章");
				intent.putExtra("entity", articleSearchList.get(position));
				startActivity(intent);
			}
			break;
		}
	}

	/**
	 * 
	 * @author ming 修改人: 时间:2015年10月21日 下午2:18:25 方法说明:获取热门搜索
	 */
	private void getHotSearch() {
		httpClient.get(Address.RECOMMEND_COURSE, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						JSONObject object = new JSONObject(data);
						boolean success = object.getBoolean("success");
						if (success) {
							JSONObject entityObject = object.getJSONObject("entity");
							if (entityObject.toString().indexOf("\"1\"") > -1) {
								JSONArray jsonArray = entityObject.getJSONArray("1");
								if (jsonArray != null && jsonArray.length() > 0) {
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject = jsonArray.getJSONObject(i);
										EntityCourse entityCourse = JSON.parseObject(jsonObject.toString(),
												EntityCourse.class);
										myList.add(entityCourse);
									}
								}
							}
							// if (entityObject.toString().indexOf("\"2\"") >
							// -1) {
							// JSONArray hotrecommend = entityObject
							// .getJSONArray("2");
							// if (hotrecommend != null
							// && hotrecommend.length() > 0) {
							// for (int i = 0; i < hotrecommend.length(); i++) {
							// JSONObject jsonObject = hotrecommend
							// .getJSONObject(i);
							// EntityCourse entityCourse = JSON
							// .parseObject(
							// jsonObject.toString(),
							// EntityCourse.class);
							// myList
							// .add(entityCourse);
							// }
							// }
							// }
							lv_search.setAdapter(new HotSearchAdapter(SearchActivityCommuntiy.this, myList));
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-11-30 下午2:58:13 方法说明:获取搜课程的方法
	 */
	private void getSearchCoursesResults(String content) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		Log.i("lala", Address.SEACRH + "?" + params.toString());
		httpClient.post(Address.SEACRH, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if (publicEntity.isSuccess()) {

							courseSearch = publicEntity.getEntity().getCourseList();
							Log.i("shuang", courseSearch.size() + "--kecheng-size---");
							if (courseSearch != null && courseSearch.size() > 0) {
								for (int i = 0; i < courseSearch.size(); i++) {
									courseSearchList.add(courseSearch.get(i));
								}
								lv_kecheng.setAdapter(
										new SearchResultsCourseAdapter(SearchActivityCommuntiy.this, courseSearchList));
								show_course_img.setVisibility(View.GONE);
							} else {
								lv_kecheng.setAdapter(
										new SearchResultsCourseAdapter(SearchActivityCommuntiy.this, courseSearchList));
								show_course_img.setVisibility(View.VISIBLE);
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	// 社区搜索
	private void getSearchGroupResults(String content) {
		RequestParams params = new RequestParams();
		params.put("searchKey", content);
		Log.i("xm", "http://sns1.268xue.com/app/searchGroupTopic" + "?" + params.toString());
		httpClient.post("http://sns1.268xue.com/app/searchGroupTopic", params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							List<EntityPublic> tempTopic = publicEntity.getEntity().getTopicList();
							List<EntityPublic> tempGroup = publicEntity.getEntity().getGroupList();
							if (tempTopic != null && tempTopic.size() > 0) {
								topicList.addAll(tempTopic);
								lv_wenzhang.setAdapter(
										new SearchResultsGroupAdapter(SearchActivityCommuntiy.this, topicList, false));
							}
							if (tempGroup != null && tempGroup.size() > 0) {
								groupList.addAll(tempGroup);
								lv_kecheng.setAdapter(
										new SearchResultsGroupAdapter(SearchActivityCommuntiy.this, groupList, true));
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-11-30 下午2:58:33 方法说明:获取搜索文章的方法
	 */
	private void getSearchArticleResults(String content) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		httpClient.post(Address.SEACRH, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							articleSearch = publicEntity.getEntity().getArticleList();
							if (articleSearch != null && articleSearch.size() > 0) {
								for (int i = 0; i < articleSearch.size(); i++) {
									articleSearchList.add(articleSearch.get(i));
								}
								lv_wenzhang.setAdapter(new SearchResultsArticleAdapter(SearchActivityCommuntiy.this,
										articleSearchList));
								show_article_img.setVisibility(View.GONE);
							} else {
								lv_wenzhang.setAdapter(new SearchResultsArticleAdapter(SearchActivityCommuntiy.this,
										articleSearchList));
								show_article_img.setVisibility(View.VISIBLE);
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}
}
