package com.yizhilu.baoli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CourseParentContentsAdapter;
import com.yizhilu.adapter.DownloadListAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollExpandableListView;
import com.yizhilu.view.NoScrollGridView;

/**
 * @author bin
 * 修改人:
 * 时间:2015-11-2 下午9:53:07
 * 类说明:选择要下载的视频
 */
public class BLDownLoadSelectActivity extends BaseActivity{
	private LinearLayout back_layout;  //返回的布局
	private TextView title; // 标题名字
	private ProgressDialog progressDialog;  //加載數據顯示的dialog
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private NoScrollGridView gridView;  //课程根目录的布局
	private List<EntityCourse> packageList;  //课程父目录的集合
	private int courseId, userId;
	private int downloadNumber = 0; // 已经选中的下载数量
	private NoScrollExpandableListView mListView; //課程的二級列表
	private Button mButton; // 下载的按钮
	private List<EntityCourse> coursePackageList; // 视频包
	private List<EntityCourse> courseKpoints; // 数据
	private List<EntityCourse> downloads; // 需要下载的课程信息
	private List<String> kPonitNames = new ArrayList<String>(); // 需要节点名字
	private String courseName; // 课程名字
	private PublicEntity publicEntity;  //课程详情的信息
	private CourseParentContentsAdapter parentAdapter;  //课程根目录的适配器
	private List<Boolean> parentSelect;  //选中的集合
	private List<List<Boolean>> childSelect;
	private boolean isok;  //是否购买
	private PolyvDBservice service;  //保利的服务
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_bldownload_course);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 	获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		publicEntity = (PublicEntity) intent.getSerializableExtra("publicEntity");
		isok = publicEntity.getEntity().isIsok();
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //獲取用戶的Id
	}

	@Override
	public void initView() {

		downloads = new ArrayList<EntityCourse>();  //需要下载的课程信息
		progressDialog = new ProgressDialog(this);  //加載數據顯示的dialog
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		parentSelect = new ArrayList<Boolean>();
		childSelect = new ArrayList<List<Boolean>>();
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回
		title = (TextView) findViewById(R.id.title_text);  //标题
		title.setText("下载列表");  //设置标题
		gridView = (NoScrollGridView)findViewById(R.id.gridView);  //课程根目录的布局
		mListView = (NoScrollExpandableListView) findViewById(R.id.download_expandablelist);
		mButton = (Button) findViewById(R.id.download_button);
		
		packageList = publicEntity.getEntity().getCoursePackageList();
		if(packageList!=null&&packageList.size()>0){
			courseName = packageList.get(0).getName();
			parentAdapter = new CourseParentContentsAdapter(BLDownLoadSelectActivity.this,packageList);
			gridView.setAdapter(parentAdapter);
		}
		courseKpoints = publicEntity.getEntity().getCourseKpoints();
		if(courseKpoints!=null&&courseKpoints.size()>0){
			for(int i=0;i<courseKpoints.size();i++){
				parentSelect.add(false);
				List<EntityCourse> childKpoints = courseKpoints.get(i).getChildKpoints();
				if(childKpoints!=null&&childKpoints.size()>0){
					for (int j = 0; j < childKpoints.size(); j++) {
						List<Boolean> childList = new ArrayList<Boolean>();
						childList.add(false);
						childSelect.add(childList);
					}
				}
			}
			DownloadListAdapter adapter = new DownloadListAdapter(
					BLDownLoadSelectActivity.this,
					courseKpoints,downloads);
			mListView.setAdapter(adapter);
			mListView.expandGroup(0);
		}
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回的佈局
		gridView.setOnItemClickListener(this);  //课程根目录的布局
		mListView.setOnChildClickListener(this);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回的佈局
			this.finish();
			break;
		case R.id.download_button:  //下载的点击事件
			Intent intent = new Intent();
			intent.setClass(BLDownLoadSelectActivity.this, PolyvDownloadListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.gridView:   //课程根目录的布局
			parentAdapter.setParentPosition(position);
			parentAdapter.notifyDataSetChanged();
			int parentId = packageList.get(position).getId();  //得到根目录的Id
			courseName = packageList.get(position).getName();
			courseId = publicEntity.getEntity().getCourse().getId();  //得到课程的Id
			//获取目录的方法
			getCourseDetails(courseId, userId, parentId);
			break;

		default:
			break;
		}
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-23 下午1:39:24
	 * 方法说明:获取课程详情的方法
	 */
	private void getCourseDetails(int courseId,int userId,int id) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		params.put("currentCourseId", id);
		Log.i("lala", Address.COURSE_DETAILS+"?"+params.toString());
		httpClient.post(Address.COURSE_DETAILS, params , new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try{
					publicEntity = JSON.parseObject(data, PublicEntity.class);
						if(publicEntity.isSuccess()){
							courseKpoints = publicEntity.getEntity().getCourseKpoints();
							if(courseKpoints!=null&&courseKpoints.size()>0){
								DownloadListAdapter adapter = new DownloadListAdapter(
										BLDownLoadSelectActivity.this,
										courseKpoints,downloads);
								mListView.setAdapter(adapter);
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
	/**
	 *	组的点击事件
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if (courseKpoints.get(groupPosition).getChildKpoints().size()<=0) {
			ConstantUtils.showMsg(this, groupPosition+"....");
			return true;
		}
		return false;
	}
	
	/* 
	 * 子的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		if (courseKpoints != null
				&& courseKpoints.get(groupPosition).getChildKpoints().size() > 0) {
			ImageView imageView = (ImageView) v.findViewById(R.id.chile_image);
			TextView text = (TextView) v.findViewById(R.id.download_child_text);
			EntityCourse entityCourse = courseKpoints.get(groupPosition)
					.getChildKpoints().get(childPosition);
//			// 验证播放节点的接口(验证是否可以下载)
//			verificationPlayVideo(userId, entityCourse.getId(),entityCourse,groupPosition,text);
			if (downloads.contains(entityCourse)) {
				downloads.remove(entityCourse);
				kPonitNames.remove(courseKpoints.get(groupPosition).getName());
				imageView.setBackgroundResource(R.drawable.section);
				text.setTextColor(getResources().getColor(
						R.color.text_color_normal));
			} else {
//				downloads.add(entityCourse);
//				kPonitNames.add(courseKpoints.get(groupPosition).getName());
//				text.setTextColor(getResources().getColor(R.color.Blue));
				// 验证播放节点的接口(验证是否可以下载)
				verificationPlayVideo(userId, entityCourse.getId(),entityCourse,groupPosition,imageView,text,childPosition);
			}
		}
		return false;
	}
	
	/**
	 * @author bin 修改人: 时间:2015-10-23 下午4:34:15 方法说明:验证播放节点的接口
	 * @param entityCourse 
	 * @param groupPosition 
	 * @param text 
	 */
	private void verificationPlayVideo(final int userId, final int kpointId, final EntityCourse entityCourse, final int groupPosition,
										final ImageView imageView,final TextView text,final int childPosition) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("kpointId", kpointId);
		Log.i("lala", Address.VERIFICATION_PLAY + "?" + params.toString());
		httpClient.post(Address.VERIFICATION_PLAY, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity entity = JSON.parseObject(data,
										PublicEntity.class);
								String message = entity.getMessage();
								if (entity.isSuccess()) { // 可以播放
									EntityPublic entityPublic = entity
											.getEntity();
									String fileType = entityPublic.getFileType();
									String videourl = entityPublic.getVideoUrl(); // 得到视频路径
									if (!TextUtils.isEmpty(fileType)&&!"VIDEO".equals(fileType)) {
										ConstantUtils.showMsg(BLDownLoadSelectActivity.this, "该类型暂不支持下载");
										return;
									}
									if(TextUtils.isEmpty(videourl)){
										ConstantUtils.showMsg(BLDownLoadSelectActivity.this, "无视频路径");
										return;
									}
									if (downloads.contains(entityCourse)) {
										downloads.remove(entityCourse);
										kPonitNames.remove(courseKpoints.get(groupPosition).getName());
										imageView.setBackgroundResource(R.drawable.section);
										text.setTextColor(getResources().getColor(
												R.color.text_color_normal));
									} else {
//										下载的方法
										String image = publicEntity.getEntity().getCourse().getMobileLogo();
										Log.i("lala", image+"............icon");
										downLoad(videourl,courseKpoints.get(groupPosition).getChildKpoints().get(childPosition).getName(),image);
//										downLoad("sl8da4jjbxa7c5a36521d30bc5e5514c_s","aaa",image);
										entityCourse.setVideourl(videourl);
										downloads.add(entityCourse);
										kPonitNames.add(courseKpoints.get(groupPosition).getName());
										imageView.setBackgroundResource(R.drawable.section_selected);
										text.setTextColor(getResources().getColor(R.color.Blue));
									}
								} else {
									if (isok) {
										ConstantUtils.showMsg(
												BLDownLoadSelectActivity.this,
												"该视频无法下载");
									} else {
										if(userId == 0){
											ConstantUtils.showMsg(
													BLDownLoadSelectActivity.this,
													"请登录购买后下载");
										}else{
											ConstantUtils.showMsg(
													BLDownLoadSelectActivity.this,
													"请购买后下载");
										}
									}
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-5-25 下午12:45:57
	 * 方法说明:下载
	 */
	public void downLoad(final String vid,final String title,final String img){
		Video.loadVideo(vid, new Video.OnVideoLoaded() {
			public void onloaded(final Video v) {
				if (v == null) {
					return;
				}
				// 码率数
				String[] items = BitRateEnum.getBitRateNameArray(v.getDfNum());

				// 数字2代表的是数组的下标
				final Builder selectDialog = new AlertDialog.Builder(BLDownLoadSelectActivity.this).setTitle("选择下载码率")
						.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
							
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int bitrate = which + 1;
						service = new PolyvDBservice(BLDownLoadSelectActivity.this);  //保利的服务
						final PolyvDownloadInfo downloadInfo = new PolyvDownloadInfo(vid, v.getDuration(), v.getFilesize(bitrate), bitrate,img);
						downloadInfo.setTitle(title);
						Log.i("videoAdapter", title);
						if (service != null && !service.isAdd(downloadInfo)) {
							service.addDownloadFile(downloadInfo);
							PolyvDownloader polyvDownloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);
							polyvDownloader.setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {
								private long total;
								@Override
								public void onDownloadSuccess() {
									service.updatePercent(downloadInfo, total, total);
								}
								
								@Override
								public void onDownloadFail(PolyvDownloaderErrorReason errorReason) {
									
								}
								
								@Override
								public void onDownload(long current, long total) {
									this.total=total;
								}
							});
							polyvDownloader.start();
						} else {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(BLDownLoadSelectActivity.this, "下载任务已经增加到队列", Toast.LENGTH_SHORT).show();
								}
							});
						}

						dialog.dismiss();
					}
				});

				selectDialog.show().setCanceledOnTouchOutside(true);
			}
		});
	}
}
