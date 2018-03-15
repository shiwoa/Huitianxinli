 package com.yizhilu.baoli;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yizhilu.huitianxinli.R;

public class PolyvDownloadListActivity extends Activity {
	private static final String TAG = "PolyvDownloadListActivity";
	private ListView list;
	private LinkedList<PolyvDownloadInfo> infos;
	private PolyvDBservice service;
	private PolyvDownloadListAdapter adapter;
	private TextView emptyView,title;
	private boolean isStop = false;
	private LinearLayout back; // 返回
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloadlist);
		list = (ListView) findViewById(R.id.list);
		emptyView = (TextView) findViewById(R.id.emptyView);
		back = (LinearLayout) findViewById(R.id.back_layout);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		title = (TextView) findViewById(R.id.title_text);
		title.setText("离线下载");
		list.setEmptyView(emptyView);
		initData();
	}

	private void initData() {
		service = new PolyvDBservice(this);
		infos = service.getDownloadFiles();
		adapter = new PolyvDownloadListAdapter(this, infos,list);
		list.setAdapter(adapter);
	}
}
