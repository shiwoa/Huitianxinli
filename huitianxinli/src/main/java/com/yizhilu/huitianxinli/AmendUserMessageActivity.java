package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * @author bin
 * 修改人:
 * 时间:2015-11-3 上午9:42:31
 * 类说明:修改用户信息的类
 */
public class AmendUserMessageActivity extends BaseActivity {
	private LinearLayout back_layout,sexLinearLayout,manLayout,womanLayout;  //返回的布局,修改性别的布局,男布局,女布局
	private RelativeLayout messageLayout;  //修改姓名以及邮箱的布局
	private ProgressDialog progressDialog;  //加载数据显示的dialog
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private String title,chenageMessage;  //标题,更改的内容
	private TextView title_text,save_message;  //标题,保存的按钮
	private EditText chenage_EditText;   //修改的内容
	private View chenage_line;  //线
	private int userId,sex;  //用户Id,性别
	private boolean isSex;  //是否是选择性别
	private ImageView manImage,womanImage;  //男女选中的图标
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_amend_usermessage);
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-11-3 上午9:54:02
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //用户Id
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		chenageMessage = intent.getStringExtra("message");
		sex = intent.getIntExtra("sex", 0);
	}
	/**
	 *	初始化控件的方法
	 */
	@Override
	public void initView() {
		progressDialog = new ProgressDialog(AmendUserMessageActivity.this);  //加载数据显示的dialog
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		title_text.setText(title);  //设置标题
		messageLayout = (RelativeLayout) findViewById(R.id.messageLayout);  //修改姓名以及邮箱的布局
		sexLinearLayout = (LinearLayout) findViewById(R.id.sexLinearLayout); // 修改性别的布局
		save_message = (TextView) findViewById(R.id.save_message);  //保存
		chenage_EditText = (EditText) findViewById(R.id.chenage_EditText);  //修改的内容
		chenage_EditText.setText(chenageMessage);
		chenage_line = findViewById(R.id.chenage_line);  //线
		manLayout = (LinearLayout) findViewById(R.id.manLayout);  //男布局
		womanLayout = (LinearLayout) findViewById(R.id.womanLayout);  //女布局
		manImage = (ImageView) findViewById(R.id.manImage);  //男选中的图标
		womanImage = (ImageView) findViewById(R.id.womanImage); //女选中的图标
		if("选择性别".equals(title)){
			isSex = true;
			save_message.setVisibility(View.GONE);
			messageLayout.setVisibility(View.GONE);
			sexLinearLayout.setVisibility(View.VISIBLE);
			if(sex == 0){
				manImage.setVisibility(View.VISIBLE);
				womanImage.setVisibility(View.GONE);
			}else{
				manImage.setVisibility(View.GONE);
				womanImage.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-11-3 上午10:10:38
	 * 方法说明:保存用户信息的方法
	 */
	public void saveUserMessage(int userId,String userMessage,int sex){
		RequestParams params = new RequestParams();
		params.put("user.id", userId);
		if(isSex){
			params.put("user.gender", sex);
		}else{
			if("修改昵称".equals(title)){
				params.put("user.nickname", userMessage);
			}else if("修改姓名".equals(title)){
				params.put("user.realname", userMessage);
			}else if("个性签名".equals(title)){
				params.put("user.userInfo", userMessage);
			}
		}
		Log.i("lala", Address.UPDATE_MYMESSAGE+"?"+params.toString());
		httpClient.post(Address.UPDATE_MYMESSAGE, params , new TextHttpResponseHandler() {
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
							ConstantUtils.showMsg(AmendUserMessageActivity.this, "修改成功");
							AmendUserMessageActivity.this.finish();
						}else{
							ConstantUtils.showMsg(AmendUserMessageActivity.this, message);
						}
					} catch (Exception e) {
						HttpUtils.exitProgressDialog(progressDialog);
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
		back_layout.setOnClickListener(this);  //返回
		save_message.setOnClickListener(this);  //保存
		manLayout.setOnClickListener(this);  //男布局
		womanLayout.setOnClickListener(this);  //女布局
		chenage_EditText.setOnFocusChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;
		case R.id.save_message:  //保存
			chenageMessage = chenage_EditText.getText().toString();
			//2代表不是选择性别
			saveUserMessage(userId, chenageMessage,2);
			break;
		case R.id.manLayout:
			sex = 0;
			manImage.setVisibility(View.VISIBLE);
			womanImage.setVisibility(View.GONE);
			saveUserMessage(userId,null,sex);
			break;
		case R.id.womanLayout:
			sex = 1;
			manImage.setVisibility(View.GONE);
			womanImage.setVisibility(View.VISIBLE);
			saveUserMessage(userId,null,sex);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		super.onFocusChange(v, hasFocus);
		switch (v.getId()) {
		case R.id.chenage_EditText:
			if (hasFocus) {
				chenage_line.setBackgroundResource(R.color.Blue);
			}else {
				chenage_line.setBackgroundResource(R.color.color_F6);
			}
			break;

		default:
			break;
		}
	}
}
