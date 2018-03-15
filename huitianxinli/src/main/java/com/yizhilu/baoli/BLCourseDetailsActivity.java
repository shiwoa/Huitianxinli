package com.yizhilu.baoli;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.easefun.polyvsdk.QuestionVO;
import com.easefun.polyvsdk.Video.ADMatter;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.IjkVideoView.ErrorReason;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.srt.PolyvSRTItemVO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.MediaController.OnVideoChangeListener;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.fragment.CourseDiscussFragment;
import com.yizhilu.fragment.CourseRecommendFragment;
import com.yizhilu.fragment.CourseSectionFragment;
import com.yizhilu.huitianxinli.ConfirmOrderActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.OpinionFeedbackActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.huitianxinli.ShareDialog;
import com.yizhilu.huitianxinli.WebViewCoursePlay;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.Logs;
import com.yizhilu.utils.NetWorkUtils;
import com.yizhilu.utils.ParamsUtil;
import com.yizhilu.utils.ScreenUtil;
import com.yizhilu.view.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author bin 修改人: 时间:2016-2-22 下午1:49:04 类说明:CC课程详情的类
 */
public class BLCourseDetailsActivity extends BaseActivity implements OnPageChangeListener, OnSeekBarChangeListener {
	private static Activity activity;
	private ImageView courseImage, playVideo, layerImage, collect_image; // 课程的图片,播放的按钮,蒙层的图片,收藏的图标
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ImageLoader imageLoader; // 加载图片的对象
	private int courseId, userId, kpointId, isfree; // 课程的Id,用户Id,播放的节点Id,是否试听
	private ViewPager viewPager; // 滑动的切换课程介绍，课程章节，讨论去的控件
	private List<Fragment> fragments; // fragment的集合
	private CourseRecommendFragment courseRecommendFragment; // 课程介绍的类
	private CourseSectionFragment courseSectionFragment; // 课程章节的类
	private CourseDiscussFragment courseDiscussFragment; // 讨论区的类
	private PublicEntity publicEntity; // 課程詳情的總实体
	private LinearLayout topBackLayout, bottomBackLayout, collectLayout, downLoadLayout, opinionLayout, shareLayout,
			audioLayout; // 顶端的返回,底部的返回,收藏,下载,意见,分享,音频的布局
	private ImageView video_back; // 播放控件上的返回
	private TextView video_textText; // 播放控件标题
	private boolean isok, isVoice, isVideo, isAudio, isAudioStop; // 是否购买,是否是图文形式,是否是视频,是否是音频,音频是否暂停
	private BroadcastReceiver receiver; // 广播的类
	// ---------視頻的屬性-------------------------
	private String videoUrl, voiceUrl; // 視頻的路徑,音频路径
	private SeekBar skbProgress; // 更新进度的控件
	private TextView playDuration, videoDuration; // 播放进度,视频的时长
	private ProgressBar bufferProgressBar;// 播放时显示的dialog
	private TimerTask timerTask;
	private boolean isStart = true;
	private boolean fav = true; // 是否收藏过(没收藏)
	private Timer timer = new Timer();// 定时器
	private int vcodenum, progress, screenOrientation, screenWidth;// ,播放的进度,0竖屏1横屏,屏幕宽高
	private RelativeLayout playerBottomLayout, playAllLayout, titleLayout, video_title_layout; // 播放进度的总布局,播放的总的布局,title的总布局,播放布局标题总控件
	private ImageView btnPlay, playScreen; // 播放和暂停的按钮,全屏的按钮
	// --------------------------------------------------------------------------------------------
	private TextView course_introduce, course_zhang, course_discuss; // 课程介绍,课程章节,讨论区
	private boolean isWifi, wifi;
	private int currentPosition;
	private Intent intent; // 意图对象
	private CircleImageView audioImage; // 音频的图片
	private MediaPlayer audioMediaPlayer; // 音频播放对象
	private Animation animation; // 音频图片旋转动画
	private ShareDialog shareDialog; // 分享的dialog
	private EntityCourse course;
	private LinearLayout fragmentLayout, buttomLayout; // fragment的布局，底部导航的布局
	// ------------------------------------保利视频----------------------------
	int w = 0, h = 0, adjusted_h = 0;
	private RelativeLayout rl = null;
	private IjkVideoView videoView = null;
	private ImageView logo = null;
	private TextView srtTextView = null;
	private int stopPosition = 0;
	private boolean startNow;
	// private PolyvPlayerFirstStartView playerFirstStartView = null;
	private PolyvQuestionView questionView = null;
	private PolyvAuditionView auditionView = null;
	private PolyvPlayerAdvertisementView adView = null;
	private MediaController mediaController = null;
	protected boolean isSchool = false;// false代表改学员已经休学

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_baoli_course_details);
		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	private void getIntentMessage() {
		Intent intent = getIntent();
		courseId = intent.getIntExtra("courseId", 0);
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		Log.i("intent", "courserID" + courseId + "__userID" + userId + "接收传递过来的值");
	}

	/**
	 * @author bin 修改人: 时间:2015-10-24 下午6:59:21 方法说明:单例模式获取本实例
	 * @return
	 */
	public static Activity getInstence() {
		if (activity == null) {
			activity = new BLCourseDetailsActivity();
		}
		return activity;
	}

	@Override
	public void initView() {
		// ShareSDK.initSDK(this);
		activity = BLCourseDetailsActivity.this;
		progressDialog = new ProgressDialog(BLCourseDetailsActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		fragments = new ArrayList<Fragment>(); // 存放fragment的集合
		intent = new Intent(); // 意图对象
		isWifi = getSharedPreferences("wifi", MODE_PRIVATE).getBoolean("wifi", true); // 是否是在wifi下下载和观看视频
		animation = AnimationUtils.loadAnimation(this, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		imageLoader = ImageLoader.getInstance(); // 加载图片的对象
		titleLayout = (RelativeLayout) findViewById(R.id.titleLayout); // title的总布局
		courseImage = (ImageView) findViewById(R.id.courseImage); // 课程的图片
		viewPager = (ViewPager) findViewById(R.id.viewPager); // 滑动的切换课程介绍，课程章节，讨论去的控件
		topBackLayout = (LinearLayout) findViewById(R.id.topBackLayout); // 顶部的返回
		bottomBackLayout = (LinearLayout) findViewById(R.id.bottomBackLayout); // 底部的返回
		collectLayout = (LinearLayout) findViewById(R.id.collectLayout); // 收藏
		collect_image = (ImageView) findViewById(R.id.collect_image); // 收藏的图标
		downLoadLayout = (LinearLayout) findViewById(R.id.downLoadLayout); // 下载
		opinionLayout = (LinearLayout) findViewById(R.id.opinionLayout); // 意见
		shareLayout = (LinearLayout) findViewById(R.id.shareLayout); // 分享
		playAllLayout = (RelativeLayout) findViewById(R.id.playAllLayout); // 播放总的布局
		layerImage = (ImageView) findViewById(R.id.layerImage); // 蒙层
		video_title_layout = (RelativeLayout) findViewById(R.id.video_title_layout); // 播放布局上的总标题布局
		video_back = (ImageView) findViewById(R.id.video_back); // 播放控件上的返回
		video_textText = (TextView) findViewById(R.id.video_title); // 播放上的标题
		playVideo = (ImageView) findViewById(R.id.playVideo); // 播放的按钮
		playDuration = (TextView) findViewById(R.id.playDuration); // 播放进度
		playerBottomLayout = (RelativeLayout) findViewById(R.id.playerBottomLayout); // 播放进度的总布局
		videoDuration = (TextView) findViewById(R.id.videoDuration);// 视频的长度
		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);
		btnPlay = (ImageView) findViewById(R.id.btnPlay); // 暂停播放的按钮
		playScreen = (ImageView) findViewById(R.id.playScreen); // 切换全屏的按钮
		audioLayout = (LinearLayout) findViewById(R.id.audioLayout); // 音频的布局
		audioImage = (CircleImageView) findViewById(R.id.audioImage); // 音频图片
		fragmentLayout = (LinearLayout) findViewById(R.id.fragmentLayout); // fragment的布局
		buttomLayout = (LinearLayout) findViewById(R.id.buttomLayout); // 底部导航
		course_introduce = (TextView) findViewById(R.id.course_introduce); // 课程介绍
		course_zhang = (TextView) findViewById(R.id.course_zhang); // 课程章节
		course_discuss = (TextView) findViewById(R.id.course_discuss); // 讨论区
		// ------保利----------
		initPolyv();
		// // 设置播放布局的高度
		setPlayViewSize(0);
		// 联网获取课程详情的方法
		getCourseDetails(courseId, userId);
	}

	/**
	 * @author bin 修改人: 时间:2016-5-24 下午3:24:14 方法说明:初始化保利视频
	 */
	public void initPolyv() {
		Point point = new Point();
		WindowManager wm = this.getWindowManager();
		wm.getDefaultDisplay().getSize(point);
		w = point.x;
		h = point.y;
		// 小窗口的比例
		float ratio = (float) 4 / 3;
		adjusted_h = (int) Math.ceil((float) w / ratio);
		rl = (RelativeLayout) findViewById(R.id.rl);
		rl.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
		videoView = (IjkVideoView) findViewById(R.id.videoview);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
		TextView videoAdCountDown = (TextView) findViewById(R.id.count_down);
		logo = (ImageView) findViewById(R.id.logo);
		srtTextView = (TextView) findViewById(R.id.srt);
		// 在缓冲时出现的loading
		videoView.setMediaBufferingIndicator(progressBar);
		videoView.setAdCountDown(videoAdCountDown);
		videoView.setOpenTeaser(false);
		videoView.setOpenAd(false);
		videoView.setOpenQuestion(true);
		videoView.setOpenSRT(true);
		videoView.setNeedGestureDetector(false);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(IMediaPlayer mp) {
				videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
				logo.setVisibility(View.VISIBLE);
				if (stopPosition > 0) {
					videoView.seekTo(stopPosition);
				}

				if (startNow == false) {
					videoView.start();
				}

				String msg = String.format("是否在线播放 %b", videoView.isLocalPlay() == false);
			}
		});

		videoView.setOnVideoStatusListener(new IjkVideoView.OnVideoStatusListener() {

			@Override
			public void onStatus(int status) {

			}
		});

		videoView.setOnVideoPlayErrorLisener(new IjkVideoView.OnVideoPlayErrorLisener() {

			@Override
			public boolean onVideoPlayError(ErrorReason errorReason) {
				return false;
			}
		});

		videoView.setOnQuestionOutListener(new IjkVideoView.OnQuestionOutListener() {

			@Override
			public void onOut(final QuestionVO questionVO) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						switch (questionVO.getType()) {
						case QuestionVO.TYPE_QUESTION:
							if (questionView == null) {
								questionView = new PolyvQuestionView(BLCourseDetailsActivity.this);
								questionView.setIjkVideoView(videoView);
							}

							questionView.show(rl, questionVO);
							break;

						case QuestionVO.TYPE_AUDITION:
							if (auditionView == null) {
								auditionView = new PolyvAuditionView(BLCourseDetailsActivity.this);
								auditionView.setIjkVideoView(videoView);
							}

							auditionView.show(rl, questionVO);
							break;
						}
					}
				});
			}
		});

		videoView.setOnQuestionAnswerTipsListener(new IjkVideoView.OnQuestionAnswerTipsListener() {

			@Override
			public void onTips(String msg) {
				questionView.showAnswerTips(msg);
			}
		});

		videoView.setOnAdvertisementOutListener(new IjkVideoView.OnAdvertisementOutListener() {

			@Override
			public void onOut(ADMatter adMatter) {
				stopPosition = videoView.getCurrentPosition();
				if (adView == null) {
					adView = new PolyvPlayerAdvertisementView(BLCourseDetailsActivity.this);
					adView.setIjkVideoView(videoView);
				}

				adView.show(rl, adMatter);
			}
		});

		videoView.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {

			@Override
			public void onPlay() {
				Log.i("lala", "onPlay");
				// 记录
			}

			@Override
			public void onPause() {
				Log.i("lala", "onPause");
				fetchPointCourseTime();
			}

			@Override
			public void onCompletion() {
				Log.i("lala", "onCompletion");
				logo.setVisibility(View.GONE);
				mediaController.setProgressMax();
				fetchPointCourseTime();
			}
		});

		videoView.setOnVideoSRTListener(new IjkVideoView.OnVideoSRTListener() {

			@Override
			public void onVideoSRT(PolyvSRTItemVO sRTItemVO) {
				if (sRTItemVO == null) {
					srtTextView.setText("");
				} else {
					srtTextView.setText(sRTItemVO.getSubTitle());
				}
			}
		});

		mediaController = new MediaController(this, false);
		mediaController.setIjkVideoView(videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		mediaController.setOnVideoChangeListener(new OnVideoChangeListener() {

			@Override
			public void onVideoChange(int layout) {
				Log.i("lala", "lay0ut:" + layout);
			}
		});
		// 设置切屏事件
		mediaController.setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {

			@Override
			public void onPortrait() {
				// // 设置播放布局的高度
				setPlayViewSize(1);
				fragmentLayout.setVisibility(View.GONE);
				buttomLayout.setVisibility(View.GONE);
				changeToLandscape();
			}

			@Override
			public void onLandscape() {
				// // 设置播放布局的高度
				setPlayViewSize(0);
				fragmentLayout.setVisibility(View.VISIBLE);
				buttomLayout.setVisibility(View.VISIBLE);
				changeToPortrait();
			}
		});

		// 设置视频尺寸 ，在横屏下效果较明显
		mediaController.setOnVideoChangeListener(new MediaController.OnVideoChangeListener() {

			@Override
			public void onVideoChange(final int layout) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						videoView.setVideoLayout(layout);
						switch (layout) {
						case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
							// Toast.makeText(
							// BLCourseDetailsActivity.this,
							// "VIDEO_LAYOUT_ORIGIN",
							// Toast.LENGTH_SHORT).show();
							break;
						case IjkVideoView.VIDEO_LAYOUT_SCALE:
							// Toast.makeText(
							// BLCourseDetailsActivity.this,
							// "VIDEO_LAYOUT_SCALE",
							// Toast.LENGTH_SHORT).show();
							break;
						case IjkVideoView.VIDEO_LAYOUT_STRETCH:
							// Toast.makeText(
							// BLCourseDetailsActivity.this,
							// "VIDEO_LAYOUT_STRETCH",
							// Toast.LENGTH_SHORT).show();
							break;
						case IjkVideoView.VIDEO_LAYOUT_ZOOM:
							// Toast.makeText(
							// BLCourseDetailsActivity.this,
							// "VIDEO_LAYOUT_ZOOM",
							// Toast.LENGTH_SHORT).show();
							break;
						}
					}
				});
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-10-23 下午1:39:24 方法说明:获取课程详情的方法
	 */
	private void getCourseDetails(int courseId, final int userId) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		System.out.println("获取课程详情的方法:" + Address.COURSE_DETAILS + "?" + params.toString());
		Log.i("wulala", "courserID" + courseId + "__userID" + userId + "接收传递过来的值       地址为:" + Address.COURSE_DETAILS
				+ "?" + params.toString());
		httpClient.post(Address.COURSE_DETAILS, params, new TextHttpResponseHandler() {

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
						publicEntity = JSON.parseObject(data, PublicEntity.class);
						isSchool = publicEntity.getEntity().getIsSchool();
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							video_textText.setText(publicEntity.getEntity().getCourse().getName());
							fav = publicEntity.getEntity().isFav();
							Log.i("lala", fav + "..");
							if (userId == 0) {
								collect_image.setBackgroundResource(R.drawable.collect);
							} else {
								if (fav) { // 可以收藏
									collect_image.setBackgroundResource(R.drawable.collect);
								} else { // 不可以收藏
									collect_image.setBackgroundResource(R.drawable.collect_yes);
								}
							}
							isok = publicEntity.getEntity().isIsok();
							course = publicEntity.getEntity().getCourse();
							imageLoader.displayImage(Address.IMAGE_NET + course.getMobileLogo(), courseImage,
									HttpUtils.getDisPlay());
							imageLoader.displayImage(Address.IMAGE_NET + course.getMobileLogo(), audioImage,
									HttpUtils.getDisPlay());
							// 初始化fragment的方法
							initFragments();
							Bundle bundle = new Bundle();
							bundle.putSerializable("publicEntity", publicEntity);
							Logs.info("BLCourseDetailsActivity price="
									+ publicEntity.getEntity().getCourse().getCurrentprice());
							courseRecommendFragment.setArguments(bundle); // 课程介绍
							courseSectionFragment.setArguments(bundle); // 课程章节
							courseDiscussFragment.setArguments(bundle); // 讨论
							viewPager.setOffscreenPageLimit(fragments.size());
							viewPager.setAdapter(new CoursePagerAdapater(getSupportFragmentManager(), fragments));
						} else {
							ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-10-19 下午1:34:40 方法说明:初始化fragment
	 */
	private void initFragments() {
		fragments.add(courseRecommendFragment = CourseRecommendFragment.getInstence()); // 课程介绍
		fragments.add(courseSectionFragment = CourseSectionFragment.getInstence()); // 课程章节
		fragments.add(courseDiscussFragment = CourseDiscussFragment.getInstence()); // 讨论区
	}

	@Override
	public void addOnClick() {
		topBackLayout.setOnClickListener(this); // 顶端的返回
		bottomBackLayout.setOnClickListener(this); // 底部的返回
		collectLayout.setOnClickListener(this); // 收藏
		downLoadLayout.setOnClickListener(this); // 下载
		opinionLayout.setOnClickListener(this); // 意见
		shareLayout.setOnClickListener(this); // 分享
		video_back.setOnClickListener(this); // 播放控件上的返回
		playVideo.setOnClickListener(this); // 播放的按钮
		viewPager.setOnPageChangeListener(this); // 滑动事件
		skbProgress.setOnSeekBarChangeListener(this);
		btnPlay.setOnClickListener(this); // 播放暂停的事件
		playScreen.setOnClickListener(this); // 全屏的事件
		course_introduce.setOnClickListener(this); // 课程介绍
		course_zhang.setOnClickListener(this); // 课程章节
		course_discuss.setOnClickListener(this); // 讨论区
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.topBackLayout: // 顶端的返回
			this.finish();
			break;
		case R.id.bottomBackLayout: // 底部的返回
			this.finish();
			break;
		case R.id.video_back: // 播放布局上的返回
			if (screenOrientation == 0) {
				finish();
			} else if (screenOrientation == 1) {
				setScreenOrientation(0);
			}
			break;
		case R.id.collectLayout: // 收藏
			if (userId == 0) {
				intent.setClass(BLCourseDetailsActivity.this, LoginActivity.class);
				startActivity(intent);
			} else {
				if (fav) { // 可收藏
					// 调用收藏的方法
					getAddCourseCollect(courseId, userId);
				} else {
					// 取消收藏
					cancelCollect(userId, courseId);
				}
			}
			break;
		case R.id.downLoadLayout: // 下载
			if (publicEntity.getEntity() == null) {
				return;
			}
			wifi = NetWorkUtils.isWIFI(BLCourseDetailsActivity.this);
			if (isWifi) {
				if (!wifi) {
					ConstantUtils.showMsg(BLCourseDetailsActivity.this, "请在wifi下观看和下载");
					return;
				}
			}
			if (course.getIsPay() == 1) {
				if (publicEntity.getEntity().isIsok()) {
					Intent downLoad_intent = new Intent(BLCourseDetailsActivity.this, BLDownLoadSelectActivity.class);
					downLoad_intent.putExtra("publicEntity", publicEntity);
					startActivity(downLoad_intent);
				} else {
					Toast.makeText(BLCourseDetailsActivity.this, "请先购买", Toast.LENGTH_SHORT).show();
				}
			} else if (course.getIsPay() == 0) {
				Intent downLoad_intent = new Intent(BLCourseDetailsActivity.this, BLDownLoadSelectActivity.class);
				downLoad_intent.putExtra("publicEntity", publicEntity);
				startActivity(downLoad_intent);
			}
			break;
		case R.id.opinionLayout: // 意见
			Intent intent = new Intent(BLCourseDetailsActivity.this, OpinionFeedbackActivity.class);
			startActivity(intent);
			// saveScreenshot();
			break;
		case R.id.shareLayout: // 分享
			if (!publicEntity.isSuccess()) {
				return;
			}
			if (shareDialog == null) {
				shareDialog = new ShareDialog(this, R.style.custom_dialog);
				shareDialog.setCancelable(false);
				shareDialog.show();
				shareDialog.setEntityCourse(publicEntity.getEntity().getCourse(), true, false, false);
			} else {
				shareDialog.show();
			}
			// showShare();
			break;
		case R.id.playVideo: // 播放的按钮

			if (userId == 0) {
				startActivity(new Intent(BLCourseDetailsActivity.this, LoginActivity.class));
				return;
			}
			if (isok == false) {
				getAlerDialog();
				return;
			}
			if (publicEntity.getEntity() == null) {
				return;
			}
			wifi = NetWorkUtils.isWIFI(BLCourseDetailsActivity.this);
			if (isWifi) {
				if (!wifi) {
					ConstantUtils.showMsg(BLCourseDetailsActivity.this, "请在wifi下观看和下载");
					return;
				}
			}

			kpointId = publicEntity.getEntity().getDefaultKpointId();
			if (isSchool) {
				// 没有进行休学状态
				// 验证播放节点的接口
				verificationPlayVideo(userId, kpointId);
			} else {// 正在休学
				ConstantUtils.showMsg(BLCourseDetailsActivity.this, "休学不能观看视频");
			}

			break;
		case R.id.btnPlay: // 暂停和播放的按钮
			if (isAudio) { // 音频
				if (isAudioStop) {
					isAudioStop = false;
					audioMediaPlayer.start();
					btnPlay.setImageResource(R.drawable.playonn);
					audioImage.startAnimation(animation);
				} else {
					isAudioStop = true;
					audioMediaPlayer.pause();
					btnPlay.setImageResource(R.drawable.btn_playee);
					audioImage.clearAnimation();
				}
			} else { // 视频

			}

			break;

		case R.id.playScreen: // 全屏的按钮
			if (screenOrientation == 0) {
				setScreenOrientation(1);
			} else if (screenOrientation == 1) {
				setScreenOrientation(0);
			}
			break;
		case R.id.course_introduce: // 课程介绍
			setBackGroud();
			course_introduce.setBackgroundResource(R.drawable.details_left);
			course_introduce.setTextColor(getResources().getColor(R.color.White));
			viewPager.setCurrentItem(0);
			break;
		case R.id.course_zhang: // 课程章节
			setBackGroud();
			course_zhang.setBackgroundResource(R.drawable.details_center);
			course_zhang.setTextColor(getResources().getColor(R.color.White));
			viewPager.setCurrentItem(1);
			break;
		case R.id.course_discuss: // 讨论区
			setBackGroud();
			course_discuss.setBackgroundResource(R.drawable.details_right);
			course_discuss.setTextColor(getResources().getColor(R.color.White));
			viewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午5:26:24 方法说明:设置课程介绍等按钮的背景
	 */
	private void setBackGroud() {
		course_introduce.setBackgroundResource(R.drawable.details_left_null);
		course_zhang.setBackgroundResource(R.drawable.details_center_null);
		course_discuss.setBackgroundResource(R.drawable.details_right_null);
		course_introduce.setTextColor(getResources().getColor(R.color.Blue));
		course_zhang.setTextColor(getResources().getColor(R.color.Blue));
		course_discuss.setTextColor(getResources().getColor(R.color.Blue));
	}

	/**
	 * @author bin 修改人: 时间:2016-4-8 上午9:54:15 方法说明:取消收藏的方法
	 */
	private void cancelCollect(int userId, int courseId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("courseId", courseId);
		Log.i("lala", Address.DELETE_COURSE_COLLECT + "?" + params);
		httpClient.post(Address.CANCEL_COLLECT, params, new TextHttpResponseHandler() {
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
						PublicEntity entity = JSON.parseObject(data, PublicEntity.class);
						String message = entity.getMessage();
						if (entity.isSuccess()) {
							ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
							fav = true;
							collect_image.setBackgroundResource(R.drawable.collect);
						} else {
							ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2015-10-23 下午4:34:15 方法说明:验证播放节点的接口
	 */
	private void verificationPlayVideo(final int userId, final int kpointId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("kpointId", kpointId);
		Log.i("lala", Address.VERIFICATION_PLAY + "?" + params.toString());
		httpClient.post(Address.VERIFICATION_PLAY, params, new TextHttpResponseHandler() {
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
						PublicEntity entity = JSON.parseObject(data, PublicEntity.class);
						String message = entity.getMessage();
						if (entity.isSuccess()) { // 可以播放
							EntityPublic entityPublic = entity.getEntity();
							String fileType = entityPublic.getFileType();
							if (!TextUtils.isEmpty(fileType)) {
								if ("VIDEO".equals(fileType)) { // 视频
									// 获取视频类型的方法
									getPlayVideoType(entityPublic);
								} else if ("AUDIO".equals(fileType)) { // 音频
									// 播放音频的方法
									playAudioType(entityPublic);
								} else { // 图文
									// 跳转图文页面的方法
									playAtlasType(userId, entityPublic);
								}
							}
						} else {
							if (isok) {
								ConstantUtils.showMsg(BLCourseDetailsActivity.this, "该视频无法播放");
							} else {
								// getAlerDialog();
								if (userId == 0) {
									ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
								} else {
									ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2016-2-16 下午4:49:08 方法说明:获取视频播放类型的方法
	 */
	private void getPlayVideoType(final EntityPublic entityPublic) {
		httpClient.get(Address.GET_PLAYVIDEO_TYPE, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity playTypeEntity = JSON.parseObject(data, PublicEntity.class);
						if (playTypeEntity.isSuccess()) {
							EntityPublic typeEntity = playTypeEntity.getEntity();
							if ("BAOLI".equals(entityPublic.getVideoType())) {
								// 播放视频的方法
								playVideoType(entityPublic);
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
	 * @author bin 修改人: 时间:2016-1-21 下午5:26:35 方法说明:类型为视频，播放视频的方法
	 */
	private void playVideoType(EntityPublic entityPublic) {
		isVideo = true;
		isAudio = false;
		videoUrl = entityPublic.getVideoUrl(); // 得到视频路径
		// 播放视频
		playVideo.setVisibility(View.GONE);
		courseImage.setVisibility(View.GONE);
		layerImage.setVisibility(View.GONE);
		bufferProgressBar.setVisibility(View.VISIBLE);
		playScreen.setVisibility(View.VISIBLE);
		btnPlay.setImageResource(R.drawable.playonn);
		audioLayout.setVisibility(View.GONE);
		audioImage.setVisibility(View.GONE);
		String videoType = entityPublic.getVideoType();
		if (TextUtils.isEmpty(videoType)) {
			ConstantUtils.showMsg(BLCourseDetailsActivity.this, "视频类型无返回");
			return;
		}
		if (videoType.equals("BAOLI")) { // 保利视频
			initPlayInfo();
		}
	}

	public void initPlayInfo() {
		playVideo.setVisibility(View.GONE);
		courseImage.setVisibility(View.GONE);
		layerImage.setVisibility(View.GONE);
		bufferProgressBar.setVisibility(View.GONE);
		videoView.setVid(videoUrl);
		Log.i("wulala", videoUrl);
		videoView.start();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-21 下午5:29:24 方法说明:类型为音频，播放音频的方法
	 */
	private void playAudioType(EntityPublic entityPublic) {
		voiceUrl = entityPublic.getVideoUrl(); // 得到音频路径
		// voiceUrl =
		// "http://static.268xue.com/upload/mavendemo/userFeedback/20151228/1451274540989933797.mp3";
		if (TextUtils.isEmpty(voiceUrl)) {
			ConstantUtils.showMsg(BLCourseDetailsActivity.this, "无音频文件");
			return;
		}
		isAudio = true;
		isVideo = false;
		playVideo.setVisibility(View.GONE);
		courseImage.setVisibility(View.GONE);
		layerImage.setVisibility(View.VISIBLE);
		layerImage.setBackgroundResource(R.drawable.audio_bg);
		audioLayout.setVisibility(View.VISIBLE);
		audioImage.setVisibility(View.VISIBLE);
		playScreen.setVisibility(View.GONE);
		if (audioMediaPlayer != null) {
			audioMediaPlayer.reset();
		}
		audioMediaPlayer = MediaPlayer.create(BLCourseDetailsActivity.this, Uri.parse(Address.IMAGE_NET + voiceUrl));
		playDuration.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getCurrentPosition()));
		videoDuration.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getDuration()));
		audioMediaPlayer.start();
		audioImage.startAnimation(animation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						if (!isAudioStop) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									int position = audioMediaPlayer.getCurrentPosition();
									int duration = audioMediaPlayer.getDuration();
									if (duration > 0) {
										if (position >= duration) {
											isAudioStop = true;
											btnPlay.setImageResource(R.drawable.btn_playee);
											audioImage.clearAnimation();
										}
										long pos = skbProgress.getMax() * position / duration;
										playDuration.setText(
												ParamsUtil.millsecondsToStr(audioMediaPlayer.getCurrentPosition()));
										skbProgress.setProgress((int) pos);
									}
								}
							});
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-21 下午5:30:52 方法说明:类型为图文,跳转图文页面的方法
	 */
	private void playAtlasType(final int userId, EntityPublic entityPublic) {
		isVoice = true;
		intent.setClass(BLCourseDetailsActivity.this, WebViewCoursePlay.class);
		intent.putExtra("userId", userId);
		intent.putExtra("kpointId", entityPublic.getKpointId());
		intent.putExtra("courseName", publicEntity.getEntity().getCourse().getName());
		startActivity(intent);
	}

	/**
	 * @author bin 修改人: 时间:2015-10-24 下午3:22:46 方法说明:提示购买的对话框
	 */
	public void getAlerDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示！").setMessage("您未购买该课程,是否购买该课程!")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 当点击确定的是支付宝回调
						try {
							Intent pay_intent = new Intent(BLCourseDetailsActivity.this, ConfirmOrderActivity.class);
							pay_intent.putExtra("publicEntity", publicEntity);
							startActivity(pay_intent);
						} catch (Exception ex) {
						}
					}
				}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午11:09:02 方法说明:连接网络获取添加课程收藏的方法
	 */
	private void getAddCourseCollect(int courseId, int userId) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		httpClient.post(Address.ADD_COURSE_COLLECT, params, new TextHttpResponseHandler() {
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
						PublicEntity collectEntity = JSON.parseObject(data, PublicEntity.class);
						String message = collectEntity.getMessage();
						ConstantUtils.showMsg(BLCourseDetailsActivity.this, message);
						fav = false;
						collect_image.setBackgroundResource(R.drawable.collect_yes);
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	// 关于视频播放的代码---------------------------------------------------------------------
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 3:
				vcodenum--;
				if (vcodenum == 0) {
					playerBottomLayout.setVisibility(View.GONE);
					video_title_layout.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (isAudio) { // 音频
			this.progress = progress * audioMediaPlayer.getDuration() / seekBar.getMax();
		} else { // 视频
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (isAudio) { // 音频
			audioMediaPlayer.seekTo(progress);
		} else { // 视频
		}
	}

	/**
	 * 设置屏幕方向
	 * 
	 * @param Orientation
	 *            0，竖屏；1横屏
	 */
	private void setScreenOrientation(int Orientation) {
		screenOrientation = Orientation;
		if (Orientation == 0) {
			playScreen.setImageResource(R.drawable.iv_media_quanping);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setPlayViewSize(0);
		} else if (Orientation == 1) {
			playScreen.setImageResource(R.drawable.iv_media_esc);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setPlayViewSize(1);
		}

	}

	/**
	 * 设置播放界面的大小
	 * 
	 * @param type
	 *            0，竖屏播放；1横屏播放
	 */
	private void setPlayViewSize(int type) {
		ViewGroup.LayoutParams layoutParams = playAllLayout.getLayoutParams();
		if (screenWidth == 0) {
			screenWidth = ScreenUtil.getInstance(this).getScreenWidth();
			ScreenUtil.getInstance(this).getScreenHeight();
		}
		if (type == 0) {
			titleLayout.setVisibility(View.GONE); // title的总布局显示
			full(false);
			layoutParams.width = screenWidth;
			layoutParams.height = layoutParams.width / 2;
			// layoutParams.height = R.dimen.y100;
		} else if (type == 1) {
			titleLayout.setVisibility(View.GONE); // title的总布局显示
			full(true);
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
		}
		playAllLayout.setLayoutParams(layoutParams);
	}

	/**
	 * @description 实现状态栏的显示与隐藏
	 */
	private void full(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	// -----------------------------------------------------------------

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
	protected void onResume() {
		if (isAudioStop) {
			isAudioStop = false;
			audioMediaPlayer.start();
			btnPlay.setImageResource(R.drawable.playonn);
			audioImage.startAnimation(animation);
		}
		if (isStart) {
			videoView.start();
		}
		super.onResume();
		if (receiver == null) {
			receiver = new BroadCastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("playVideo");
			registerReceiver(receiver, filter);
		}

		// 当进行开启课程详情Activity的时候进行网络请求获取到该课程的数据courseId
		fetchTotalCourseTime();

	}

	/**
	 * 记录该课程的总时间
	 */
	private void fetchTotalCourseTime() {// ?userId=1443&courseId=254
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		Log.i("lala", "记录课程的总时间:" + Address.ADD_STUDY_SCHEDULE + params.toString());
		httpClient.post(Address.ADD_STUDY_SCHEDULE, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				Log.i("lala", "记录课程的总时间成功");
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	/**
	 * 记录该课程下面的小结的时间
	 */
	private void fetchPointCourseTime() {// ?studyTime.userId=1444&studyTime.courseId=254&studyTime.kpointId=2449&studyTime.totalTime=6302&studyTime.currentTime=3151
		RequestParams params = new RequestParams();
		params.put("studyTime.courseId", courseId);
		params.put("studyTime.userId", userId);
		params.put("studyTime.kpointId", kpointId);
		params.put("studyTime.totalTime", videoView.getDuration() / 1000);
		params.put("studyTime.currentTime", videoView.getCurrentPosition() / 1000);
		Log.i("lala", "记录该课程下面的小结的时间:" + Address.ADD_TIME_RECORD + params.toString());
		httpClient.post(Address.ADD_TIME_RECORD, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				Log.i("lala", "记录该课程下面的小结的时间成功");
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	@Override
	protected void onPause() {
		if (audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
			isAudioStop = true;
			audioMediaPlayer.pause();
			btnPlay.setImageResource(R.drawable.btn_playee);
			audioImage.clearAnimation();
		}

		if (videoView != null && videoView.isPlaying()) {
			isStart = true;
			fetchPointCourseTime();
			videoView.pause();
			videoView.release(true);
		} else {
			isStart = false;
		}

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (timerTask != null) {
			timerTask.cancel();
		}
		if (audioMediaPlayer != null) {
			audioMediaPlayer.reset();
			audioMediaPlayer.release();
			audioMediaPlayer = null;
		}

		if (videoView != null) {
			videoView.release(true);
		}

		if (questionView != null) {
			questionView.hide();
		}

		if (auditionView != null) {
			auditionView.hide();
		}

		// if (playerFirstStartView != null) {
		// playerFirstStartView.hide();
		// }

		if (adView != null) {
			adView.hide();
		}
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-24 下午2:30:47 类说明:广播
	 */
	class BroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("playVideo".equals(action)) { // 点击目录传过来的
				wifi = NetWorkUtils.isWIFI(BLCourseDetailsActivity.this);
				if (isWifi) {
					if (!wifi) {
						ConstantUtils.showMsg(BLCourseDetailsActivity.this, "请在wifi下观看和下载");
						return;
					}
				}
				// videoUrl = intent.getStringExtra("videoUrl");
				kpointId = intent.getIntExtra("kpointId", 0);
				// isfree = intent.g|etIntExtra("isfree", 0);
				if (isSchool) {
					// 没有进行休学状态
					// 验证播放节点的接口
					verificationPlayVideo(userId, kpointId);
				} else {// 正在休学
					ConstantUtils.showMsg(BLCourseDetailsActivity.this, "休学不能观看视频");
				}
			}
		}

	}

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
			course_introduce.setTextColor(getResources().getColor(R.color.White));
			break;
		case 1:
			course_zhang.setBackgroundResource(R.drawable.details_center);
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

	/**
	 * @author bin 修改人: 时间:2016-3-26 上午9:27:47 方法说明:课程介绍点立即观看播放视频的方法
	 */
	public void playVideo() {
		if (publicEntity.getEntity() == null) {
			return;
		}
		kpointId = publicEntity.getEntity().getDefaultKpointId();
		// 验证播放节点的接口
		verificationPlayVideo(userId, kpointId);
	}

	@Override
	public void onBackPressed() {
		if (screenOrientation == 0) {
			finish();
		} else if (screenOrientation == 1) {
			setScreenOrientation(0);
		}
	}

	/**
	 * 切换到横屏
	 */
	public void changeToLandscape() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(h, w);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 切换到竖屏
	 */
	public void changeToPortrait() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
		rl.setLayoutParams(p);
		stopPosition = videoView.getCurrentPosition();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	// 配置文件设置congfigchange 切屏调用一次该方法，hide()之后再次show才会出现在正确位置
	@Override
	public void onConfigurationChanged(Configuration arg0) {
		super.onConfigurationChanged(arg0);
		videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		mediaController.hide();

		if (questionView != null && questionView.isShowing()) {
			questionView.hide();
			questionView.refresh();
		} else if (auditionView != null && auditionView.isShowing()) {
			auditionView.hide();
			auditionView.refresh();
		}

		if (adView != null && adView.isShowing()) {
			adView.hide();
			adView.refresh();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// 当程序发生了不异常崩溃进行记录
		if (videoView != null && videoView.isPlaying()) {
			fetchPointCourseTime();
			isStart = true;
			videoView.pause();
		} else {
			isStart = false;
		}
	}

	/**
	 * 
	 * 当键盘按下时，首先触发dispatchKeyEvent，然后触发onUserInteraction，再触发onKeyDown方法
	 * 
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() != KeyEvent.ACTION_UP) {
			// TODO
		}
		return super.dispatchKeyEvent(event);
	}

}
