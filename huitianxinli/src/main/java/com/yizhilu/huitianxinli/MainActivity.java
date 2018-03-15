package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.community.GroupMainActivity;
import com.yizhilu.exam.ExamActivity;
import com.yizhilu.exam.entity.StringEntity;
import com.yizhilu.fragment.ComboFragment;
import com.yizhilu.fragment.CopyOfCourseFragment;
import com.yizhilu.fragment.HomeFragment;
import com.yizhilu.fragment.HomeFragment.OnSelectPageLisenner;
import com.yizhilu.fragment.MyFragment;
import com.yizhilu.fragment.NewArticleFragment;
import com.yizhilu.fragment.SlidingMenuFragment;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.PhoneUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author bin 修改人: 时间:2015-9-21 上午10:12:41 类说明:主页
 * 
 *         ps:if null nothing true
 */
public class MainActivity extends BaseActivity implements OnPageChangeListener, OnSelectPageLisenner {
	// private ViewPager viewPager; // 切换Fragment的控件
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private List<Fragment> fragments; // fragment的集合
	private LinearLayout slidingMenuLayout, search_layout, toHistory_layout; // slidingMenu的布局,搜索的布局,播放记录的布局
	private Intent intent; // 发送广播的意图
	private SlidingMenu slidingMenu; // 侧栏的对象
	private int userId; // 用户Id
	private long firstTime = 0;
	private TextView title_text; // 标题
	private Fragment homeFragment, courseFragment, newArticleFragment, myFragment, coboFragment; // 首页的对象
	private static MainActivity mainActivity; // 本类对象
	private BroadCast broadCast; // 广播的对象
	private PhoneUtils phoneUtils; // 手机的工具类
	private FragmentManager fragmentManager; // fragment管理器
	private RelativeLayout title_layout; // title总的布局
	private LinearLayout homeLayout, courseLayout, articleLayout, meLayout, comboLayout; // 底部导航栏布局
	private ImageView homeImage, courseImage, articleImage, meImage, comboImage; // 底部导航栏图片
	private TextView homeText, courseText, articleText, meText, comboText; // 底部导航栏文字
	private DemoApplication app;
	private TextView gallery, goto_sns;
	protected Dialog dialog;
	protected StringEntity targetId;
	protected StringEntity publicEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		DemoApplication.isNetWork = true;
		app = (DemoApplication) getApplication();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
		if (broadCast == null) {
			broadCast = new BroadCast();
			IntentFilter filter = new IntentFilter();
			filter.addAction("close_sliding");
			registerReceiver(broadCast, filter);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (broadCast != null) {
			unregisterReceiver(broadCast);
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-29 上午10:33:20 方法说明:获取本类对象的方法
	 */
	public static MainActivity getIntence() {
		if (mainActivity == null) {
			mainActivity = new MainActivity();
		}
		return mainActivity;
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {

		mainActivity = this;
		progressDialog = new ProgressDialog(MainActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		phoneUtils = new PhoneUtils(MainActivity.this); // 手机的工具类
		fragments = new ArrayList<Fragment>(); // fragment的集合
		intent = new Intent();
		title_layout = (RelativeLayout) findViewById(R.id.title_layout); // title总的布局
		search_layout = (LinearLayout) findViewById(R.id.search_layout); // 搜索的布局
		toHistory_layout = (LinearLayout) findViewById(R.id.toHistory_layout); // 播放记录的布局
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		slidingMenuLayout = (LinearLayout) findViewById(R.id.slidingMenuLayout); // slidingMenu的布局
		slidingMenuLayout.setVisibility(View.GONE); // 隐藏侧滑
		// viewPager = (ViewPager) findViewById(R.id.viewPager); //
		// 切换Fragment的控件
		gallery = (TextView) this.findViewById(R.id.gallery);
		goto_sns = (TextView) this.findViewById(R.id.goto_sns);
		homeLayout = (LinearLayout) findViewById(R.id.homeLayout); // 首页
		courseLayout = (LinearLayout) findViewById(R.id.courseLayout); // 课程
		articleLayout = (LinearLayout) findViewById(R.id.articleLayout); // 文章
		meLayout = (LinearLayout) findViewById(R.id.meLayout); // 我
		comboLayout = (LinearLayout) findViewById(R.id.combolayout);// 套餐
		homeImage = (ImageView) findViewById(R.id.homeImage);
		courseImage = (ImageView) findViewById(R.id.courseImage);
		articleImage = (ImageView) findViewById(R.id.articleImage);
		meImage = (ImageView) findViewById(R.id.meImage);
		comboImage = (ImageView) findViewById(R.id.comboImage);
		homeText = (TextView) findViewById(R.id.homeText);
		courseText = (TextView) findViewById(R.id.courseText);
		articleText = (TextView) findViewById(R.id.articleText);
		meText = (TextView) findViewById(R.id.meText);
		comboText = (TextView) findViewById(R.id.comboText);
		// 初始化slidingMenu的方法
		initSlidingMenu();
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
		if (userId != 0) {
			// 添加使用记录的方法
			addApplyRecord(userId, 1);
		}
		homeFragment = HomeFragment.getInstance(); // 首页
		fragmentManager = getSupportFragmentManager(); // 得到fragment管理器
		fragmentManager.beginTransaction().add(R.id.fragment_layout, homeFragment).commit();
		// 添加fragment的方法
		// addFragment();
		// viewPager.setOffscreenPageLimit(fragments.size()); //
		// 设置viewPager预加载的个数
		// // 绑定适配器
		// viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
		// fragments));

	}

	/**
	 * @author bin 修改人: 时间:2016-1-25 下午2:47:00 方法说明:添加使用记录的方法
	 */
	private void addApplyRecord(int userId, int type) {
		RequestParams params = new RequestParams();
		params.put("websiteInstall.ip", PhoneUtils.GetHostIp());
		params.put("websiteInstall.brand", phoneUtils.getPhoneBrand());
		params.put("websiteInstall.modelNumber", phoneUtils.getPhoneModel());
		params.put("websiteInstall.size", phoneUtils.getPhoneSize());
		params.put("websiteUse.userId", userId);
		params.put("websiteUse.state", type); // 开始时传1
		params.put("websiteLogin.type", "android");
		httpClient.post(Address.ADD_APPLY_RECORD, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-10-19 上午10:46:24 方法说明:初始化slidingMenu的方法
	 */
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(MainActivity.this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 设置压缩的效果
		slidingMenu.setBehindScrollScale(0.35f);
		// 设置滑动菜单拉开后的距离
		slidingMenu.setBehindOffsetRes(R.dimen.menu_offset_with);
		// 设置阴影宽度
		slidingMenu.setShadowWidth(getWindowManager().getDefaultDisplay().getWidth() / 50);
		// 设置阴影的图片
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		// 设置是否开启渐变效果
		slidingMenu.setFadeEnabled(true);
		// 设置渐变想过的值 范围(0.0-1.0f);
		slidingMenu.setFadeDegree(0.3f);
		slidingMenu.setBehindWidth((int) (MainActivity.this.getWindowManager().getDefaultDisplay().getHeight() / 2.3));
		slidingMenu.setMenu(R.layout.menu_sliding);
		slidingMenu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.slidingMenu_layout, new SlidingMenuFragment());
		transaction.commit();
	}

	/**
	 * @author bin 修改人: 时间:2015-10-15 下午3:57:16 方法说明:添加fragment的方法
	 */
	private void addFragment() {
		fragments.add(new HomeFragment()); // 添加首页
		fragments.add(new CopyOfCourseFragment()); // 添加课程页
		fragments.add(new NewArticleFragment()); // 添加文章也
		MyFragment myFragment = new MyFragment();
		fragments.add(myFragment); // 添加我的页面
		MyFragment.myFragment = myFragment;
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		// viewPager.setOnPageChangeListener(this); // 绑定切换时的事件
		slidingMenuLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slidingMenu.toggle(true);
			}
		}); // slidingMenu的事件
		search_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(MainActivity.this, SearchActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});// 搜索
		toHistory_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userId == 0) {
					intent.setClass(MainActivity.this, LoginActivity.class);
				} else {
					intent.setClass(MainActivity.this, PlayHistoryActivity.class);
				}
				MainActivity.this.startActivity(intent);
			}
		}); // 播放记录
		homeLayout.setOnClickListener(this); // 首页
		courseLayout.setOnClickListener(this); // 课程
		articleLayout.setOnClickListener(this); // 文章
		meLayout.setOnClickListener(this); // 我
		comboLayout.setOnClickListener(this); // 我
		// homeFragment.setOnSelectPageLisenner(this);
		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				/**
				 * 判断是否购买套餐
				 */
				if (userId != 0) {
					ableToExam(userId);
				} else {
					intent.setClass(MainActivity.this, LoginActivity.class);
					startActivity(intent);
				}

			}
		});
		goto_sns.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, GroupMainActivity.class));

			}
		});
		HomeFragment.getInstance().setOnSelectPageLisenner(this);
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		// 开启一个Fragment的事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有的fragment
		hideFragments(transaction);
		// 设置图片和背景颜色
		setResources();
		switch (v.getId()) {
		case R.id.homeLayout: // 首页
			title_layout.setVisibility(View.VISIBLE);
			if (homeFragment == null) {
				homeFragment = HomeFragment.getInstance();
				transaction.add(R.id.fragment_layout, homeFragment);
			} else {
				transaction.show(homeFragment);
			}
			title_text.setText(getResources().getString(R.string.home));
			homeImage.setBackgroundResource(R.drawable.home_button_selected);
			homeText.setTextColor(getResources().getColor(R.color.White));
			// viewPager.setCurrentItem(0);
			break;
		case R.id.courseLayout: // 课程
			title_layout.setVisibility(View.VISIBLE);
			if (courseFragment == null) {
				courseFragment = CopyOfCourseFragment.getInstance();
				transaction.add(R.id.fragment_layout, courseFragment);
			} else {
				transaction.show(courseFragment);
			}
			title_text.setText(getResources().getString(R.string.course));
			courseImage.setBackgroundResource(R.drawable.course_button_selected);
			courseText.setTextColor(getResources().getColor(R.color.White));
			// viewPager.setCurrentItem(1);
			break;
		case R.id.articleLayout: // 文章
			title_layout.setVisibility(View.VISIBLE);
			if (newArticleFragment == null) {
				newArticleFragment = NewArticleFragment.getInstance();
				transaction.add(R.id.fragment_layout, newArticleFragment);
			} else {
				transaction.show(newArticleFragment);
			}
			title_text.setText(getResources().getString(R.string.article));
			articleImage.setBackgroundResource(R.drawable.article_button_selected);
			articleText.setTextColor(getResources().getColor(R.color.White));
			// viewPager.setCurrentItem(1);
			break;
		case R.id.meLayout: // 我
			if (myFragment == null) {
				myFragment = MyFragment.getInstance();
				transaction.add(R.id.fragment_layout, myFragment);
			} else {
				transaction.show(myFragment);
			}
			title_layout.setVisibility(View.GONE);
			// title_text.setText(getResources().getString(R.string.my));
			meImage.setBackgroundResource(R.drawable.me_button_selected);
			meText.setTextColor(getResources().getColor(R.color.White));
			// viewPager.setCurrentItem(3);
			break;

		case R.id.dialog_linear_sure: // 考试拦截
			title_layout.setVisibility(View.VISIBLE);
			if (coboFragment == null) {
				coboFragment = new ComboFragment();
				transaction.add(R.id.fragment_layout, coboFragment);
			} else {
				transaction.detach(coboFragment);
				coboFragment = new ComboFragment();
				transaction.add(R.id.fragment_layout, coboFragment);
			}
			Bundle args = new Bundle();
			args.putString("subjectId", publicEntity.getEntity());
			coboFragment.setArguments(args);
			Log.i("wulala", "" + publicEntity.getEntity());
			title_text.setText(getResources().getString(R.string.package_home));
			comboImage.setBackgroundResource(R.drawable.package_select);
			comboText.setTextColor(getResources().getColor(R.color.White));
			if (dialog != null) {
				dialog.cancel();
			}
			break;
		case R.id.combolayout: // 套餐
			title_layout.setVisibility(View.VISIBLE);
			if (coboFragment == null) {
				coboFragment = new ComboFragment();
				transaction.add(R.id.fragment_layout, coboFragment);
			} else {
				transaction.show(coboFragment);
			}
			title_text.setText(getResources().getString(R.string.package_home));
			comboImage.setBackgroundResource(R.drawable.package_select);
			comboText.setTextColor(getResources().getColor(R.color.White));
			if (dialog != null) {
				dialog.cancel();
			}
			// viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 此方法在状态改变时调用，共有三个状态：0，什么都没做；1，正在滑动；2，滑动完毕
	 */
	@Override
	public void onPageScrollStateChanged(int position) {

	}

	/**
	 * 页面滑动的时候调用此方法
	 */
	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {

	}

	/**
	 * 此方法是页面跳转完后得到调用
	 */
	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0: // 首页
			title_layout.setVisibility(View.VISIBLE);
			title_text.setText(getResources().getString(R.string.home));
			break;
		case 1: // 课程页面
			title_layout.setVisibility(View.VISIBLE);
			title_text.setText(getResources().getString(R.string.course));
			break;
		case 2: // 文章页面
			title_layout.setVisibility(View.VISIBLE);
			title_text.setText(getResources().getString(R.string.article));
			break;
		case 3: // 我的页面
			comboLayout.setVisibility(View.VISIBLE);
			comboText.setText(getResources().getString(R.string.past_coupon));
			break;
		case 4: // 我的页面
			title_layout.setVisibility(View.GONE);
			title_text.setText(getResources().getString(R.string.my));
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-15 下午4:03:29 类说明:ViewPager的适配器
	 */
	class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments; // fragment的集合

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}

	}

	/**
	 * @author bin 修改人: 时间:2016-4-9 下午1:54:54 方法说明:设置背景和颜色
	 */
	private void setResources() {
		homeImage.setBackgroundResource(R.drawable.home_button);
		courseImage.setBackgroundResource(R.drawable.course_button);
		articleImage.setBackgroundResource(R.drawable.article_button);
		meImage.setBackgroundResource(R.drawable.me_button);
		homeText.setTextColor(getResources().getColor(R.color.tabText));
		courseText.setTextColor(getResources().getColor(R.color.tabText));
		articleText.setTextColor(getResources().getColor(R.color.tabText));
		meText.setTextColor(getResources().getColor(R.color.tabText));
		comboImage.setBackgroundResource(R.drawable.package_normal);
		comboText.setTextColor(getResources().getColor(R.color.tabText));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (courseFragment != null) {
			transaction.hide(courseFragment);
		}
		if (newArticleFragment != null) {
			transaction.hide(newArticleFragment);
		}
		if (myFragment != null) {
			transaction.hide(myFragment);
		}
		if (coboFragment != null) {
			transaction.hide(coboFragment);
		}
	}

	/**
	 * 点击首页小编更多和热门更多调用的方法
	 */
	@Override
	public void onSelectPage(int page) {
		// viewPager.setCurrentItem(page);
		// 开启一个Fragment的事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有的fragment
		hideFragments(transaction);
		// 设置图片和背景颜色
		setResources();
		title_layout.setVisibility(View.VISIBLE);
		if (courseFragment == null) {
			courseFragment = CopyOfCourseFragment.getInstance();
			transaction.add(R.id.fragment_layout, courseFragment);
		} else {
			transaction.show(courseFragment);
		}
		transaction.commit();
		title_text.setText(getResources().getString(R.string.course));
		courseImage.setBackgroundResource(R.drawable.course_button_selected);
		courseText.setTextColor(getResources().getColor(R.color.White));
	}

	class BroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("close_sliding".equals(action)) {
				slidingMenu.toggle(false); // 关闭侧滑栏(slidingMenuFragment发过来的广播)
			}
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else { // 两次按键小于2秒时，退出应用
				if (userId != 0) {
					// 添加使用记录的方法
					addApplyRecord(userId, 2);
				}
				System.exit(0);
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
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
						startActivity(new Intent(MainActivity.this, ExamActivity.class));
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
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(MainActivity.this, R.style.custom_dialog);
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
