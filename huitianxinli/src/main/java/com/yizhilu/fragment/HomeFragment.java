package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.RecommendAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.BannerEntity;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.ComboDetailsActivity;
import com.yizhilu.huitianxinli.InformationDetailsActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.MeViewPager;
import com.yizhilu.view.NoScrollGridView;

/**
 * @author bin 修改人: 时间:2015-10-15 下午1:57:54 类说明:首页的类
 */
public class HomeFragment extends BaseFragment implements OnPageChangeListener {
	private static HomeFragment homeFragment; // 本类对象
	private View inflate; // 首页的总布局
	private PullToRefreshScrollView refreshScrollView; // 上拉加载,下拉刷新的控件
	private MeViewPager meViewPager; // 切换广告图的控件
	private TextView bannerText; // 广告图的名字的控件
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private List<BannerEntity> centerBanner; // 广告图的集合
	private ImageView[] roundViews; // 小圆点的集合
	private List<ImageView> imageList; // banner图的集合
	private List<EntityCourse> announcementList, recommendCourseList, recommendHotCourseList; // 公告的集合,小编推荐课程的集合,热门推荐
	private boolean isBanner;
	private int select = 0, announcementSelect = 0; // 广告图的当前的位置,公告的下标
	private TextView announcementText; // 公告的textView
	private NoScrollGridView recommendGridView, recommend_HotGridView; // 小编推荐课程的控件,热门推荐
	private ImageView small_recommend_more, hot_recommend_more; // 小编更多,推荐更多
	private OnSelectPageLisenner onSelectPageLisenner; // 接口的对象
	private boolean isAnnoun; // 公告的线程

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onSelectPageLisenner = (OnSelectPageLisenner) activity;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/**
	 * @author bin 修改人: 时间:2015-10-27 下午4:33:04 类说明:点击小编更多和热门更多主页面实现的方法
	 */
	public interface OnSelectPageLisenner {
		public void onSelectPage(int page);
	}

	public void setOnSelectPageLisenner(OnSelectPageLisenner onSelectPageLisenner) {
		this.onSelectPageLisenner = onSelectPageLisenner;
	}

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_home, container, false);
		return inflate;
	}

	/**
	 * 获取本类对象的方法
	 */
	public static HomeFragment getInstance() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}
		return homeFragment;
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {

		progressDialog = new ProgressDialog(getActivity()); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		imageList = new ArrayList<ImageView>(); // banner图的集合
		recommendCourseList = new ArrayList<EntityCourse>(); // 小编推荐的集合
		recommendHotCourseList = new ArrayList<EntityCourse>(); // 热门推荐的集合
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView); // 上拉加载,下拉刷新的控件
		meViewPager = (MeViewPager) inflate.findViewById(R.id.meViewPager); // 切换广告图的控件
		meViewPager.setMyContext(getActivity());
		bannerText = (TextView) inflate.findViewById(R.id.bannerText); // 广告图的名字的控件
		announcementText = (TextView) inflate.findViewById(R.id.announcementText); // 公告的textView
		small_recommend_more = (ImageView) inflate.findViewById(R.id.small_recommend_more); // 小编更多
		hot_recommend_more = (ImageView) inflate.findViewById(R.id.hot_recommend_more); // 推荐更多
		recommendGridView = (NoScrollGridView) inflate.findViewById(R.id.recommendGridView); // 小编推荐课程的控件
		recommend_HotGridView = (NoScrollGridView) inflate.findViewById(R.id.recommend_HotGridView); // 热门推荐的课程
		// 获取广告图的方法
		getBannerData();
		// 获取公告的方法
		getAnnouncementData();
		// 获取小编推荐和热门推荐的课程
		getRecommend_Course();
	}

	/**
	 * @author bin 修改人: 时间:2015-10-12 下午5:46:09 方法说明:联网获取首页公告的方法
	 */
	private void getAnnouncementData() {
		httpClient.get(Address.ANNOUNCEMENT, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				getActivity().getSharedPreferences("Announ", getActivity().MODE_PRIVATE).edit()
						.putString("announ", data).commit();
				// 解析公告的方法
				getParserAnnoun(data);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
				String data = getActivity().getSharedPreferences("Announ", getActivity().MODE_PRIVATE)
						.getString("announ", "");
				// 解析公告的方法
				getParserAnnoun(data);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2016-3-22 下午5:17:41 方法说明:解析公告的方法
	 */
	private void getParserAnnoun(String data) {
		if (!TextUtils.isEmpty(data)) {
			try {
				CourseEntity courseEntity = JSON.parseObject(data, CourseEntity.class);
				if (courseEntity.isSuccess()) {
					announcementList = courseEntity.getEntity();
					if (announcementList != null && announcementList.size() > 0) {
						announcementText.setText(announcementList.get(announcementSelect).getTitle());
						if (!isAnnoun) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									while (true) {
										try {
											// 每5秒自动换图
											Thread.sleep(3000);
											announcementSelect++;
											if (announcementSelect > announcementList.size() - 1) {
												announcementSelect = 0;
											}
											getActivity().runOnUiThread(new Runnable() {

												@Override
												public void run() {
													announcementText.setText(
															announcementList.get(announcementSelect).getTitle());
												}
											});
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}).start();
							isAnnoun = true;
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-12 下午5:30:04 方法说明:获取小编推荐课程的方法
	 */
	private void getRecommend_Course() {
		httpClient.get(Address.RECOMMEND_COURSE, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				getActivity().getSharedPreferences("Recommend", getActivity().MODE_PRIVATE).edit()
						.putString("recommend", data).commit();
				// 解析推荐课程等
				getParserRecommend(data);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
				refreshScrollView.onRefreshComplete();
				String data = getActivity().getSharedPreferences("Recommend", getActivity().MODE_PRIVATE)
						.getString("recommend", "");
				// 解析推荐课程等
				getParserRecommend(data);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2016-3-22 下午5:23:56 方法说明:解析推荐课程的方法
	 */
	private void getParserRecommend(String data) {
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
								EntityCourse entityCourse = JSON.parseObject(jsonObject.toString(), EntityCourse.class);
								recommendCourseList.add(entityCourse);
							}
							recommendGridView.setAdapter(new RecommendAdapter(getActivity(), recommendCourseList));
						}
					}
					if (entityObject.toString().indexOf("\"2\"") > -1) {
						JSONArray hotrecommend = entityObject.getJSONArray("2");
						if (hotrecommend != null && hotrecommend.length() > 0) {
							for (int i = 0; i < hotrecommend.length(); i++) {
								JSONObject jsonObject = hotrecommend.getJSONObject(i);
								EntityCourse entityCourse = JSON.parseObject(jsonObject.toString(), EntityCourse.class);
								recommendHotCourseList.add(entityCourse);
							}
							recommend_HotGridView
									.setAdapter(new RecommendAdapter(getActivity(), recommendHotCourseList));
						}
					}
					refreshScrollView.onRefreshComplete();
				}
			} catch (Exception e) {
				refreshScrollView.onRefreshComplete();
			}
		}
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		refreshScrollView.setOnRefreshListener(this); // 绑定上拉加载,下拉刷新的事件
		meViewPager.setOnPageChangeListener(this); // 绑定meViewPager的滑动事件
		recommendGridView.setOnItemClickListener(this); // 小编推荐
		announcementText.setOnClickListener(this); // 公告的点击事件
		small_recommend_more.setOnClickListener(this); // 小编推荐更多
		hot_recommend_more.setOnClickListener(this); // 热门推荐更多
		recommend_HotGridView.setOnItemClickListener(this); // 热门推荐
	}

	/**
	 * @author bin 修改人: 时间:2015-10-12 下午4:54:51 方法说明:获取广告图的方法
	 */
	private void getBannerData() {
		Log.i("wulala", Address.BANNER);
		httpClient.get(Address.BANNER, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				boolean aaa = getActivity().getSharedPreferences("banner", getActivity().MODE_PRIVATE).edit()
						.putString("banner", data).commit();
				// 解析banner公告的方法
				getParserBanner(data);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
				 String data = getActivity().getSharedPreferences("banner",
				 Context.MODE_PRIVATE).getString("banner", "");
				 //解析banner公告的方法
				 getParserBanner(data);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2016-3-22 下午4:56:36 方法说明:解析banner公告的方法
	 */
	private void getParserBanner(String data) {
		if (!TextUtils.isEmpty(data)) {
			try {
				PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
				if (publicEntity.isSuccess()) {
					centerBanner = publicEntity.getEntity().getIndexCenterBanner();
					if (centerBanner == null || centerBanner.size() <= 0) {
						return;
					}
					meViewPager.setSelltype(centerBanner.get(0).getSellType());
					meViewPager.setCourseId(centerBanner.get(0).getCourseId());
					bannerText.setText(centerBanner.get(0).getTitle());
					ViewGroup group = (ViewGroup) inflate.findViewById(R.id.roundLayout);
					roundViews = new ImageView[centerBanner.size()];
					for (int i = 0; i < centerBanner.size(); i++) {
						ImageView imageView = new ImageView(getActivity());
						imageView.setLayoutParams(new LayoutParams(10, 10));
						roundViews[i] = imageView;
						if (i == 0) {
							roundViews[0].setBackgroundResource(R.drawable.chooesbar);
						} else {
							roundViews[i].setBackgroundResource(R.drawable.nochooese);
						}
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						layoutParams.leftMargin = 10;
						layoutParams.rightMargin = 10;
						group.addView(imageView, layoutParams);
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							isBanner = true;
							Bitmap bitmap_image;
							for (int j = 0; j < centerBanner.size(); j++) {
								bitmap_image = loadImageDisplay(Address.IMAGE_NET + centerBanner.get(j).getImagesUrl());
								
								
								if (bitmap_image != null) {
									ImageView imageView = new ImageView(getActivity());
									imageView.setScaleType(ScaleType.FIT_XY);
									imageView.setImageBitmap(bitmap_image);
									imageList.add(imageView);
								}
							}
							if (imageList != null) {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										meViewPager.setAdapter(meViewAdapter);
										if (isBanner) {
											// 轮播广告图的方法
											scoPic();
										}
									}
								});
							} else {
							}
						}
					}).start();
				}
			} catch (Exception e) {
				Log.i("wulala", e.toString());
			}
		}
	}

	/**
	 * meViewPager的适配器
	 */
	private PagerAdapter meViewAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View) arg1;
		}

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(imageList.get(position));
			return imageList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(imageList.get(position));
		}

	};

	/**
	 * 轮播广告图的方法
	 */
	public void scoPic() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Message msg = mhandler.obtainMessage();
						msg.arg1 = select;
						mhandler.sendMessage(msg);
						// 每5秒自动换图
						Thread.sleep(5000);
						select++;
						if (select > centerBanner.size() - 1) {
							select = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Viewpager每次轮循的处理
	 */
	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			meViewPager.setCurrentItem(msg.arg1);
			if (centerBanner != null && centerBanner.size() > 0) {
				bannerText.setText(centerBanner.get(msg.arg1).getTitle());
			}
		};
	};

	/**
	 * @author bin 修改人: 时间:2015-10-15 下午6:09:58 方法说明:联网获取图片的方法
	 */
	public Bitmap loadImageDisplay(final String string) {
		Bitmap bitmap_new = null;
		if (string != null) {
			bitmap_new = HttpUtils.getBitmap(string);
		}
		return bitmap_new;

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		try {

			for (int i = 0; i < roundViews.length; i++) {
				if (i == arg0) {
					roundViews[i].setBackgroundResource(R.drawable.chooesbar);
				} else {
					roundViews[i].setBackgroundResource(R.drawable.nochooese);
				}
			}
			if (centerBanner.size() > 0) {
				bannerText.setText(centerBanner.get(arg0).getTitle());
			}
			if (centerBanner.size() > 0) {
				meViewPager.setSelltype(centerBanner.get(arg0).getSellType());
				meViewPager.setCourseId(centerBanner.get(arg0).getCourseId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent();
		switch (parent.getId()) {
		case R.id.recommendGridView: // 小编推荐
			String type = recommendCourseList.get(position).getSellType();
			if (type.equals("COURSE")) {
				intent.setClass(getActivity(), BLCourseDetailsActivity.class);
				intent.putExtra("courseId", recommendCourseList.get(position).getCourseId());
				startActivity(intent);
			} else if (type.equals("PACKAGE")) {
				intent.setClass(getActivity(), ComboDetailsActivity.class);
				intent.putExtra("comboId", recommendCourseList.get(position).getCourseId());
				startActivity(intent);
			}
			break;
		case R.id.recommend_HotGridView: // 热门推荐
			String type2 = recommendHotCourseList.get(position).getSellType();
			if (type2.equals("COURSE")) {
				intent.setClass(getActivity(), BLCourseDetailsActivity.class);
				intent.putExtra("courseId", recommendHotCourseList.get(position).getCourseId());
				startActivity(intent);
			} else if (type2.equals("PACKAGE")) {
				intent.setClass(getActivity(), ComboDetailsActivity.class);
				intent.putExtra("comboId", recommendHotCourseList.get(position).getCourseId());
				startActivity(intent);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.announcementText: // 公告的点击事件
			if (announcementList != null && announcementList.size() > 0) {
				// int informationId =
				// announcementList.get(announcementSelect).getId();
				Intent intent = new Intent();
				intent.setClass(getActivity(), InformationDetailsActivity.class);
				intent.putExtra("entity", announcementList.get(announcementSelect));
				// intent.putExtra("informationId", informationId);
				intent.putExtra("informationTitle", "公告详情");
				startActivity(intent);
			}
			break;
		case R.id.small_recommend_more: // 小编推荐更多
			onSelectPageLisenner.onSelectPage(1);
			break;
		case R.id.hot_recommend_more: // 热门推荐更多
			onSelectPageLisenner.onSelectPage(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		recommendCourseList.clear();
		recommendHotCourseList.clear();
		if (!isBanner) {
			// 获取广告图的方法
			getBannerData();
		}
		// 获取小编推荐的课程
		getRecommend_Course();
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
	}
}
