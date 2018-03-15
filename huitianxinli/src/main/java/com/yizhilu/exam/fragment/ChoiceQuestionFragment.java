package com.yizhilu.exam.fragment;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.exam.PhotoActivity;
import com.yizhilu.exam.adapter.CompletionAdapter;
import com.yizhilu.exam.adapter.OptionsAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.utils.HtmlImageGetter;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ParamsUtil;
import com.yizhilu.utils.StringUtil;
import com.yizhilu.view.NoScrollListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 作者：caibin 时间：2016/6/4 15:42 类说明：选择题和判断题的类
 */
public class ChoiceQuestionFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {
	private TextView questionName, audio_progress_text, audio_zong_progress; // 试题的题目,音频当前进度和总进度
	private ImageView typeImage, audio_start_image, start_video; // 拓展类型图片,音频开始暂定图片,播放视频的图标
	private NoScrollListView optionListView, completionListView; // 选择和判断选项的列表,填空的列表
	private View inflate; // 总布局
	private int typePosition, examPosition, zongPosition, qstType; // 试题类型的下标,试题类型试题的下标,试题总的下标,试题类型
	private PublicEntity publicEntity; // 试题的实体
	private OptionsAdapter optionsAdapter; // 选项的适配器
	private String contentAddress, optOrder; // 拓展类型内容,多选选项
	private Intent examIntent; // 广播的意图
	private List<String> doubleAnswerList, completionList; // 存放多选答案的集合,填空答案集合
	private StringBuffer doubleBuffer; // 放多选答案的
	private EditText discussEdit; // 论述题输入框
	private CompletionAdapter completionAdapter; // 填空题的适配器
	private BroadCast broadCast; // 广播
	private ImageLoader imageLoader; // 加载图片的对象
	private Intent intent; // 意图对象
	private WebView analysisName; // 材料
	private LinearLayout audio_layout; // 音频布局
	private RelativeLayout video_layout; // 视频布局
	private SeekBar audio_seekBar; // 音频进度条
	private boolean isAudioStop = true; // 默认不是播放
	private MediaPlayer audioMediaPlayer; // 音频播放对象
	private int progress; // 播放进度

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.choice_question_layout, container, false);
		// 获取传过来的信息
		getIntentMessage();
		return inflate;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (broadCast == null) {
			broadCast = new BroadCast();
			IntentFilter filter = new IntentFilter();
			filter.addAction("completion");
			getActivity().registerReceiver(broadCast, filter);
		}
	}

	@Override
	public void initView() {
		intent = new Intent(); // 意图对象
		imageLoader = ImageLoader.getInstance(); // 实例化加载图片的对象
		doubleAnswerList = new ArrayList<String>(); // 存放多选答案的集合
		completionList = new ArrayList<String>(); // 填空答案的集合
		doubleBuffer = new StringBuffer(); // 存放多选的答案
		examIntent = new Intent("exam"); // 设置action
		analysisName = (WebView) inflate.findViewById(R.id.analysis_name); // 材料
		questionName = (TextView) inflate.findViewById(R.id.questionName); // 试题题目
		typeImage = (ImageView) inflate.findViewById(R.id.typeImage); // 拓展类型图片
		audio_layout = (LinearLayout) inflate.findViewById(R.id.audio_layout); // 音频布局
		audio_start_image = (ImageView) inflate.findViewById(R.id.audio_start_image); // 音频开始暂停图片
		audio_progress_text = (TextView) inflate.findViewById(R.id.audio_progress_text); // 音频当前进度
		audio_zong_progress = (TextView) inflate.findViewById(R.id.audio_zong_progress); // 音频总进度
		audio_seekBar = (SeekBar) inflate.findViewById(R.id.audio_seekBar); // 进度条
		video_layout = (RelativeLayout) inflate.findViewById(R.id.video_layout); // 视频布局
		start_video = (ImageView) inflate.findViewById(R.id.start_video); // 播放视频的图标
		optionListView = (NoScrollListView) inflate.findViewById(R.id.optionListView); // 选项的列表
		discussEdit = (EditText) inflate.findViewById(R.id.discuss_eidt); // 论述题输入框
		completionListView = (NoScrollListView) inflate.findViewById(R.id.completionListView); // 填空题列表
		if (!TextUtils.isEmpty(StringUtil.isFieldEmpty(publicEntity.getComplexContent()))) {
			analysisName.setVisibility(View.VISIBLE);
			analysisName.loadDataWithBaseURL(null, "材料 : " + publicEntity.getComplexContent(), "html/text", "utf-8",
					null);
		}
		questionName.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
		questionName.setMovementMethod(LinkMovementMethod.getInstance());// 设置超链接可以打开网页
		Drawable defaultDrawable = getResources().getDrawable(R.drawable.sprite);
		questionName.setText(Html.fromHtml(publicEntity.getQstContent(),
				new HtmlImageGetter(getActivity(), questionName, "/esun_msg", defaultDrawable), null)); // 试题题目
		qstType = publicEntity.getQstType(); // 获取试题的类型
		if (qstType == 0) { // 材料题下面的题型qstType为0
			qstType = publicEntity.getQuestionType();
		}
		String extendContentType = publicEntity.getExtendContentType(); // 获取拓展类型
		this.contentAddress = publicEntity.getContentAddress(); // 拓展类型内容
		if ("1".equals(extendContentType)) { // 音频
			audio_layout.setVisibility(View.VISIBLE);
		} else if ("2".equals(extendContentType)) { // 视频
			video_layout.setVisibility(View.VISIBLE);
		} else if ("3".equals(extendContentType)) { // 图片
			typeImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(ExamAddress.IMAGE_HOST + contentAddress, typeImage, HttpUtils.getDisPlay());
		}
		if (qstType == 1 || qstType == 2 || qstType == 3) { // 选择和判断类型
			optionListView.setVisibility(View.VISIBLE);
			if (qstType == 2) {
				String positionAnswer = StaticUtils.getPositionAnswer(zongPosition - 1);// 获取该试题用户的答案
				String userAnswer = StringUtil.isFieldEmpty(publicEntity.getUserAnswer()); // 获取已作答的答案
				if (TextUtils.isEmpty(positionAnswer)) {
					for (int i = 0; i < publicEntity.getOptions().size(); i++) {
						String order = publicEntity.getOptions().get(i).getOptOrder();
						if (userAnswer.contains(order)) {
							doubleAnswerList.add(order); // 向集合中添加选中的答案
							doubleBuffer.append(order + ",");
						} else {
							doubleAnswerList.add(""); // 向集合中添加空的答案
							doubleBuffer.append("");
						}
					}
					if (doubleBuffer.toString().contains(",")) {
						doubleBuffer.deleteCharAt(doubleBuffer.length() - 1);
					}
					StaticUtils.setPositionAnswer(zongPosition - 1, doubleBuffer.toString());
				} else {
					for (int i = 0; i < publicEntity.getOptions().size(); i++) {
						doubleAnswerList.add("");
					}
				}
			}
			optionsAdapter = new OptionsAdapter(getActivity(), publicEntity, zongPosition);
			optionListView.setAdapter(optionsAdapter);
		} else if (qstType == 6) { // 论述题
			String positionAnswer = StaticUtils.getPositionAnswer(zongPosition - 1);// 获取该试题用户的答案
			discussEdit.setVisibility(View.VISIBLE);
			discussEdit.setText(positionAnswer); // 设置已做过的答案
			discussEdit.setOnFocusChangeListener(this); // 获取和失去焦点的事件
		} else if (qstType == 7) { // 填空题
			for (int i = 0; i < publicEntity.getFillList().size(); i++) {
				completionList.add("");
			}
			completionListView.setVisibility(View.VISIBLE);
			completionAdapter = new CompletionAdapter(getActivity(), publicEntity, zongPosition, completionList);
			completionListView.setAdapter(completionAdapter);
		}

	}

	/**
	 * @author bin 时间: 2016/6/4 17:54 方法说明:获取传过来的信息
	 */
	public void getIntentMessage() {
		Bundle arguments = getArguments(); // 获取传过来的信息
		publicEntity = (PublicEntity) arguments.getSerializable("entity"); // 考试的总的实体
		typePosition = arguments.getInt("typePosition", -1); // 试题类型的下标
		examPosition = arguments.getInt("examPosition", 0); // 试题类型试题的下标
		zongPosition = arguments.getInt("zongPosition", 0); // 试题总的下标
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		super.onFocusChange(v, hasFocus);
		switch (v.getId()) {
		case R.id.discuss_eidt: // 论述题输入框
			if (!hasFocus) {
				StaticUtils.setPositionAnswer(zongPosition - 1, discussEdit.getText().toString());
			}
			break;
		}
	}

	@Override
	public void addOnClick() {
		optionListView.setOnItemClickListener(this); // 选项的点击事件
		typeImage.setOnClickListener(this); // 图片点击事件
		audio_start_image.setOnClickListener(this); // 暂停和开始按钮
		audio_seekBar.setOnSeekBarChangeListener(this); // 进度条的监听事件
		start_video.setOnClickListener(this); // 视频播放
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.typeImage: // 图片的点击事件
			intent.setClass(getActivity(), PhotoActivity.class);
			intent.putExtra("url", this.contentAddress);
			startActivity(intent);
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
			audio_start_image.setBackgroundResource(R.mipmap.audio_pause_bg);
			audioMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(contentAddress));
			audio_progress_text.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getCurrentPosition()));
			audio_zong_progress.setText(ParamsUtil.millsecondsToStr(audioMediaPlayer.getDuration()));
			audioMediaPlayer.start();
			isAudioStop = false;
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.optionListView: // 选项的列表
			if (qstType == 1 || qstType == 3) {
				// 获取点击的选项
				String optOrder = publicEntity.getOptions().get(position).getOptOrder();
				StaticUtils.setPositionAnswer(zongPosition - 1, optOrder);
				optionsAdapter.notifyDataSetChanged();
				examIntent.putExtra("position", zongPosition); // 当前试题是第几题
				getActivity().sendBroadcast(examIntent);
			} else if (qstType == 2) { // 点击多选的选项
				// 获取多选的答案
				String practice = StaticUtils.getAnswerList().get(zongPosition - 1);
				for (int i = 0; i < doubleAnswerList.size(); i++) {
					String order = publicEntity.getOptions().get(i).getOptOrder();
					if (practice.contains(order)) {
						doubleAnswerList.set(i, order);
					}
				}
				doubleBuffer = new StringBuffer(); // 存放多选的答案
				TextView option_text = (TextView) view.findViewById(R.id.optionImage);
				TextView optionContext_text = (TextView) view.findViewById(R.id.optionContent);
				optOrder = publicEntity.getOptions().get(position).getOptOrder(); // 获取点击的选项
				// 判断选中的选项中是否存在点击的答案
				if (TextUtils.isEmpty(doubleAnswerList.get(position))) {
					doubleAnswerList.set(position, optOrder); // 不存在选择的答案
					option_text.setBackgroundResource(R.mipmap.double_selected);
					option_text.setTextColor(getResources().getColor(R.color.white));
					for (int i = 0; i < doubleAnswerList.size(); i++) {
						String answer = doubleAnswerList.get(i);
						if (TextUtils.isEmpty(answer)) {
							doubleBuffer.append("");
						} else {
							doubleBuffer.append(answer + ",");
						}
					}
					doubleBuffer.deleteCharAt(doubleBuffer.length() - 1);
					StaticUtils.setPositionAnswer(zongPosition - 1, doubleBuffer.toString());
				} else {
					doubleAnswerList.set(position, ""); // 存在选择的答案
					option_text.setBackgroundResource(R.mipmap.double_select);
					option_text.setTextColor(getResources().getColor(R.color.color_main));
					for (int i = 0; i < doubleAnswerList.size(); i++) {
						String answer = doubleAnswerList.get(i);
						if (TextUtils.isEmpty(answer)) {
							doubleBuffer.append("");
						} else {
							doubleBuffer.append(answer + ",");
						}
					}
					if (!TextUtils.isEmpty(doubleBuffer.toString())) {
						doubleBuffer.deleteCharAt(doubleBuffer.length() - 1);
					}
					StaticUtils.setPositionAnswer(zongPosition - 1, doubleBuffer.toString());
				}
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		if (audioMediaPlayer != null) {
			destroyAudio();
		}
		super.onDestroy();
		if (broadCast != null) {
			getActivity().unregisterReceiver(broadCast);
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

	class BroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("completion".equals(action)) {
				if (qstType == 6) {
					StaticUtils.setPositionAnswer(zongPosition - 1, discussEdit.getText().toString());
				} else if (qstType == 7) {
					if (completionAdapter != null) {
						completionAdapter.setAnswer();
					}
				}
			}
		}

	}
}
