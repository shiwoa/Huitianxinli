package com.yizhilu.exam;

import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;



import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.CompletionParserAdapter;
import com.yizhilu.exam.adapter.SingleOptionsAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ParamsUtil;
import com.yizhilu.utils.StringUtil;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin 时间: 2016/6/2 17:02 类说明:单题查看解析
 */
public class SingleLookParserActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {


	ImageView sideMenu;
	// 返回

	LinearLayout leftLayout;
	// 返回布局

	TextView title;
	// 标题

	TextView questionName;
	// 试题题目

	NoScrollListView questionList;
	// 选择和判断选项

	TextView rightAnswer;
	// 正确答案

	TextView myAnswer;

	ImageView rightImage;

	LinearLayout myAnswerLayout;
	// 我的答案的布局

	TextView questionParser;
	// 试题解析

	TextView questionNote;
	// 笔记

	LinearLayout answerLayout;
	// 答案对错的布局

	TextView discussMyAnswer;
	// 论述题我的答案

	LinearLayout discussLayout;
	// 论述题布局

	TextView completionNull;
	// 填空无答案显示

	NoScrollListView completionListView;
	// 填空用户答案的列表

	LinearLayout completionLayout;
	// 填空的布局

	NoScrollListView completionParserList;
	// 填空解析的列表

	WebView analysisName;
	// 材料题的材料

	ImageView typeImage;
	// 拓展类型图片

	TextView recordNote;
	// 记笔记
	private TextView audio_progress_text, audio_zong_progress; // 音频当前进度和总进度
	private ImageView audio_start_image, start_video; // 音频开始暂定图片,播放视频的图标
	private LinearLayout audio_layout; // 音频布局
	private LinearLayout completion_rightAnswer_layout; // 填空正确答案的布局
	private TextView completion_rightAnswer_null; // 无正确答案显示
	private RelativeLayout video_layout; // 视频布局
	private SeekBar audio_seekBar; // 音频进度条
	private boolean isAudioStop = true; // 默认不是播放
	private MediaPlayer audioMediaPlayer; // 音频播放对象
	private int progress; // 播放进度
	private Intent intent; // 意图对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private int userId, subjectId, id; // 用户Id,专业的Id,试卷的Id
	private PublicEntity publicEntity; // 解析的实体类
	private SingleOptionsAdapter singleOptionsAdapter; // 单题选项列表适配器
	private ImageLoader imageLoader; // 加载图片对象
	private String contentAddress; // 拓展类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_single_look_parsers);

		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	public void getIntentMessage() {
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0); // 获取试卷的Id
	}

	@Override
	public void initView() {
		intent = new Intent(); // 意图对象
		imageLoader = ImageLoader.getInstance(); // 加载图片对象
		progressDialog = new ProgressDialog(SingleLookParserActivity.this); // 加载数据显示的dialog
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户id
		subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0); // 获取专业id
		leftLayout.setVisibility(View.VISIBLE); // 返回的布局
		sideMenu.setVisibility(View.VISIBLE); // 返回的图片
		sideMenu.setBackgroundResource(R.mipmap.return_button); // 设置返回图片
		title.setText(R.string.look_parser); // 设置标题
		audio_layout = (LinearLayout) findViewById(R.id.audio_layout); // 音频布局
		audio_start_image = (ImageView) findViewById(R.id.audio_start_image); // 音频开始暂停图片
		audio_progress_text = (TextView) findViewById(R.id.audio_progress_text); // 音频当前进度
		audio_zong_progress = (TextView) findViewById(R.id.audio_zong_progress); // 音频总进度
		audio_seekBar = (SeekBar) findViewById(R.id.audio_seekBar); // 进度条
		video_layout = (RelativeLayout) findViewById(R.id.video_layout); // 视频布局
		start_video = (ImageView) findViewById(R.id.start_video); // 播放视频的图标
		completion_rightAnswer_layout = (LinearLayout) findViewById(R.id.completion_rightAnswer_layout); // 填空正确答案的布局
		completion_rightAnswer_null = (TextView) findViewById(R.id.completion_rightAnswer_null); // 无正确答案显示
		// 查看解析的方法
		getLookParserData(userId, id);
	}

	@Override
	public void addOnClick() {
		leftLayout.setOnClickListener(this); // 返回布局
		recordNote.setOnClickListener(this); // 记笔记
		audio_start_image.setOnClickListener(this); // 暂停和开始按钮
		audio_seekBar.setOnSeekBarChangeListener(this); // 进度条的监听事件
		start_video.setOnClickListener(this); // 视频播放
	}

	/**
	 * @author bin 时间: 2016/6/2 19:40 方法说明:获取查看解析的方法
	 */
	public void getLookParserData(int userId, int id) {
		RequestParams params = new RequestParams();
		params.put("cusId", userId);
		params.put("qstId", id);
		Log.i("lala", ExamAddress.LOOKSINGLEPARSER_URL + "?" + params.toString());
		DemoApplication.getHttpClient().post(ExamAddress.LOOKSINGLEPARSER_URL, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				ConstantUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				ConstantUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							PublicEntity questionEntity = publicEntity.getEntity().getQueryQuestion();
							questionName.setText(questionEntity.getQstContent()); // 设置题目
							questionNote.setText(StringUtil.isFieldEmpty(questionEntity.getNoteContent()));
							if (!TextUtils.isEmpty(StringUtil.isFieldEmpty(questionEntity.getComplexContent()))) {
								analysisName.setVisibility(View.VISIBLE);
								analysisName.loadDataWithBaseURL(null, "材料 : " + questionEntity.getComplexContent(),
										"html/text", "utf-8", null);
							}
							String extendContentType = questionEntity.getExtendContentType();
							contentAddress = questionEntity.getContentAddress();
							if ("1".equals(extendContentType)) { // 音频
								// contentAddress =
								// "http://static.268xue.com/upload/mavendemo/userFeedback/20151228/1451274540989933797.mp3";
								audio_layout.setVisibility(View.VISIBLE);
							} else if ("2".equals(extendContentType)) { // 视频
								video_layout.setVisibility(View.VISIBLE);
							} else if ("3".equals(extendContentType)) { // 图片
								typeImage.setVisibility(View.VISIBLE);
								imageLoader.displayImage(ExamAddress.IMAGE_HOST + contentAddress, typeImage,
										HttpUtils.getDisPlay());
							}
							int qstType = questionEntity.getQstType();
							if (qstType == 0) {
								qstType = questionEntity.getQuestionType();
							}
							if (qstType == 1 || qstType == 2 || qstType == 3) { // 选题和判断
								questionList.setVisibility(View.VISIBLE);
								answerLayout.setVisibility(View.VISIBLE);
								singleOptionsAdapter = new SingleOptionsAdapter(SingleLookParserActivity.this,
										questionEntity);
								questionList.setAdapter(singleOptionsAdapter);
								// 获取正确答案
								String isAsr = StringUtil.isFieldEmpty(questionEntity.getIsAsr());
								if (TextUtils.isEmpty(isAsr)) {
									rightAnswer.setText("正确答案 : 无");
								} else {
									rightAnswer.setText("正确答案 : " + isAsr);
								}
								// 获取我的答案
								String userAnswer = StringUtil.isFieldEmpty(questionEntity.getUserAnswer());
								if (TextUtils.isEmpty(userAnswer)) {
									myAnswerLayout.setBackgroundResource(R.mipmap.answer_error_bg);
									myAnswer.setText("您的答案 : 无");
									rightImage.setBackgroundResource(R.mipmap.error_question);
								} else {
									// 获取试题是否正确的值
									int qstRecordstatus = questionEntity.getQstRecordstatus();
									if (qstRecordstatus == 0) { // 对
										myAnswerLayout.setBackgroundResource(R.mipmap.answer_right_bg);
										myAnswer.setText("您的答案 : " + userAnswer);
										rightImage.setBackgroundResource(R.mipmap.right_question);
									} else { // 错
										myAnswerLayout.setBackgroundResource(R.mipmap.answer_error_bg);
										myAnswer.setText("您的答案 : " + userAnswer);
										rightImage.setBackgroundResource(R.mipmap.error_question);
									}
								}
								if (TextUtils.isEmpty(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()))) {
									// 设置试题解析
									questionParser.setText("无");
								} else {
									// 设置试题解析
									questionParser.setText(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()));
								}
							} else if (qstType == 6) { // 论述题
								discussLayout.setVisibility(View.VISIBLE);
								// 获取我的答案
								String userAnswer = StringUtil.isFieldEmpty(questionEntity.getUserAnswer());
								discussMyAnswer.setText(userAnswer);
								discussMyAnswer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
								discussMyAnswer.getPaint().setAntiAlias(true);// 抗锯齿
								if (TextUtils.isEmpty(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()))) {
									// 设置试题解析
									questionParser.setText("无");
								} else {
									// 设置试题解析
									questionParser.setText(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()));
								}
							} else if (qstType == 7) {
								completionLayout.setVisibility(View.VISIBLE);
								List<String> userFillList = questionEntity.getUserFillList();
								if (userFillList == null || userFillList.size() <= 0) {
									completionNull.setVisibility(View.VISIBLE);
								} else {
									completionListView.setVisibility(View.VISIBLE);
									completionListView.setAdapter(new CompletionParserAdapter(
											SingleLookParserActivity.this, questionEntity, true));
								}
								if (TextUtils.isEmpty(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()))) {
									// 设置试题解析
									questionParser.setText("无");
								} else {
									// 设置试题解析
									questionParser.setText(StringUtil.isFieldEmpty(questionEntity.getQstAnalyze()));
								}
								List<String> fillList = questionEntity.getFillList();
								if (fillList == null || fillList.size() <= 0) {
									completion_rightAnswer_layout.setVisibility(View.VISIBLE);
									completion_rightAnswer_null.setVisibility(View.VISIBLE);
									completion_rightAnswer_null.setText("无");
								} else {
									completion_rightAnswer_layout.setVisibility(View.VISIBLE);
									completionParserList.setVisibility(View.VISIBLE);
									completionParserList.setAdapter(new CompletionParserAdapter(
											SingleLookParserActivity.this, questionEntity, false));
								}
							}
						} else {
							ConstantUtils.showMsg(SingleLookParserActivity.this, message);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				ConstantUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_layout: // 返回
			this.finish();
			break;
		case R.id.recordNote: // 记笔记
			intent.setClass(SingleLookParserActivity.this, RecordNoteActivity.class);
			intent.putExtra("qstId", publicEntity.getEntity().getQueryQuestion().getId());
			intent.putExtra("noteContent", questionNote.getText().toString()); // 笔记内容
			startActivityForResult(intent, 0);
			break;
		case R.id.audio_start_image: // 音频暂停和开始
			// 暂停和播放的方法
			startOrStopAudio();
			break;
		case R.id.start_video: // 播放视频的事件
			break;
		}
	}

	/**
	 * @author bin 时间: 2016/6/29 15:56 方法说明:暂停和播放音频的方法
	 */
	public void startOrStopAudio() {
		if (audioMediaPlayer != null) {
			if (isAudioStop) {
				isAudioStop = false;
				audioMediaPlayer.start();
				audio_start_image.setBackgroundResource(R.mipmap.audio_pause_bg);
			} else {
				isAudioStop = true;
				audioMediaPlayer.pause();
				audio_start_image.setBackgroundResource(R.mipmap.audio_start_bg);
			}
		} else {
			audioMediaPlayer = MediaPlayer.create(SingleLookParserActivity.this, Uri.parse(contentAddress));
			audio_progress_text.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getCurrentPosition()));
			audio_zong_progress.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getDuration()));
			audioMediaPlayer.start();
			isAudioStop = false;
			audio_start_image.setBackgroundResource(R.mipmap.audio_pause_bg);
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
										try {
											if (audioMediaPlayer != null) {
												int position = audioMediaPlayer.getCurrentPosition();
												int duration = audioMediaPlayer.getDuration();
												if (duration > 0) {
													if (position >= duration) {
														destroyAudio();
													}
													long pos = audio_seekBar.getMax() * position / duration;
													audio_progress_text.setText(ParamsUtil
															.millsecondsToStr(audioMediaPlayer.getCurrentPosition()));
													audio_seekBar.setProgress((int) pos);
												}
											}
										} catch (Exception e) {
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
	}

	/**
	 * 结束音频播放
	 */
	private void destroyAudio() {
		audio_seekBar.setProgress(0);
		audioMediaPlayer.reset();
		audioMediaPlayer.release();
		audio_start_image.setBackgroundResource(R.mipmap.audio_start_bg);
		audio_progress_text.setText("");
		audio_zong_progress.setText("");
		audioMediaPlayer = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == resultCode && data != null) {
			String noteContent = data.getStringExtra("note");
			questionNote.setText(noteContent);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		this.progress = progress * audioMediaPlayer.getDuration() / seekBar.getMax();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		audioMediaPlayer.seekTo(progress);
	}

	@Override
	public void onPause() {
		if (audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
			audio_seekBar.setProgress(0);
			isAudioStop = true;
			audioMediaPlayer.pause();
			audio_start_image.setBackgroundResource(R.mipmap.audio_start_bg);
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (audioMediaPlayer != null) {
			destroyAudio();
		}
		super.onDestroy();
	}
}
