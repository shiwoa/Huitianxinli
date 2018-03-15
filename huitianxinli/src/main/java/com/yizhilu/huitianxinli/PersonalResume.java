package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-13 下午4:06:36
 * 类说明:个人简历的类
 */
public class PersonalResume extends BaseActivity {
	private RelativeLayout title_layout;  //title布局
	private LinearLayout back_layout;  //返回的布局
	private TextView title_text,last_time;  //标题,登录时间
	private TextView resume_userInfo,regist_time,login_number,look_video,join_exam,publish_note,publish_comment; //简介,注册时间,登录次数,观看视频,参加考试,发表日记,发表评论
	private int userId;  //用户Id
	private ProgressDialog progressDialog;  //加载数据显示的dialog
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private ImageLoader imageLoader;  //加载图片对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_personal_resume);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		progressDialog = new ProgressDialog(PersonalResume.this);  //获取数据显示的dialog
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		imageLoader = ImageLoader.getInstance();  //实例化加载图片对象
		title_layout = (RelativeLayout) findViewById(R.id.title_layout);  //title的布局
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		title_text.setText(R.string.study_statistics);  //设置标题
		last_time = (TextView) findViewById(R.id.last_time);  //登录时间
		regist_time = (TextView) findViewById(R.id.regist_time); //注册时间
		login_number = (TextView) findViewById(R.id.login_number);  //登录次数
		look_video = (TextView) findViewById(R.id.look_video); //观看视频
		join_exam = (TextView) findViewById(R.id.join_exam); //参加考试
		publish_note = (TextView) findViewById(R.id.publish_note); //发表笔记
		publish_comment = (TextView) findViewById(R.id.publish_comment); //发表评论
		//获取个人简历的方法
		getPersonalResume(userId);
	}

	/**
	 * 获取个人简历的方法
	 */
	private void getPersonalResume(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("lala", Address.GET_PERSONAL_RESUME+"?"+params.toString());
		httpClient.post(Address.GET_PERSONAL_RESUME, params , new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if(publicEntity.isSuccess()){
							EntityPublic userEntity = publicEntity.getEntity().getUser();
							String timeString = userEntity.getLastSystemTime();
							String month = timeString.split(":")[0];
							String time = timeString.split(":")[1].split(":")[0];
							last_time.setText(month+":"+time);
							String timeStringg = userEntity.getCreatedate();
							String monthh = timeStringg.split(":")[0];
							String timee = timeStringg.split(":")[1].split(":")[0];
							regist_time.setText(monthh+":"+timee);
							login_number.setText(userEntity.getLoginNum()+"");
							look_video.setText(userEntity.getStudyNum()+"");
							join_exam.setText(userEntity.getExamNum()+"");
							publish_note.setText(userEntity.getNoteNum()+"");
							publish_comment.setText(userEntity.getAssessNum()+"");
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

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回的布局
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;

		default:
			break;
		}
	}
}
