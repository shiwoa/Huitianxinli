package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2016-2-1 上午9:52:53
 * 类说明:解除第三方绑定确认的类
 */
public class RemoveBindingConfigActivity extends BaseActivity {
	private LinearLayout back_layout,errorMessage_layout;  //返回的布局,错误信息的布局
	private TextView title_text,show_message,userName_text,relieve_binding,error_message;  //标题,提示的信息,用户名,解除绑定,错误信息
	private Intent intent;  //意图对象
	private EntityPublic userExpandDto,thridEntity;  //个人信息的实体,第三方的实体
	private String thridType;  //第三方类型
	private EditText passWord_edit;  //密码
	private View passWord_line;  //密码的线
	private int userId;  //用户Id
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private ProgressDialog progressDialog;  //获取数据显示的dialog
	private TextView relieve_binding_cancel; //取消
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_config_remove_binding);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 下午2:45:46
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		intent = getIntent();
		userExpandDto = (EntityPublic) intent.getSerializableExtra("EntityPublic");
		thridEntity = (EntityPublic) intent.getSerializableExtra("entity");
		userId = userExpandDto.getId();
		thridType = thridEntity.getProfiletype();
	}
	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		progressDialog = new ProgressDialog(RemoveBindingConfigActivity.this);  //获取数据显示的dialog
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		title_text.setText(R.string.relieve_binding);  //立即解绑
//		show_message = (TextView) findViewById(R.id.show_message);  //显示的信息
//		if("QQ".equals(thridType)){
//			show_message.setText("解除绑定后将不在使用"+thridType+"登录"+getResources().getString(R.string.app_name));
//		}else if("WEIXIN".equals(thridType)){
//			show_message.setText("解除绑定后将不在使用微信登录"+getResources().getString(R.string.app_name));
//		}else if("SINA".equals(thridType)){
//			show_message.setText("解除绑定后将不在使用新浪微博登录"+getResources().getString(R.string.app_name));
//		}
		userName_text = (TextView) findViewById(R.id.userName_text);  //用户名
		int id = userExpandDto.getId();
		String email = userExpandDto.getEmail();
		String mobile = userExpandDto.getMobile();
		if(!email.isEmpty()){
			userName_text.setText(email);
		}else if(!mobile.isEmpty()){
			userName_text.setText(mobile);
		}
		passWord_edit = (EditText) findViewById(R.id.passWord_edit);  //密码
		passWord_line = findViewById(R.id.passWord_line);  //密码的线
		errorMessage_layout = (LinearLayout) findViewById(R.id.errorMessage_layout);  //错误的布局
		error_message = (TextView) findViewById(R.id.error_message);  //错误信息 
		relieve_binding = (TextView) findViewById(R.id.relieve_binding);  //解除绑定
		relieve_binding_cancel = (TextView) findViewById(R.id.relieve_binding_cancel); //取消
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回
//		passWord_edit.setOnFocusChangeListener(this);  //获取焦点的事件
		relieve_binding.setOnClickListener(this);  //解除绑定
		relieve_binding_cancel.setOnClickListener(this); //取消
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;
		case R.id.relieve_binding:  //解除绑定
			errorMessage_layout.setVisibility(View.GONE);
			error_message.setText("");
			String passWord = passWord_edit.getText().toString();
			if (TextUtils.isEmpty(passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入密码");
				return;
			}
			int id = thridEntity.getId();
			// 解绑的三方登陆的方法
			getUnBundling(id, userId,passWord);
			break;
		case R.id.relieve_binding_cancel: //取消
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 下午4:33:24
	 * 方法说明:解除绑定的方法
	 */
	private void getUnBundling(int id, int userId, String passWord) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("userId", userId);
		params.put("userPassword", passWord);
		Log.i("lala", Address.UNBUNDLING+"?"+params.toString());
		httpClient.post(Address.UNBUNDLING, params , new TextHttpResponseHandler() {
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
							ConstantUtils.showMsg(RemoveBindingConfigActivity.this, message);
							RemoveBindingConfigActivity.this.finish();
						}else{
							ConstantUtils.showMsg(RemoveBindingConfigActivity.this, message);
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
	
//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		super.onFocusChange(v, hasFocus);
//		switch (v.getId()) {
//		case R.id.passWord_edit: // 密码
//			if (hasFocus) {
//				passWord_line.setBackgroundResource(R.color.lanse);
//				errorMessage_layout.setVisibility(View.GONE);
//				error_message.setText("");
//			} else {
//				passWord_line.setBackgroundResource(R.color.color_F6);
//			}
//			break;
//		default:
//			break;
//		}
//	}
}
