package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ValidateUtil;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-20 上午9:41:33
 * 类说明:意见反馈的类
 */
public class OpinionFeedbackActivity extends BaseActivity {

	private LinearLayout feedbackBack; //返回
	private int userId;  //用户Id
	private EditText content,number; //内容 电话号 
	private Button feedback; //反馈按钮
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private ProgressDialog progressDialog;  //联网获取数据显示的dialog
	private String userNumber,userContent; //用户内容 手机号
	private TextView title; //标题
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_opinion_feedback);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initView() {
		feedbackBack = (LinearLayout) findViewById(R.id.back_layout); //返回
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		progressDialog = new ProgressDialog(OpinionFeedbackActivity.this); //联网获取数据显示的dialog
		content = (EditText) findViewById(R.id.feedback_content); //内容
		number = (EditText) findViewById(R.id.feedback_number); //电话号
		feedback = (Button) findViewById(R.id.feedback_button); //反馈按钮
		title = (TextView) findViewById(R.id.title_text); //标题
		title.setText("意见反馈");
	}

	@Override
	public void addOnClick() {
       feedbackBack.setOnClickListener(this); //返回
       feedback.setOnClickListener(this); //反馈按钮
	}
	
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-20 下午1:31:02
	 * 方法说明:意见反馈的方法
	 */
	private void getHelpFeedback(final int userId,String contact,String userContent){
		RequestParams params=new RequestParams();
		params.put("userFeedback.userId", userId);
		params.put("contact", contact);
		params.put("userFeedback.content", userContent);
		httpClient.post(Address.HELP_FEEDBACK, params, new TextHttpResponseHandler() {
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
						if (publicEntity.isSuccess()) {
							ConstantUtils.showMsg(OpinionFeedbackActivity.this, "您的反馈已成功提交！");
							finish();
						}else{
							ConstantUtils.showMsg(OpinionFeedbackActivity.this, message);
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout://返回
			finish();
			break;
		case R.id.feedback_button: //反馈按钮
			userId = getSharedPreferences("userId",MODE_PRIVATE).getInt("userId",0);  //获取用户Id
			if (userId == 0) {
				Intent intent =new Intent(OpinionFeedbackActivity.this,LoginActivity.class);
				startActivity(intent);
				return;
			}
			userNumber = number.getText().toString(); 
			userContent = content.getText().toString();
			if(TextUtils.isEmpty(userContent)){
				ConstantUtils.showMsg(OpinionFeedbackActivity.this,"请输入您的反馈内容");
				return;
			}
			if(TextUtils.isEmpty(userNumber)){
				ConstantUtils.showMsg(OpinionFeedbackActivity.this,"请输入您的QQ/邮箱/手机号");
				return;
			}
			if(ValidateUtil.isMobile(userNumber)){
				getHelpFeedback(userId,userNumber,userContent);
			}else if(ValidateUtil.isEmail(userNumber)){
				getHelpFeedback(userId,userNumber,userContent);
			}else if(ValidateUtil.isNumbers(userNumber)&&userNumber.length()>=5){
				getHelpFeedback(userId,userNumber,userContent);
			}else{
				ConstantUtils.showMsg(OpinionFeedbackActivity.this,"请输入正确的QQ/邮箱/手机号格式");
			}
		default:
			break;
		}
	}


}
