package com.yizhilu.community;

import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.adapter.TopicTypeAdapter;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//社区-发表话题-选择类型
public class TopicSelectTypeActivity extends BaseActivity {
	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private PublicEntity publicEntity;
	private ListView type_list;// 类型的列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_topic_select_type);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		publicEntity = (PublicEntity) intent.getSerializableExtra("subjectList");
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		title_text = (TextView) findViewById(R.id.title_text);
		type_list = (ListView) findViewById(R.id.type_list);
		title_text.setText("选择类型");// 设置标题
		type_list.setAdapter(
				new TopicTypeAdapter(TopicSelectTypeActivity.this, publicEntity.getEntity().getSubjectList()));
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			TopicSelectTypeActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
