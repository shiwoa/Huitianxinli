package com.yizhilu.fragment;

import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CatalogueExpandableAdapter;
import com.yizhilu.adapter.CourseParentContentsAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollExpandableListView;
import com.yizhilu.view.NoScrollGridView;

public class CourseSectionFragment extends BaseFragment {
	private static CourseSectionFragment courseSectionFragment; //课程章节的对象
	private View inflate;  //课程章节的布局
	private NoScrollGridView gridView;  //课程根目录的布局
	private PublicEntity publicEntity;  //课程详情的实体
	private List<EntityCourse> packageList;  //课程父目录的集合
	private NoScrollExpandableListView expandableListView;  //目录的列表
	private List<EntityCourse> courseKpoints;  //子目录的集合
	private ProgressDialog progressDialog;  //联网获取数据的方法
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private int courseId,userId;  //课程Id,用户Id
	private CourseParentContentsAdapter parentAdapter;  //课程根目录的适配器
	private CatalogueExpandableAdapter expandableAdapter;  //二级列表
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_course_section, container,false);
		Bundle bundle = getArguments();
		publicEntity = (PublicEntity) bundle.getSerializable("publicEntity");
		return inflate;
	}
	
	public static CourseSectionFragment getInstence(){
		if(courseSectionFragment == null){
			courseSectionFragment = new CourseSectionFragment();
		}
		return courseSectionFragment;
	}

	@Override
	public void initView() {
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);  //得到用户的Id
		progressDialog = new ProgressDialog(getActivity());  //联网获取数据的方法
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		gridView = (NoScrollGridView) inflate.findViewById(R.id.gridView);  //课程根目录的布局
		expandableListView = (NoScrollExpandableListView) inflate.findViewById(R.id.expandableListView);  //目录的列表
		
		packageList = publicEntity.getEntity().getCoursePackageList();
		if(packageList!=null&&packageList.size()>0){
			parentAdapter = new CourseParentContentsAdapter(getActivity(),packageList);
			gridView.setAdapter(parentAdapter);
		}
		courseKpoints = publicEntity.getEntity().getCourseKpoints();
		if(courseKpoints!=null&&courseKpoints.size()>0){
			expandableAdapter = new CatalogueExpandableAdapter(getActivity(), courseKpoints);
			expandableListView.setAdapter(expandableAdapter);
			expandableListView.expandGroup(0);
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
								expandableAdapter = new CatalogueExpandableAdapter(getActivity(), courseKpoints);
								expandableListView.setAdapter(expandableAdapter);
								expandableListView.expandGroup(0);
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
 
	@Override
	public void addOnClick() {
		gridView.setOnItemClickListener(this);  //课程根目录的布局
		expandableListView.setOnGroupClickListener(this);
		expandableListView.setOnChildClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.gridView:   //课程根目录的布局
			parentAdapter.setParentPosition(position);
			parentAdapter.notifyDataSetChanged();
			int parentId = packageList.get(position).getId();  //得到根目录的Id
			courseId = publicEntity.getEntity().getCourse().getId();  //得到课程的Id
			//获取目录的方法
			getCourseDetails(courseId, userId, parentId);
			break;

		default:
			break;
		}
	}
	
	/**
	 *	组的点击事件
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		if (courseKpoints.get(groupPosition).getType() == 0) {
			EntityCourse entityCourse = courseKpoints.get(groupPosition);
			expandableAdapter.setSelectEntity(entityCourse);
			expandableAdapter.notifyDataSetChanged();
//			String videoUrl = entityCourse.getVideourl();
			int kpointId = entityCourse.getId();
//			int isfree = entityCourse.getIsfree();
			Intent intent = new Intent("playVideo");
//			intent.putExtra("videoUrl", videoUrl);
			intent.putExtra("kpointId", kpointId);
//			intent.putExtra("isfree", isfree);
			getActivity().sendBroadcast(intent);   //发向课程详情和讨论区
		}
		return false;
	}
	
	/**
	 *	子的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		EntityCourse entityCourse = publicEntity.getEntity().getCourseKpoints().get(groupPosition).getChildKpoints().get(childPosition);
		expandableAdapter.setSelectEntity(entityCourse);
		expandableAdapter.notifyDataSetChanged();
//		String videoUrl = entityCourse.getVideourl();
		int kpointId = entityCourse.getId();
//		int isfree = entityCourse.getIsfree();
		Intent intent = new Intent("playVideo");
//		intent.putExtra("videoUrl", videoUrl);
		intent.putExtra("kpointId", kpointId);
//		intent.putExtra("isfree", isfree);
		getActivity().sendBroadcast(intent);   //发向课程详情和讨论区
		return false;
	}
}
