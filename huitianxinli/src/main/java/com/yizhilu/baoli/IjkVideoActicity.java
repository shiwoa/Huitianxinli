package com.yizhilu.baoli;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.QuestionVO;
import com.easefun.polyvsdk.Video.ADMatter;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.IjkVideoView.ErrorReason;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.srt.PolyvSRTItemVO;
import com.yizhilu.baoli.OfflineMediaController.OnBackListener;
import com.yizhilu.huitianxinli.R;

public class IjkVideoActicity extends Activity {
	private static final String TAG = "IjkVideoActicity";
	private IjkVideoView videoView = null;
	private PolyvQuestionView questionView = null;
	private PolyvAuditionView auditionView = null;
	private PolyvPlayerAdvertisementView adView = null;
	private OfflineMediaController mediaController = null;
	private ImageView logo = null;
	private TextView srtTextView = null;
	// private PolyvPlayerFirstStartView playerFirstStartView = null;
	int w = 0, h = 0, adjusted_h = 0;
	private RelativeLayout rl = null;
	private int stopPosition = 0;

	public static Intent newIntent(Context context, PlayMode playMode,
			PlayType playType, String value, boolean startNow,String str) {
		Intent intent = new Intent(context, IjkVideoActicity.class);
		intent.putExtra("playMode", playMode.getCode());
		intent.putExtra("playType", playType.getCode());
		intent.putExtra("value", value);
		intent.putExtra("startNow", startNow);
		intent.putExtra("title", str);
		
		return intent;
	}

	public static void intentTo(Context context, PlayMode playMode,
			PlayType playType, String value, boolean startNow,String str) {
		context.startActivity(newIntent(context, playMode, playType, value,
				startNow,str));
	}

	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_small2);

		// handle arguments
		int playModeCode = getIntent().getIntExtra("playMode", 0);
		PlayMode playMode = PlayMode.getPlayMode(playModeCode);
		int playTypeCode = getIntent().getIntExtra("playType", 0);
		final PlayType playType = PlayType.getPlayType(playTypeCode);
		final String value = getIntent().getStringExtra("value");
		final boolean startNow = getIntent().getBooleanExtra("startNow", false);
		if (playMode == null || playType == null || TextUtils.isEmpty(value)) {
			Log.e(TAG, "Null Data Source");
			finish();
			return;
		}

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
				if (stopPosition > 0) {
					videoView.seekTo(stopPosition);
				}

				if (startNow == false) {
					mediaController.hint(); // 隐藏横竖屏的图标
					videoView.start();
					// videoView.pause(true);
					// if (playType == PlayType.vid) {
					// playerFirstStartView.show(rl, value);
					// }
				}

				String msg = String.format("是否在线播放 %b",
						videoView.isLocalPlay() == false);
				Log.d(TAG, msg);
				Toast.makeText(IjkVideoActicity.this, msg, Toast.LENGTH_SHORT);
			}
		});

		videoView
				.setOnVideoStatusListener(new IjkVideoView.OnVideoStatusListener() {

					@Override
					public void onStatus(int status) {

					}
				});

		videoView
				.setOnVideoPlayErrorLisener(new IjkVideoView.OnVideoPlayErrorLisener() {

					@Override
					public boolean onVideoPlayError(ErrorReason errorReason) {
						return false;
					}
				});

		videoView
				.setOnQuestionOutListener(new IjkVideoView.OnQuestionOutListener() {

					@Override
					public void onOut(final QuestionVO questionVO) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								switch (questionVO.getType()) {
								case QuestionVO.TYPE_QUESTION:
									if (questionView == null) {
										questionView = new PolyvQuestionView(
												IjkVideoActicity.this);
										questionView.setIjkVideoView(videoView);
									}

									questionView.show(rl, questionVO);
									break;

								case QuestionVO.TYPE_AUDITION:
									if (auditionView == null) {
										auditionView = new PolyvAuditionView(
												IjkVideoActicity.this);
										auditionView.setIjkVideoView(videoView);
									}

									auditionView.show(rl, questionVO);
									break;
								}
							}
						});
					}
				});

		videoView
				.setOnQuestionAnswerTipsListener(new IjkVideoView.OnQuestionAnswerTipsListener() {

					@Override
					public void onTips(String msg) {
						questionView.showAnswerTips(msg);
					}
				});

		videoView
				.setOnAdvertisementOutListener(new IjkVideoView.OnAdvertisementOutListener() {

					@Override
					public void onOut(ADMatter adMatter) {
						stopPosition = videoView.getCurrentPosition();
						if (adView == null) {
							adView = new PolyvPlayerAdvertisementView(
									IjkVideoActicity.this);
							adView.setIjkVideoView(videoView);
						}

						adView.show(rl, adMatter);
					}
				});

		videoView
				.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {

					@Override
					public void onPlay() {

					}

					@Override
					public void onPause() {

					}

					@Override
					public void onCompletion() {
						logo.setVisibility(View.GONE);
						mediaController.setProgressMax();
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

		videoView.setClick(new IjkVideoView.Click() {

			@Override
			public void callback(boolean start, boolean end) {
				mediaController.toggleVisiblity();
			}
		});

		videoView.setLeftUp(new IjkVideoView.LeftUp() {

			@Override
			public void callback(boolean start, boolean end) {
				int brightness = videoView.getBrightness() + 5;
				if (brightness > 100) {
					brightness = 100;
				}

				videoView.setBrightness(brightness);
			}
		});

		videoView.setLeftDown(new IjkVideoView.LeftDown() {

			@Override
			public void callback(boolean start, boolean end) {
				int brightness = videoView.getBrightness() - 5;
				if (brightness < 0) {
					brightness = 0;
				}

				videoView.setBrightness(brightness);
			}
		});

		videoView.setRightUp(new IjkVideoView.RightUp() {

			@Override
			public void callback(boolean start, boolean end) {
				// 加减单位最小为10，否则无效果
				int volume = videoView.getVolume() + 10;
				if (volume > 100) {
					volume = 100;
				}

				videoView.setVolume(volume);
			}
		});

		videoView.setRightDown(new IjkVideoView.RightDown() {

			@Override
			public void callback(boolean start, boolean end) {
				// 加减单位最小为10，否则无效果
				int volume = videoView.getVolume() - 10;
				if (volume < 0) {
					volume = 0;
				}

				videoView.setVolume(volume);
			}
		});

		videoView.setSwipeLeft(new IjkVideoView.SwipeLeft() {

			@Override
			public void callback(boolean start, boolean end) {
				// TODO 左滑事件
				Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
			}
		});

		videoView.setSwipeRight(new IjkVideoView.SwipeRight() {

			@Override
			public void callback(boolean start, boolean end) {
				// TODO 右滑事件
				Log.d(TAG, String.format("SwipeRight %b %b", start, end));
			}
		});

		mediaController = new OfflineMediaController(this, false,getIntent().getStringExtra("title"));
		mediaController.setIjkVideoView(videoView);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		mediaController.setOnBackListener(new OnBackListener() {

			@Override
			public void onBack() {

				IjkVideoActicity.this.finish();
			}
		});
		// 设置切屏事件
		mediaController
				.setOnBoardChangeListener(new OfflineMediaController.OnBoardChangeListener() {

					@Override
					public void onPortrait() {
						changeToLandscape();
					}

					@Override
					public void onLandscape() {
						changeToPortrait();
					}
				});

		// 设置视频尺寸 ，在横屏下效果较明显
		mediaController
				.setOnVideoChangeListener(new OfflineMediaController.OnVideoChangeListener() {

					@Override
					public void onVideoChange(final int layout) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								videoView.setVideoLayout(layout);
								switch (layout) {
								case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
									break;
								case IjkVideoView.VIDEO_LAYOUT_SCALE:
									break;
								case IjkVideoView.VIDEO_LAYOUT_STRETCH:
									break;
								case IjkVideoView.VIDEO_LAYOUT_ZOOM:
									break;
								}
							}
						});
					}
				});

		switch (playMode) {
		case landScape:
			changeToLandscape();
			break;

		case portrait:
			changeToPortrait();
			break;
		}

		switch (playType) {
		case vid:
			videoView.setVid(value);
			break;
		case url:
			progressBar.setVisibility(View.GONE);
			videoView.setVideoPath(value);
			break;
		}

		if (startNow) {
			videoView.start();
		} else {
			// if (playType == PlayType.vid) {
			// if (playerFirstStartView == null) {
			// playerFirstStartView = new PolyvPlayerFirstStartView(this);
			// playerFirstStartView.setCallback(new
			// PolyvPlayerFirstStartView.Callback() {
			//
			// @Override
			// public void onClickStart() {
			// videoView.start();
			// playerFirstStartView.hide();
			// }
			// });
			// }
			// }
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
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,
				adjusted_h);
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

		// if (playerFirstStartView != null && playerFirstStartView.isShowing())
		// {
		// playerFirstStartView.hide();
		// playerFirstStartView.refresh();
		// }

		if (adView != null && adView.isShowing()) {
			adView.hide();
			adView.refresh();
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// boolean value = mediaController.dispatchKeyEvent(event);
	// if (value)
	// return true;
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// if (videoView != null) {
		// videoView.release(true);
		// }
		//
		// if (questionView != null) {
		// questionView.hide();
		// }
		//
		// if (auditionView != null) {
		// auditionView.hide();
		// }

		// if (playerFirstStartView != null) {
		// playerFirstStartView.hide();
		// }

		// if (adView != null) {
		// adView.hide();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
	};

	/**
	 * 播放类型
	 * 
	 * @author TanQu
	 */
	public enum PlayType {
		/** 使用vid播放 */
		vid(1),
		/** 使用url播放 */
		url(2);

		private final int code;

		private PlayType(int code) {
			this.code = code;
		}

		/**
		 * 取得类型对应的code
		 * 
		 * @return
		 */
		public int getCode() {
			return code;
		}

		public static PlayType getPlayType(int code) {
			switch (code) {
			case 1:
				return vid;
			case 2:
				return url;
			}

			return null;
		}
	}

	/**
	 * 播放模式
	 * 
	 * @author TanQu
	 */
	public enum PlayMode {
		/** 横屏 */
		landScape(3),
		/** 竖屏 */
		portrait(4);

		private final int code;

		private PlayMode(int code) {
			this.code = code;
		}

		/**
		 * 取得类型对应的code
		 * 
		 * @return
		 */
		public int getCode() {
			return code;
		}

		public static PlayMode getPlayMode(int code) {
			switch (code) {
			case 3:
				return landScape;
			case 4:
				return portrait;
			}

			return null;
		}
	}
}
