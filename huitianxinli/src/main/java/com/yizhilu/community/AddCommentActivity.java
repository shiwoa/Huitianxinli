package com.yizhilu.community;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.MessageEntity;
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

//社区-发表评论
public class AddCommentActivity extends BaseActivity {
	private LinearLayout back_layout, email_regist;// back...
	private TextView title_text, email_text;// title...,发送
	private AsyncHttpClient httpClient;// 网络连接
	private int topicId, userId;// 小组Id,用户Id
	private ProgressDialog progressDialog;// loading...
	private EditText edit_comment;// 评论输入框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_add_comment);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		topicId = intent.getIntExtra("topicId", 0);
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		email_regist = (LinearLayout) findViewById(R.id.email_regist);
		title_text = (TextView) findViewById(R.id.title_text);
		email_text = (TextView) findViewById(R.id.email_text);
		edit_comment = (EditText) findViewById(R.id.edit_comment);
		email_regist.setVisibility(View.VISIBLE);
		title_text.setText("写评论");// 设置标题
		email_text.setText("发送");
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
			AddCommentActivity.this.finish();
			break;
		case R.id.email_text:
			if (TextUtils.isEmpty(edit_comment.getText())) {
				Toast.makeText(AddCommentActivity.this, "请输入评论内容！", 0).show();
			} else {
				submitComment(topicId, userId, edit_comment.getText().toString().trim());// 提交话题评论
			}
			break;

		default:
			break;
		}
	}

	// 提交话题评论
	private void submitComment(int topicId, int userId, String commentContent) {
		RequestParams params = new RequestParams();
		params.put("topicId", topicId);
		params.put("userId", userId);
		params.put("commentContent", commentContent);
		Log.i("xm", Address.CREATECOMMENT + "?" + params);
		httpClient.post(Address.CREATECOMMENT, params, new TextHttpResponseHandler() {
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
							Toast.makeText(AddCommentActivity.this, messageEntity.getMessage(), 0).show();
							AddCommentActivity.this.finish();
						} else {
							if (TextUtils.isEmpty(messageEntity.getMessage())) {
								Toast.makeText(AddCommentActivity.this, "评论失败！", 0).show();
							} else {
								Toast.makeText(AddCommentActivity.this, messageEntity.getMessage(), 0).show();
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

}
