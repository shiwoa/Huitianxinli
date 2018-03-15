package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CourseDiscussAdapater;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bishuang
 * 修改人:
 * 时间:2015-10-23 上午10:11:52
 * 类说明:课程讨论的类
 */ 
public class CourseDiscussFragment extends BaseFragment {
	private static CourseDiscussFragment courseDiscussFragment;  //讨论区的对象
	private View inflate;
	private PullToRefreshScrollView refreshScrollView;
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private ProgressDialog progressDialog;  //加载数据显示的dialog
	private List<EntityPublic> entityPublics; //课程评论列表的实体
	private NoScrollListView discuss_listView; //讨论列表
	private CourseDiscussAdapater adapter; //讲师的适配器
	private int page = 1,isfree;// 初始的页数
	private EditText setEdit; //评论文本
	private int userId;  //用户Id
	private PublicEntity publicEntity;  //课程详情的实体
	private int kpointId,courseId;  //课程节点的Id,课程Id
	private TextView send_message;  //发送按钮
	private String videoUrl;  //视频路径
	private BroadcastReceiver receiver;  //广播的类
	private LinearLayout no_discuss_layout;  //没有评论的布局
	
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_course_discuss, container,false);
		publicEntity = (PublicEntity) getArguments().getSerializable("publicEntity");  //得到课程详情的实体
		return inflate;
	}
	
	public static CourseDiscussFragment getInstence(){
		if(courseDiscussFragment == null){
			courseDiscussFragment = new CourseDiscussFragment();
		}
		return courseDiscussFragment;
	}

	@Override
	public void initView() {
		courseId = publicEntity.getEntity().getCourse().getId();  // 课程Id
		kpointId = publicEntity.getEntity().getDefaultKpointId();  //得到课程节点的Id
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		progressDialog =new ProgressDialog(getActivity()); //联网获取数据显示的dialog
		discuss_listView = (NoScrollListView) inflate.findViewById(R.id.discuss_listView); //讨论列表
		entityPublics = new ArrayList<EntityPublic>(); //课程评论列表的实体
		setEdit = (EditText) inflate.findViewById(R.id.discuss_setEdit); //评论文本
		send_message = (TextView) inflate.findViewById(R.id.send_message);  //发送的按钮
		no_discuss_layout = (LinearLayout) inflate.findViewById(R.id.no_discuss_layout);  //没有评论的布局
//		setEdit.setInputType(InputType.TYPE_CLASS_TEXT);
//		setEdit.setImeOptions(EditorInfo.IME_ACTION_SEND);
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView);
		refreshScrollView.setMode(Mode.DISABLED);  //設置加載模式
		//联网获取讨论列表的方法
		getCourse_Comment_List(courseId,page);
	}
	@Override
	public void onResume() {
		super.onResume();
		if(receiver == null){
			receiver = new BroadCastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("playVideo");
			getActivity().registerReceiver(receiver, filter );
		}
	}
	
	@Override
	public void addOnClick() { 
		setEdit.setOnFocusChangeListener(this);  //editText获取焦点
		send_message.setOnClickListener(this);  //发送的按钮
		refreshScrollView.setOnRefreshListener(this);
//		setEdit.setOnEditorActionListener(new OnEditorActionListener() {
//			
//			@Override 
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEND)     
//                {     
//					String content = setEdit.getText().toString();
//					if (!TextUtils.isEmpty(content)) {
//						userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
//						getAdd_Course_Comment(userId, courseId,kpointId,content);
//					}else {
//						Toast.makeText(getActivity(), "请输入评论内容",Toast.LENGTH_SHORT).show();
//					}
//					
//                }  
//				return false;
//				
//			}
//		});
	}
	
	
	
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-23 上午11:25:12
	 * 方法说明:添加课程评论的方法
	 */
	private void getAdd_Course_Comment(int userId,final int courseId,int kpointId,String content){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("courseAssess.courseId", courseId);
		params.put("courseAssess.kpointId", kpointId);
		params.put("courseAssess.content", content);
		Log.i("lala", Address.ADD_COURSE_COMMENT+params.toString()+".....");
		httpClient.post(Address.ADD_COURSE_COMMENT, params, new TextHttpResponseHandler() {
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
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						String message = parseObject.getMessage();
						boolean success = parseObject.isSuccess();
						if (success) {
							entityPublics.clear();
							getCourse_Comment_List(courseId,page);
//							HideKeyboard(setEdit); 
							Toast.makeText(getActivity(), "评论发表成功！",Toast.LENGTH_SHORT).show();
						}else {
							ConstantUtils.showMsg(getActivity(), message);
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
	
//	虚拟键盘消失的方法
    public static void HideKeyboard(EditText v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );     
      if ( imm.isActive( ) ) {     
          imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );   
          
      }    
    }
	
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-22 下午6:33:42
	 * 方法说明:	获取课程评论列表
	 */
	private void getCourse_Comment_List(int courseId,int currentPage){
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("page.currentPage", currentPage);
		Log.i("lala", Address.COURSE_COMMENT_LIST+params.toString());
		httpClient.post(Address.COURSE_COMMENT_LIST, params, new TextHttpResponseHandler() {
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
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						String message = parseObject.getMessage();
						if (parseObject.isSuccess()) { 
							List<EntityPublic> assessList = parseObject.getEntity().getAssessList();
							if(assessList == null || assessList.size()<=0){
								no_discuss_layout.setVisibility(View.VISIBLE);
							}else{
								no_discuss_layout.setVisibility(View.GONE);
								for (int i = 0; i < assessList.size(); i++) {
									entityPublics.add(assessList.get(i));
								}
								adapter = new CourseDiscussAdapater(getActivity(), entityPublics);
								discuss_listView.setAdapter(adapter);
							}
						}else {
							ConstantUtils.showMsg(getActivity(), message);
						}
						refreshScrollView.onRefreshComplete();
					} catch (Exception e) {
						refreshScrollView.onRefreshComplete();
						HttpUtils.exitProgressDialog(progressDialog);
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				refreshScrollView.onRefreshComplete();
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	
	/**
	 *  EditText获取焦点的监听
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		super.onFocusChange(v, hasFocus);
		if(hasFocus){
			Log.i("lala", "获取焦点。。。。。。");
		}else{
			Log.i("lala", "失去。。。。。。");
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.send_message:  //发送的按钮
			userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
			String string = setEdit.getText().toString();
			setEdit.setText("");
			if(!TextUtils.isEmpty(string)){
				getAdd_Course_Comment(userId, courseId, kpointId, string);
			}else{
				ConstantUtils.showMsg(getActivity(), "请输入内容");
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-24 下午2:30:47
	 * 类说明:广播
	 */
	class BroadCastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if("playVideo".equals(action)){  //点击目录传过来的
				videoUrl = intent.getStringExtra("videoUrl");
				kpointId = intent.getIntExtra("kpointId", 0);
				isfree = intent.getIntExtra("isfree", 0);
				entityPublics.clear();
				page = 1;
				getCourse_Comment_List(courseId,page);
			}
		}
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if(receiver!=null){
				getActivity().unregisterReceiver(receiver);
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		page = 1;
		entityPublics.clear();
		getCourse_Comment_List(courseId,page);
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		page++;
		getCourse_Comment_List(courseId,page);
	}
	
}
