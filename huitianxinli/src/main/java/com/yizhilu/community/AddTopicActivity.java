package com.yizhilu.community;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//社区-发表话题
public class AddTopicActivity extends BaseActivity {
	private LinearLayout back_layout, email_regist;// back...
	private TextView title_text, email_text;// title...,发送,选择类型
	private AsyncHttpClient httpClient;// 网络连接
	private int groupId, userId;// 小组Id,用户Id
	private ProgressDialog progressDialog;// loading...
	private EditText edit_title, edit_content;// 标题输入框，内容输入框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_add_topic);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		groupId = intent.getIntExtra("GroupId", 0);
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		email_regist = (LinearLayout) findViewById(R.id.email_regist);
		title_text = (TextView) findViewById(R.id.title_text);
		email_text = (TextView) findViewById(R.id.email_text);
		edit_title = (EditText) findViewById(R.id.edit_title);
		edit_content = (EditText) findViewById(R.id.edit_content);
		email_regist.setVisibility(View.VISIBLE);
		title_text.setText("发表话题");// 设置标题
		email_text.setText("发表");
		httpClient = new AsyncHttpClient();
		progressDialog = new ProgressDialog(this);
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
		email_text.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			AddTopicActivity.this.finish();
			break;
		case R.id.email_text:
			if (TextUtils.isEmpty(edit_title.getText())) {
				Toast.makeText(AddTopicActivity.this, "标题不能为空", 0).show();
			} else if (edit_title.getText().length() < 4) {
				Toast.makeText(AddTopicActivity.this, "标题不能少于4个字符", 0).show();
			} else if (TextUtils.isEmpty(edit_content.getText())) {
				Toast.makeText(AddTopicActivity.this, "内容不能为空", 0).show();
			} else if (edit_content.getText().length() < 10) {
				Toast.makeText(AddTopicActivity.this, "内容不能少于10个字符", 0).show();
			} else {
				submitTopic(groupId, userId, edit_title.getText().toString().trim(),
						edit_content.getText().toString().trim());// 提交话题
			}
			break;

		default:
			break;
		}
	}

	// 提交话题
	private void submitTopic(int groupId, int userId, String title, String content) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		params.put("title", title);
		params.put("content", content);
		Log.i("xm", Address.ADDTOPIC + "?" + params);
		httpClient.post(Address.ADDTOPIC, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
						if (messageEntity.isSuccess()) {
							Intent intent = new Intent();
							intent.setAction("Change");
							intent.putExtra("topic", true);
							sendBroadcast(intent);
							Toast.makeText(AddTopicActivity.this, "发表成功！", 0).show();
							AddTopicActivity.this.finish();
						} else {
							if (TextUtils.isEmpty(messageEntity.getMessage())) {
								Toast.makeText(AddTopicActivity.this, "发表失败！", 0).show();
							} else {
								Toast.makeText(AddTopicActivity.this, messageEntity.getMessage(), 0).show();
							}
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

	// 获取话题类型-备注：好像没有用
	private void getTopicType(int groupId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		Log.i("xm", Address.TOPICTYPE + "?" + params);
		httpClient.post(Address.TOPICTYPE, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							Intent intent = new Intent(AddTopicActivity.this, TopicSelectTypeActivity.class);
							intent.putExtra("subjectList", publicEntity);
							startActivity(intent);
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

}
