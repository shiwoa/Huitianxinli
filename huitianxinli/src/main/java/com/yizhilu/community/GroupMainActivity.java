package com.yizhilu.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.fragment.FindFragment;
import com.yizhilu.community.fragment.GroupFragment;
import com.yizhilu.community.fragment.MyFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.exam.ExamActivity;
import com.yizhilu.exam.entity.StringEntity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.MainActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//社区-主界面
public class GroupMainActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {

	private static ViewPager viewPager;
	private List<Fragment> fragments;
	private FragmentAdapter adapter;
	private TextView group_text, find_text, my_text, title, gotoDemo1, textView3;
	private ImageView hot, find, my, img;
	private LinearLayout hot_layout, find_layout, my_layout, main_group;// 热点，发现，我的
																		// 社区文字
	private int userId;// 用户Id
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private EntityPublic userExpandDto; // 用户信息的实体
	private ImageLoader imageLoader; // 加载用户头像
	protected StringEntity publicEntity;
	protected Dialog dialog;

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_group_main);
		super.onCreate(savedInstanceState);
		progressDialog = new ProgressDialog(GroupMainActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		group_text = (TextView) findViewById(R.id.group_text);
		find_text = (TextView) findViewById(R.id.find_text);
		my_text = (TextView) findViewById(R.id.my_text);
		title = (TextView) findViewById(R.id.title);
		title.setVisibility(View.GONE);
		imageLoader = ImageLoader.getInstance(); // 加载头像
		hot = (ImageView) findViewById(R.id.hot);
		find = (ImageView) findViewById(R.id.find);
		my = (ImageView) findViewById(R.id.my);
		main_group = (LinearLayout) findViewById(R.id.main_group);
		main_group.setVisibility(View.VISIBLE);
		my_layout = (LinearLayout) findViewById(R.id.my_layout);
		find_layout = (LinearLayout) findViewById(R.id.find_layout);
		hot_layout = (LinearLayout) findViewById(R.id.hot_layout);
		img = (ImageView) findViewById(R.id.main_img);
		// title.setText("热点");// 设置标题
		gotoDemo1 = (TextView) findViewById(R.id.gotoDemo1);
		gotoDemo1.setVisibility(View.VISIBLE);
		textView3 = (TextView) findViewById(R.id.textView3); // 题库
		textView3.setVisibility(View.VISIBLE);
		fragments = new ArrayList<Fragment>();
		fragments.add(new GroupFragment());// 热点
		fragments.add(new FindFragment());// 发现
		fragments.add(new MyFragment());// 我的
		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(fragments.size());
		viewPager.setAdapter(adapter);
		gotoDemo1.setOnClickListener(this);
		textView3.setOnClickListener(this); // 题库
		hot_layout.setOnClickListener(this);
		find_layout.setOnClickListener(this);
		my_layout.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);
		img.setOnClickListener(this);
		getMy_Message(userId);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.hot_layout:
			viewPager.setCurrentItem(0);
			break;
		case R.id.find_layout:
			viewPager.setCurrentItem(1);
			break;
		case R.id.my_layout:
			// userId==0，如果没有用户登录，进入登录页引导用户登录
			if (userId == 0) {
				intent.setClass(GroupMainActivity.this, LoginActivity.class);
				intent.putExtra("formSNS", true);
				startActivity(intent);
			} else {
				viewPager.setCurrentItem(2);
			}
			break;
		case R.id.gotoDemo1:
			GroupMainActivity.this.finish();
			break;
		case R.id.textView3: // 题库
			if (userId == 0) {
				intent.setClass(GroupMainActivity.this, LoginActivity.class);
				intent.putExtra("formSNS", true);
				startActivity(intent);
			} else {
				ableToExam(userId);

			}
			break;
		case R.id.main_img:
			Intent intent2 = new Intent();
			intent2.setAction("show");
			sendBroadcast(intent2);
			GroupMainActivity.this.finish();
			break;
		default:
			break;
		}
	}

	// 切换到发现页
	public static void gotoFind() {
		viewPager.setCurrentItem(1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			setGroup();
			break;
		case 1:
			setFind();
			break;
		case 2:
			if (userId == 0) {
				Intent intent = new Intent();
				intent.setClass(GroupMainActivity.this, LoginActivity.class);
				intent.putExtra("formSNS", true);
				startActivity(intent);
				viewPager.setCurrentItem(1);
				return;
			}
			setMy();
			break;
		default:
			break;
		}
	}

	class FragmentAdapter extends FragmentPagerAdapter {

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	private void setGroup() {
		// title.setText("热点");
		hot.setBackground(getResources().getDrawable(R.drawable.btn_hoted));
		find.setBackground(getResources().getDrawable(R.drawable.btn_find));
		my.setBackground(getResources().getDrawable(R.drawable.btn_my));
		group_text.setTextColor(getResources().getColor(R.color.text_blue3F8));
		find_text.setTextColor(getResources().getColor(R.color.text_gray595));
		my_text.setTextColor(getResources().getColor(R.color.text_gray595));
	}

	private void setFind() {
		// title.setText("发现");
		find.setBackground(getResources().getDrawable(R.drawable.btn_finded));
		hot.setBackground(getResources().getDrawable(R.drawable.btn_hot));
		my.setBackground(getResources().getDrawable(R.drawable.btn_my));
		find_text.setTextColor(getResources().getColor(R.color.text_blue3F8));
		group_text.setTextColor(getResources().getColor(R.color.text_gray595));
		my_text.setTextColor(getResources().getColor(R.color.text_gray595));
	}

	private void setMy() {
		// title.setText("我的");
		my.setBackground(getResources().getDrawable(R.drawable.btn_myed));
		find.setBackground(getResources().getDrawable(R.drawable.btn_find));
		hot.setBackground(getResources().getDrawable(R.drawable.btn_hot));
		my_text.setTextColor(getResources().getColor(R.color.text_blue3F8));
		find_text.setTextColor(getResources().getColor(R.color.text_gray595));
		group_text.setTextColor(getResources().getColor(R.color.text_gray595));
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-19 下午2:10:21 方法说明:获取个人信息的类
	 */
	private void getMy_Message(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("lala", Address.MY_MESSAGE + "?" + params.toString());
		httpClient.post(Address.MY_MESSAGE, params, new TextHttpResponseHandler() {

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
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							userExpandDto = publicEntity.getEntity().getUserExpandDto();
							if (userExpandDto.getAvatar() != null) {
								imageLoader.displayImage(Address.IMAGE_NET + userExpandDto.getAvatar(), img,
										HttpUtils.getUserHeadDisPlay());
							} else {
								img.setImageResource(R.drawable.head_bg); // 修改用户头像
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
	 * 验证是否购买过套餐课程
	 */
	public void ableToExam(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);

		Log.i("wulala", "" + Address.RMBPLAYER + params);
		httpClient.post(Address.RMBPLAYER, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					publicEntity = JSON.parseObject(data, StringEntity.class);
					if (!publicEntity.isSuccess()) {
						showQuitDiaLog();
					} else if (publicEntity.isSuccess()) {
						startActivity(new Intent(GroupMainActivity.this, ExamActivity.class));
						GroupMainActivity.this.finish();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});

	}

	// 退出时显示的diaLog
	public void showQuitDiaLog() {
		View view = LayoutInflater.from(GroupMainActivity.this).inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) GroupMainActivity.this.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(GroupMainActivity.this, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
		TextView textView = (TextView) view.findViewById(R.id.texttitles);
		textView.setText("您需要学习相应的课程之后才可以获取考试的权限，现在去购买学习！");
		LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
		linbtnsure.setOnClickListener(this);
		TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
		LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
		linbtncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

}
