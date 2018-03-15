package com.yizhilu.fragment;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.fragment.MyFragment.OnSetUserMessageLisenner;
import com.yizhilu.huitianxinli.CelebrityLecturerActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.MyCourseActivity;
import com.yizhilu.huitianxinli.OpinionFeedbackActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.huitianxinli.SystemAcmActivity;
import com.yizhilu.huitianxinli.SystemSettingActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;

/**
 * @author bin 修改人: 时间:2015-10-19 上午10:53:01 类说明:侧滑菜单的类
 */
public class SlidingMenuFragment extends BaseFragment implements
		OnSetUserMessageLisenner {
	private static SlidingMenuFragment slidingMenuFragment; // 侧滑菜单的对象
	private View inflate; // 侧滑菜单的布局
	private RelativeLayout teacherLayout, courseLayout, setLayout,
			feedbackLayout, announcement; // 名家讲师 我的课程 系统设置 意见反馈 系统公告
	private CircleImageView image_head; // 用户头像
	private TextView userName; // 用户名
	private ImageLoader imageLoader; // 加载头像的对象
	private int userId; // 用户Id
	private AsyncHttpClient httpClient; // 联网获取数据的对象

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imageLoader = ImageLoader.getInstance();
		inflate = inflater.inflate(R.layout.fragment_sliding_menu, container,
				false);
		return inflate;
	}

	/**
	 * @author bin 修改人: 时间:2015-10-19 上午10:55:26 方法说明:获取侧滑菜单实例的方法
	 */
	public static SlidingMenuFragment getInstence() {
		if (slidingMenuFragment == null) {
			slidingMenuFragment = new SlidingMenuFragment();
		}
		return slidingMenuFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		userId = getActivity().getSharedPreferences("userId",
				getActivity().MODE_PRIVATE).getInt("userId", 0);
		if (userId != 0) {
			// 获取用户信息的方法
			getUserMessage();
		}else{
			userName.setText("未登录");
		}
	}

	@Override
	public void initView() {
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		image_head = (CircleImageView) inflate.findViewById(R.id.image_head); // 用户头像
		userName = (TextView) inflate.findViewById(R.id.userName); // 用户名
		teacherLayout = (RelativeLayout) inflate
				.findViewById(R.id.sliding_teacher); // 名家讲师
		courseLayout = (RelativeLayout) inflate
				.findViewById(R.id.sliding_myCourse); // 我的课程
		setLayout = (RelativeLayout) inflate
				.findViewById(R.id.sliding_systemSet); // 系统设置
		feedbackLayout = (RelativeLayout) inflate
				.findViewById(R.id.sliding_feedback); // 意见反馈
		announcement = (RelativeLayout) inflate
				.findViewById(R.id.sliding_announcement); // 系统公告
	}

	@Override
	public void addOnClick() {
		teacherLayout.setOnClickListener(this); // 名家讲师
		courseLayout.setOnClickListener(this); // 我的课程
		setLayout.setOnClickListener(this); // 系统设置
		feedbackLayout.setOnClickListener(this); // 意见反馈
		announcement.setOnClickListener(this); // 系统公告
		MyFragment.getInstance().setOnSetUserMessageLisenner(this); // 获取用户信息
	}

	@Override
	public void onClick(View v) {
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		Intent intent = new Intent();
		intent.setAction("close_sliding");
		getActivity().sendBroadcast(intent);
		super.onClick(v);
		switch (v.getId()) {
		case R.id.sliding_teacher: // 名家讲师
			intent.setClass(getActivity(), CelebrityLecturerActivity.class);
			startActivity(intent);
			break;
		case R.id.sliding_myCourse: // 我的课程
			if(userId == 0){
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}else{
				intent.setClass(getActivity(), MyCourseActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.sliding_systemSet: // 系统设置
			intent.setClass(getActivity(), SystemSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.sliding_feedback: // 意见反馈
			intent.setClass(getActivity(), OpinionFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.sliding_announcement: // 系统公告
			intent.setClass(getActivity(), SystemAcmActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午8:28:42 方法说明:联网获取用户信息的方法
	 */
	private void getUserMessage() {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		httpClient.post(Address.MY_MESSAGE, params,
				new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								if (publicEntity.isSuccess()) {
									EntityPublic userExpandDto = publicEntity
											.getEntity().getUserExpandDto();
									userName.setText(userExpandDto
											.getShowname());
									imageLoader.displayImage(Address.IMAGE_NET
											+ userExpandDto.getAvatar(),
											image_head, HttpUtils.getDisPlay());
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	/**
	 * 获取用户信息的方法
	 */
	@Override
	public void onSetUserMessage(String message) {
		Log.i("lala", message);
		if (!TextUtils.isEmpty(message)) {
			if("exit_login".equals(message)){
				image_head.setImageResource(R.drawable.head_bg);
				userName.setText("未登陆");
			}
		}
	}
}
