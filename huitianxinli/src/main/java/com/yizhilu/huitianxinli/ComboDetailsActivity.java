package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.BLDownLoadSelectActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.fragment.PackageCourseFragment;
import com.yizhilu.fragment.PackageDescriptionFragment;
import com.yizhilu.fragment.PackageReviewsFragment;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.NetWorkUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2016-7-16 下午4:09:29
 * 类说明:套餐详情页
 */
public class ComboDetailsActivity extends BaseActivity implements OnPageChangeListener {
	private int comboId,userId;//套餐课程Id,用户ID
	private AsyncHttpClient httpClient;  //联网获取数据
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private LinearLayout backLayout; // 返回
	private static ComboDetailsActivity comboDetailsActivity;
	private List<Fragment> fragments; // fragment的集合
	private ImageLoader imageLoader; // 加载图片的对象
	//-----------------------------------------------------------------------
	private TextView course_introduce, course_zhang, course_discuss; // 套餐描述,套餐课程,套餐评论
	private Boolean successCourse;//判断套餐是否可以评论
	private LinearLayout  shareLayout,opinionLayout,bottomBackLayout; //分享,意见,底部的返回
	private ViewPager viewPager; // 滑动的切换套餐描述,套餐课程,套餐评论
	private PackageReviewsFragment packageReviewsFragment;//套餐评论
	private PackageDescriptionFragment packageDescriptionFragment;//套餐描述
	private PackageCourseFragment packageCourseFragment;//套餐课程
	private ShareDialog shareDialog; // 分享的dialog
	private PublicEntity publicEntity; // 課程詳情的總实体
	private boolean fav = false; // 是否收藏过(没收藏)
	private EntityCourse course;//课程详情
	private ImageView courseImage,video_back,collect_image;//课程图片，课程返回键,收藏图片
	private TextView video_title;
	private LinearLayout  collectLayout;
	private Intent intent; // 意图对象
	private boolean wifi,isWifi;
	private LinearLayout downLoadLayout;
	private LinearLayout title_switch;//标题切换
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_combo_details);
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	//初始化
	@Override
	public void initView() {
		comboDetailsActivity=this;
		intent=new Intent();//意图对象
		isWifi = getSharedPreferences("wifi", MODE_PRIVATE).getBoolean("wifi",
				true); // 是否是在wifi下下载和观看视频
		progressDialog = new ProgressDialog(ComboDetailsActivity.this);  //加载数据显示
		httpClient = new AsyncHttpClient();  //联网获取数据
		backLayout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		fragments = new ArrayList<Fragment>(); // 存放fragment的集合
		imageLoader = ImageLoader.getInstance(); // 加载图片的对象
		
		viewPager = (ViewPager) findViewById(R.id.viewPager); // 滑动的切换套餐描述,套餐课程,套餐评论
		course_introduce = (TextView) findViewById(R.id.course_introduce); // 套餐描述
		course_zhang = (TextView) findViewById(R.id.course_zhang); // 套餐课程
		course_discuss = (TextView) findViewById(R.id.course_discuss); // 套餐评论
		
		shareLayout = (LinearLayout) findViewById(R.id.shareLayout); // 分享
		opinionLayout = (LinearLayout) findViewById(R.id.opinionLayout); // 意见
		bottomBackLayout = (LinearLayout) findViewById(R.id.bottomBackLayout); // 底部的返回
		courseImage = (ImageView) findViewById(R.id.courseImage); // 课程的图片
		video_back=(ImageView) findViewById(R.id.video_back);//课程返回键
		collect_image=(ImageView) findViewById(R.id.collect_image);
		video_title=(TextView) findViewById(R.id.video_title);
		collectLayout=(LinearLayout) findViewById(R.id.collectLayout);
		downLoadLayout=(LinearLayout) findViewById(R.id.downLoadLayout);
		title_switch=(LinearLayout) findViewById(R.id.title_switch);
		// 是否能分享可以评论
		getShare();
		
	}
	
	/**
	 * @author bin 修改人: 时间:2015-10-23 下午1:39:24 方法说明:获取课程详情的方法
	 */
	private void getCourseDetails(int courseId, final int userId) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		Log.i("wulala", Address.COURSE_DETAILS + "?" + params.toString()+"课程详情");
		httpClient.post(Address.COURSE_DETAILS, params,
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
//							try {
								publicEntity = JSON.parseObject(data,
										PublicEntity.class);
								String message = publicEntity.getMessage();
								if (publicEntity.isSuccess()) {
									fav = publicEntity.getEntity().isFav();
									Log.i("lala", fav + "..");
									if (userId == 0) {
										collect_image.setBackgroundResource(R.drawable.collect);
									} else {
										if (fav) { // 不可以收藏
											collect_image.setBackgroundResource(R.drawable.collect);
										} else { // 可以收藏
											collect_image.setBackgroundResource(R.drawable.collect_yes);
										}
									}
									course = publicEntity.getEntity().getCourse();
									imageLoader.displayImage(Address.IMAGE_NET+ course.getMobileLogo(),courseImage, HttpUtils.getDisPlay());
									video_title.setText(course.getName());
									// 初始化fragment的方法
									initFragments();
									Bundle bundle = new Bundle();
									bundle.putSerializable("publicEntity",
											publicEntity);
									packageDescriptionFragment
											.setArguments(bundle); // 描述介绍
									packageCourseFragment.setArguments(bundle); // 套餐课程
									if (successCourse) {
										packageReviewsFragment
												.setArguments(bundle); // 讨论
									}
									viewPager.setOffscreenPageLimit(fragments.size());
									viewPager.setAdapter(new CoursePagerAdapater(getSupportFragmentManager(),fragments));
								} else {
									ConstantUtils.showMsg(ComboDetailsActivity.this,message);
								}
//							} catch (Exception e) {
//							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}
	
	// 是否能分享
		private void getShare() {
			Log.i("lala", Address.WEBSITE_VERIFY_LIST);
			httpClient.get(Address.WEBSITE_VERIFY_LIST,
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
									boolean success = parseObject.isSuccess();
									EntityPublic entity = parseObject.getEntity();
									String verifyTranspond = entity
											.getVerifyTranspond();
									String verifyCourseDiscuss = entity
											.getVerifyCourseDiscuss();
									if (success) {
										if (verifyTranspond.equals("ON")) {
											shareLayout.setVisibility(View.VISIBLE);
										} else {
											shareLayout.setVisibility(View.GONE);
										}
										if (verifyCourseDiscuss.equals("ON")) {
											successCourse = true;
											course_discuss
													.setVisibility(View.VISIBLE);
											course_zhang
													.setBackgroundResource(R.drawable.details_center_null);
										} else {
											successCourse = false;
											course_discuss.setVisibility(View.GONE);
										}
										title_switch.setVisibility(View.VISIBLE);
										// 联网获取课程套餐详情的方法
										getCourseDetails(comboId, userId);
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
	
	/**
	 * @author bin 修改人: 时间:2015-10-24 下午6:59:21 方法说明:单例模式获取本实例
	 * @return
	 */
	public static ComboDetailsActivity getInstence() {
		if (comboDetailsActivity == null) {
			comboDetailsActivity = new ComboDetailsActivity();
		}
		return comboDetailsActivity;
	}
	
	//传过来的数据
	private void getIntentMessage() {
		Intent intent = getIntent();
		comboId = intent.getIntExtra("comboId", 0);//套餐课程Id
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0);//用户ID
	}
	//添加点击事件 
	@Override
	public void addOnClick() {
		course_introduce.setOnClickListener(this); //  套餐描述
		course_zhang.setOnClickListener(this); // 套餐课程
		course_discuss.setOnClickListener(this); // 套餐评论
		viewPager.setOnPageChangeListener(this); // 滑动事件
		
		opinionLayout.setOnClickListener(this); // 意见
		bottomBackLayout.setOnClickListener(this); // 底部的返回
		shareLayout.setOnClickListener(this); // 分享
		video_back.setOnClickListener(this);//顶部返回键
		collectLayout.setOnClickListener(this);//收藏
		downLoadLayout.setOnClickListener(this);//下载
	}
	/**
	 * @author bin 修改人: 时间:2015-10-19 下午1:34:40 方法说明:初始化fragment
	 */
	private void initFragments() {
		fragments.add(packageDescriptionFragment=PackageDescriptionFragment.getInstance()); // 套餐描述
		fragments.add(packageCourseFragment=PackageCourseFragment.getInstance()); // 套餐课程
		if (successCourse) {
			fragments.add(packageReviewsFragment=PackageReviewsFragment.getInstance()); //套餐讨论
		}
	}
	
	//--------viewpage的滑事件------------------------------------------
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int position) {
		setBackGroud();
		switch (position) {
		case 0:
			course_introduce.setBackgroundResource(R.drawable.details_left);
			course_introduce.setTextColor(getResources()
					.getColor(R.color.White));
			break;
		case 1:
			if (successCourse) {
				course_zhang.setBackgroundResource(R.drawable.details_center);
			} else {
				course_zhang.setBackgroundResource(R.drawable.details_right);
			}
			course_zhang.setTextColor(getResources().getColor(R.color.White));
			break;
		case 2:
			course_discuss.setBackgroundResource(R.drawable.details_right);
			course_discuss.setTextColor(getResources().getColor(R.color.White));
			break;
		default:
			break;
		}
		
	}
	//--------viewpage的滑事件------------------------------------------
	
	//viewpage的适配器
	class CoursePagerAdapater extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public CoursePagerAdapater(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.course_introduce: // 套餐描述
			setBackGroud();
			course_introduce.setBackgroundResource(R.drawable.details_left);
			course_introduce.setTextColor(getResources()
					.getColor(R.color.White));
			viewPager.setCurrentItem(0);
			break;
		case R.id.course_zhang: // 套餐课程
			setBackGroud();
			if (successCourse) {
				course_zhang.setBackgroundResource(R.drawable.details_center);
			} else {
				course_zhang.setBackgroundResource(R.drawable.details_right);
			}
			course_zhang.setTextColor(getResources().getColor(R.color.White));
			viewPager.setCurrentItem(1);
			break;
		case R.id.course_discuss: // 套餐评论
			setBackGroud();
			course_discuss.setBackgroundResource(R.drawable.details_right);
			course_discuss.setTextColor(getResources().getColor(R.color.White));
			viewPager.setCurrentItem(2);
			break;
		case R.id.opinionLayout: // 意见
			intent.setClass(ComboDetailsActivity.this,
					OpinionFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.bottomBackLayout: // 底部的返回
			this.finish();
			break;
		case R.id.shareLayout: // 分享
			if (!publicEntity.isSuccess()) {
				return;
			}
			if (shareDialog == null) {
				shareDialog = new ShareDialog(this, R.style.custom_dialog);
				shareDialog.setCancelable(false);
				shareDialog.show();
				shareDialog.setEntityCourse(publicEntity.getEntity()
						.getCourse(), true, false, false);
			} else {
				shareDialog.show();
			}
			break;
		case R.id.video_back: // 顶部的返回
			this.finish();
			break;
		case R.id.collectLayout: // 收藏
			if (userId == 0) {
				intent.setClass(ComboDetailsActivity.this,
						LoginActivity.class);
				startActivity(intent);
			} else {
				if (fav) { 
					// 调用收藏的方法
					getAddCourseCollect(comboId, userId);
				} else {// 可收藏
					// 取消收藏
					cancelCollect(userId, comboId);
				}
			}
			break;
		case R.id.downLoadLayout: // 下载
			if (publicEntity.getEntity() == null) {
				return;
			}
			wifi = NetWorkUtils.isWIFI(ComboDetailsActivity.this);
			if (isWifi) {
				if (!wifi) {
					ConstantUtils.showMsg(ComboDetailsActivity.this,
							"请在wifi下观看和下载");
					return;
				}
			}
			if (course.getIsPay() == 1) {
				if (publicEntity.getEntity().isIsok()) {
					Intent downLoad_intent = new Intent(
							ComboDetailsActivity.this,
							BLDownLoadSelectActivity.class);
					downLoad_intent.putExtra("publicEntity", publicEntity);
					startActivity(downLoad_intent);
				} else {
					Toast.makeText(ComboDetailsActivity.this, "请先购买",
							Toast.LENGTH_SHORT).show();
				}
			} else if (course.getIsPay() == 0) {
				Intent downLoad_intent = new Intent(
						ComboDetailsActivity.this,
						BLDownLoadSelectActivity.class);
				downLoad_intent.putExtra("publicEntity", publicEntity);
				startActivity(downLoad_intent);
			}
			break;
		}
	}
	
	/**
	 * @author bin 修改人: 时间:2015-10-28 下午5:26:24 方法说明:设置课程介绍等按钮的背景
	 */
	private void setBackGroud() {
		course_introduce.setBackgroundResource(R.drawable.details_left_null);
		if (successCourse) {
			course_zhang.setBackgroundResource(R.drawable.details_center_null);
		} else {
			course_zhang.setBackgroundResource(R.drawable.details_right_null);
		}
		course_discuss.setBackgroundResource(R.drawable.details_right_null);
		course_introduce.setTextColor(getResources().getColor(R.color.Blue));
		course_zhang.setTextColor(getResources().getColor(R.color.Blue));
		course_discuss.setTextColor(getResources().getColor(R.color.Blue));
	}
	
	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午11:09:02 方法说明:连接网络获取添加课程收藏的方法
	 */
	private void getAddCourseCollect(int courseId, int userId) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		httpClient.post(Address.ADD_COURSE_COLLECT, params,
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
								PublicEntity collectEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = collectEntity.getMessage();
								ConstantUtils.showMsg(
										ComboDetailsActivity.this, message);
								fav = false;
								collect_image
										.setBackgroundResource(R.drawable.collect_yes);
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
	
	/**
	 * @author bin 修改人: 时间:2016-4-8 上午9:54:15 方法说明:取消收藏的方法
	 */
	private void cancelCollect(int userId, int courseId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("courseId", courseId);
		Log.i("lala", Address.DELETE_COURSE_COLLECT + "?" + params);
		httpClient.post(Address.CANCEL_COLLECT, params,
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
								PublicEntity entity = JSON.parseObject(data,
										PublicEntity.class);
								String message = entity.getMessage();
								if (entity.isSuccess()) {
									ConstantUtils.showMsg(
											ComboDetailsActivity.this,
											message);
									fav = true;
									collect_image
											.setBackgroundResource(R.drawable.collect);
								} else {
									ConstantUtils.showMsg(
											ComboDetailsActivity.this,
											message);
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
}
