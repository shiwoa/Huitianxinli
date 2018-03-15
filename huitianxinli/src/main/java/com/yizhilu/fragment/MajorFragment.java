package com.yizhilu.fragment;

import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.SubjectAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-20 下午2:41:21
 * 类说明:专业的类
 */
public class MajorFragment extends BaseFragment {
	public interface LoadCourseListenner{
		public void setCourseMessage(int subjectId,int teacherId,int sortId);
	}
	private View inflate;  //专业的总布局
	private static MajorFragment majorFragment;  //专业的对象
	private LoadCourseListenner loadCourseListenner;  //接口的对象
	private ListView stair_major;  //一级专业,二级专业
	private int parentId,subjectId;  //
	private ProgressDialog progressDialog;  //加载数据显示的dialog
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private SubjectAdapter subjectAdapter;  //一级专业的适配器
	private List<EntityCourse> subjectList;  //专业的集合
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		loadCourseListenner = (LoadCourseListenner)activity;
	}
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_major, container,false);
		return inflate;
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-20 下午2:49:39
	 * 方法说明:返回本类对象的方法
	 */
	public static MajorFragment getInstence(){
		if(majorFragment == null){
			majorFragment = new MajorFragment();
		}
		return majorFragment;
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(getActivity()); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		stair_major = (ListView) inflate.findViewById(R.id.stair_major);  //一级专业
		
		//联网获取专业列表的方法
		getSubjectData(parentId);
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-20 下午3:12:44
	 * 方法说明:获取专业列表数据的方法
	 */
	private void getSubjectData(int parentId) {
		RequestParams params = new RequestParams();
		params.put("parentId", parentId);
		httpClient.post(Address.MAJOR_LIST, params , new TextHttpResponseHandler() {

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
						CourseEntity courseEntity = JSON.parseObject(data, CourseEntity.class);
						subjectList = courseEntity.getEntity();
						if(subjectList!=null&&subjectList.size()>0){
							subjectAdapter = new SubjectAdapter(getActivity(), subjectList);
							stair_major.setAdapter(subjectAdapter);
						}
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
			}
		});
	}

	@Override
	public void addOnClick() {
		stair_major.setOnItemClickListener(this);  //一级专业的事件
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.stair_major:  //一级专业
			if(position == 0){
				subjectId = 0;
			}else{
				subjectId = subjectList.get(position-1).getSubjectId();
			}
			loadCourseListenner.setCourseMessage(subjectId, 0, 0);
			break;
		default:
			break;
		}
	}
}
