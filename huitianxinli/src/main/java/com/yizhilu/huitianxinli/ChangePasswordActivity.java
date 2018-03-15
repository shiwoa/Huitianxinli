package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;

public class ChangePasswordActivity extends BaseActivity {

	private EditText oldPwd,newPwd,resumeLoad; //旧密码 新密码 重新输入
	private Button preserve; //保存按钮
	ProgressDialog pdDialog;
	private LinearLayout back,correct; //返回 错误
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private ProgressDialog progressDialog;  //获取数据显示dialog
	private TextView correctContent; //错误内容
	private int userId; //用户Id
	private View oldLine,newLine,inputLine; //线
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_change_password);
		super.onCreate(savedInstanceState);
	}
	
	@Override 
	public void initView() {
		oldPwd = (EditText) findViewById(R.id.old_password); //旧密码
		newPwd = (EditText) findViewById(R.id.new_password); //新密码
		resumeLoad = (EditText) findViewById(R.id.password_resumeLoad); //重新输入
		preserve = (Button) findViewById(R.id.password_preserve); //保存
		pdDialog = new ProgressDialog(this);
		back =  (LinearLayout) findViewById(R.id.change_password_back); //返回
		correct = (LinearLayout) findViewById(R.id.pwd_correct); //错误提示
		httpClient = new AsyncHttpClient(); //联网获取数据的方法
		progressDialog = new ProgressDialog(this);  //获取数据显示dialog
		correctContent = (TextView) findViewById(R.id.pwd_CorrectContent); //错误内容
		oldLine=findViewById(R.id.oldpwd_line);
		newLine =findViewById(R.id.newpwd_line);
		inputLine = findViewById(R.id.inputpwd_line);
	}

	@Override
	public void addOnClick() {
		preserve.setOnClickListener(this); //保存
		back.setOnClickListener(this); //返回
		oldPwd.setOnFocusChangeListener(this); //旧密码
		newPwd.setOnFocusChangeListener(this); //新密码
		resumeLoad.setOnFocusChangeListener(this); //再次输入
		
	}
	
	private void getUpdate_Password(int userId,String newpwd,String oldpwd){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("newpwd", newpwd);
		params.put("oldpwd", oldpwd);
		Log.i("lala",Address.UPDATE_PASSWORD+"?"+params.toString());
		httpClient.post(Address.UPDATE_PASSWORD, params,new TextHttpResponseHandler(){

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
						JSONObject parseObject = JSON.parseObject(data);
						String message = parseObject.getString("message");
						boolean success = parseObject.getBoolean("success");
						if (success) {
							ConstantUtils.showMsg(ChangePasswordActivity.this, message);
							ChangePasswordActivity.this.finish();
						}else {
							ConstantUtils.showMsg(ChangePasswordActivity.this, message);
						}
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) {
			    HttpUtils.exitProgressDialog(progressDialog);
			}

			
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.password_preserve:  //保存
			String oldpwd = oldPwd.getText().toString(); //获取旧密码
			String newpwd = newPwd.getText().toString(); // 获取新密码
			String resumeload = resumeLoad.getText().toString();
			if (TextUtils.isEmpty(oldpwd)) {
				correctContent.setText("请输入旧密码");
				correct.setVisibility(View.VISIBLE);
				return;
			}
			if(TextUtils.isEmpty(newpwd)){
				correctContent.setText("请输入新密码");
				correct.setVisibility(View.VISIBLE);
				return;
			}
			if(TextUtils.isEmpty(resumeload)){
				correctContent.setText("请再输入一次密码");
				correct.setVisibility(View.VISIBLE);
				return;
			}
			if (newpwd.equals(resumeload)) {
				userId=getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
				getUpdate_Password(userId, newpwd, oldpwd);
			}else {
				correctContent.setText("两次新密码不一致");
				correct.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.change_password_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	/* 
	 * 失去或获取焦点的监听
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		super.onFocusChange(v, hasFocus);
		switch (v.getId()) {
		case R.id.old_password: //旧密码
			if (hasFocus) {
//				oldLine.setBackgroundResource(R.color.Blue);
				correct.setVisibility(View.GONE);
				correctContent.setText("");
			}else {
				oldLine.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.new_password: //新密码
			if (hasFocus) {
//				newLine.setBackgroundResource(R.color.Blue);
				correct.setVisibility(View.GONE);
				correctContent.setText("");
			}else {
				newLine.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.password_resumeLoad: //再次输入
			if (hasFocus) {
//				inputLine.setBackgroundResource(R.color.Blue);
				correct.setVisibility(View.GONE);
				correctContent.setText("");
			}else {
				inputLine.setBackgroundResource(R.color.color_F6);
			}
			break;

		default:
			break;
		}
	}

}
