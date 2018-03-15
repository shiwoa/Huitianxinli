package com.yizhilu.exam.fragment;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.exam.RecordNoteActivity;
import com.yizhilu.exam.adapter.CompletionParserAdapter;
import com.yizhilu.exam.adapter.SingleOptionsAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.utils.HtmlImageGetter;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ParamsUtil;
import com.yizhilu.utils.StringUtil;
import com.yizhilu.view.NoScrollListView;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 作者：caibin 时间：2016/6/15 10:09 类说明：查看解析的fragfment
 */
public class LookParserFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {
	private View inflate; // 总布局
	private PublicEntity publicEntity; // 试题的实体
	private int examPosition, zongPosition, examType; // 试题类型试题的下标,试题总的下标,类型2为试卷
	private TextView questionName, audio_progress_text, audio_zong_progress; // 试题的题目,音频当前进度和总进度
	private TextView analysisName; // 材料
	private NoScrollListView questionList, completionListView, completionParserList; // 单选.判断选项列表,填空列表,填空解析列表
	private LinearLayout answerLayout, myAnswerLayout, discussLayout, completionLayout; // 答案的布局,我的答案的布局,论述题布局,填空题布局
	private LinearLayout completion_rightAnswer_layout; // 填空正确答案的布局
	private TextView completion_rightAnswer_null; // 填空无正确答案
	private TextView rightAnswer, myAnswer, discussMyAnswer, completionNull, questionParser; // 正确答案,我的答案,论述题我的答案,填空题我没答案,试题解析
	private ImageView rightImage, typeImage, audio_start_image, start_video; // 正确图标,拓展图片,音频开始暂停图片,播放视频的图标
	private SingleOptionsAdapter singleOptionsAdapter; // 单题选项列表适配器
	private TextView recordNote, question_note; // 记笔记,笔记内容
	private Intent intent; // 意图对象
	private String contentAddress; // 拓展内容
	private ImageLoader imageLoader; // 加载图片对象
	private String noteContent; // 笔记
	private LinearLayout audio_layout; // 音频布局
	private RelativeLayout video_layout; // 视频布局
	private SeekBar audio_seekBar; // 音频进度条
	private boolean isAudioStop = true; // 默认不是播放
	private MediaPlayer audioMediaPlayer; // 音频播放对象
	private int progress; // 播放进度

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_look_parser, container, false);
		// 获取传过来的信息
		getIntentMessage();
		return inflate;
	}

	/**
	 * @author bin 时间: 2016/6/4 17:54 方法说明:获取传过来的信息
	 */
	public void getIntentMessage() {
		Bundle arguments = getArguments(); // 获取传过来的信息
		publicEntity = (PublicEntity) arguments.getSerializable("entity"); // 考试的总的实体
		examType = arguments.getInt("examType", 0); // 获取类型
		examPosition = arguments.getInt("examPosition", 0); // 试题类型试题的下标
		zongPosition = arguments.getInt("zongPosition", 0); // 试题总的下标
	}

	@Override
	public void initView() {
		intent = new Intent(); // 意图对象
		imageLoader = ImageLoader.getInstance(); // 加载图片对象
		questionName = (TextView) inflate.findViewById(R.id.question_name); // 试题题目
		analysisName = (TextView) inflate.findViewById(R.id.analysis_name); // 材料
		typeImage = (ImageView) inflate.findViewById(R.id.typeImage); // 拓展类型图片
		video_layout = (RelativeLayout) inflate.findViewById(R.id.video_layout); // 视频布局
		start_video = (ImageView) inflate.findViewById(R.id.start_video); // 播放视频的图标
		audio_layout = (LinearLayout) inflate.findViewById(R.id.audio_layout); // 音频布局
		audio_start_image = (ImageView) inflate.findViewById(R.id.audio_start_image); // 音频开始暂停图片
		audio_progress_text = (TextView) inflate.findViewById(R.id.audio_progress_text); // 音频当前进度
		audio_zong_progress = (TextView) inflate.findViewById(R.id.audio_zong_progress); // 音频总进度
		audio_seekBar = (SeekBar) inflate.findViewById(R.id.audio_seekBar); // 进度条
		questionList = (NoScrollListView) inflate.findViewById(R.id.question_list); // 选项列表
		questionName.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
		questionName.setMovementMethod(LinkMovementMethod.getInstance());// 设置超链接可以打开网页
		Drawable defaultDrawable = getResources().getDrawable(R.drawable.sprite);
		questionName.setText(Html.fromHtml(publicEntity.getQstContent(),
				new HtmlImageGetter(getActivity(), questionName, "/esun_msg", defaultDrawable), null)); // 试题题目
		// questionName.setText(publicEntity.getQstContent()); //设置题目
		answerLayout = (LinearLayout) inflate.findViewById(R.id.answerLayout); // 答案的布局
		rightAnswer = (TextView) inflate.findViewById(R.id.right_answer); // 正确答案
		myAnswerLayout = (LinearLayout) inflate.findViewById(R.id.my_answer_layout); // 我的答案的布局
		myAnswer = (TextView) inflate.findViewById(R.id.my_answer); // 我的答案
		rightImage = (ImageView) inflate.findViewById(R.id.right_image); // 正确图片
		discussLayout = (LinearLayout) inflate.findViewById(R.id.discuss_layout); // 论述题布局
		discussMyAnswer = (TextView) inflate.findViewById(R.id.discuss_myAnswer); // 论述题我的答案
		completionLayout = (LinearLayout) inflate.findViewById(R.id.completion_layout); // 填空题布局
		completionNull = (TextView) inflate.findViewById(R.id.completion_null); // 填空我的答案为空
		completion_rightAnswer_layout = (LinearLayout) inflate.findViewById(R.id.completion_rightAnswer_layout); // 填空正确答案
		completion_rightAnswer_null = (TextView) inflate.findViewById(R.id.completion_rightAnswer_null); // 填空题无正确答案显示
		completionListView = (NoScrollListView) inflate.findViewById(R.id.completionListView); // 填空列表
		completionParserList = (NoScrollListView) inflate.findViewById(R.id.completion_parser_list); // 填空解析列表
		questionParser = (TextView) inflate.findViewById(R.id.question_parser); // 试题解析
		recordNote = (TextView) inflate.findViewById(R.id.recordNote); // 记笔记s
		question_note = (TextView) inflate.findViewById(R.id.question_note); // 显示笔记
		question_note.setText(StaticUtils.getNoteList().get(zongPosition - 1)); // 显示笔记内容
		if (!TextUtils.isEmpty(StringUtil.isFieldEmpty(publicEntity.getComplexContent()))) {
			analysisName.setVisibility(View.VISIBLE);
			// analysisName.loadDataWithBaseURL(null, "材料 :
			// "+publicEntity.getComplexContent(), "html/text", "utf-8", null);
			analysisName.setText(Html.fromHtml(publicEntity.getComplexContent(),
					new HtmlImageGetter(getActivity(), analysisName, "/esun_msg", defaultDrawable), null));
		}
		String extendContentType = publicEntity.getExtendContentType(); // 获取拓展类型
		contentAddress = publicEntity.getContentAddress();
		if ("1".equals(extendContentType)) { // 音频
			// contentAddress =
			// "http://static.268xue.com/upload/mavendemo/userFeedback/20151228/1451274540989933797.mp3";
			audio_layout.setVisibility(View.VISIBLE);
		} else if ("2".equals(extendContentType)) { // 视频
			video_layout.setVisibility(View.VISIBLE);
		} else if ("3".equals(extendContentType)) { // 图片
			typeImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(ExamAddress.IMAGE_HOST + contentAddress, typeImage, HttpUtils.getDisPlay());
		}
		int qstType = publicEntity.getQstType();
		if (qstType == 0) {
			qstType = publicEntity.getQuestionType();
		}
		if (qstType == 1 || qstType == 2 || qstType == 3) { // 选题和判断
			questionList.setVisibility(View.VISIBLE);
			answerLayout.setVisibility(View.VISIBLE);
			singleOptionsAdapter = new SingleOptionsAdapter(getActivity(), publicEntity);
			questionList.setAdapter(singleOptionsAdapter);
			// 获取正确答案
			String isAsr = StringUtil.isFieldEmpty(publicEntity.getIsAsr());

			if (TextUtils.isEmpty(isAsr)) {
				rightAnswer.setText("正确答案 : 无");
			} else {
				rightAnswer.setText("正确答案 : " + isAsr);
			}
			// 获取我的答案
			String userAnswer = StringUtil.isFieldEmpty(publicEntity.getUserAnswer());
			if (TextUtils.isEmpty(userAnswer)) {
				myAnswerLayout.setBackgroundResource(R.mipmap.answer_error_bg);
				myAnswer.setText("您的答案 : 无");
				rightImage.setBackgroundResource(R.mipmap.error_question);
			} else {
				// 获取试题是否正确的值
				int qstRecordstatus = publicEntity.getQstRecordstatus();
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
			if (TextUtils.isEmpty(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()))) {
				// 设置试题解析
				questionParser.setText("无");
			} else {
				// 设置试题解析
				questionParser.setText(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()));
			}
		} else if (qstType == 6) { // 论述题
			discussLayout.setVisibility(View.VISIBLE);
			// 获取我的答案
			String userAnswer = StringUtil.isFieldEmpty(publicEntity.getUserAnswer());
			discussMyAnswer.setText(userAnswer);
			discussMyAnswer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			discussMyAnswer.getPaint().setAntiAlias(true);// 抗锯齿
			if (TextUtils.isEmpty(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()))) {
				// 设置试题解析
				questionParser.setText("无");
			} else {
				// 设置试题解析
				questionParser.setText(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()));
			}
		} else if (qstType == 7) { // 填空
			completionLayout.setVisibility(View.VISIBLE);
			List<String> userFillList = publicEntity.getUserFillList();
			if (userFillList == null || userFillList.size() <= 0) {
				completionNull.setVisibility(View.VISIBLE);
			} else {
				completionListView.setVisibility(View.VISIBLE);
				completionListView.setAdapter(new CompletionParserAdapter(getActivity(), publicEntity, true));
			}
			if (TextUtils.isEmpty(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()))) {
				// 设置试题解析
				questionParser.setText("无");
			} else {
				// 设置试题解析
				questionParser.setText(StringUtil.isFieldEmpty(publicEntity.getQstAnalyze()));
			}
			List<String> fillList = publicEntity.getFillList();
			if (fillList == null || fillList.size() <= 0) {
				completion_rightAnswer_layout.setVisibility(View.VISIBLE);
				completion_rightAnswer_null.setVisibility(View.VISIBLE);
				completion_rightAnswer_null.setText("无");
			} else {
				completion_rightAnswer_layout.setVisibility(View.VISIBLE);
				completionParserList.setVisibility(View.VISIBLE);
				completionParserList.setAdapter(new CompletionParserAdapter(getActivity(), publicEntity, false));
			}
		}
	}

	@Override
	public void addOnClick() {
		recordNote.setOnClickListener(this); // 记笔记
		audio_start_image.setOnClickListener(this); // 暂停和开始按钮
		audio_seekBar.setOnSeekBarChangeListener(this); // 进度条的监听事件
		start_video.setOnClickListener(this); // 视频播放
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.recordNote: // 记笔记
			intent.setClass(getActivity(), RecordNoteActivity.class);
			if (examType != 2) {
				intent.putExtra("qstId", publicEntity.getId());
			} else {
				intent.putExtra("qstId", publicEntity.getQstId());
			}
			intent.putExtra("zongPosition", zongPosition); // 当前试题的下标
			intent.putExtra("noteContent", question_note.getText().toString());
			startActivityForResult(intent, 1);
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
			audioMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(contentAddress));
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
								getActivity().runOnUiThread(new Runnable() {

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == resultCode) {
			noteContent = data.getStringExtra("note");
			question_note.setText(noteContent);
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (!isVisibleToUser) {
			if (audioMediaPlayer != null && audioMediaPlayer.isPlaying()) {
				audio_seekBar.setProgress(0);
				isAudioStop = true;
				audioMediaPlayer.pause();
				audio_start_image.setBackgroundResource(R.mipmap.audio_start_bg);
			}
		}
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
