package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;


public class JieKouActivity extends BaseActivity {
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private ProgressDialog progressDialog;  //联网获取数据显示的dialog
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_jiekou);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		progressDialog = new ProgressDialog(this); //联网获取数据显示的dialog
	}

	@Override
	public void addOnClick() {

	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-12 下午6:24:33
	 * 方法说明: 连网获取我购买的课程的方法
	 */
	private void getMyBuyCourse(int userId){	
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		httpClient.post(Address.MY_BUY_COURSE, params, new TextHttpResponseHandler() {
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
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午9:26:58
	 * 方法说明:连网获取修改个人信息数据
	 */
	private void getUpdateMymessage(int id,String nickname,String mobile,String gender,String realname,String userInfo){
		 
		 RequestParams params=new RequestParams();
		 params.put("queryUser.id", id);
		 params.put("queryUser.nickname",nickname);
		 params.put("queryUser.mobile", mobile);
		 params.put("queryUser.gender", gender);
		 params.put("queryUser.realname", realname);
		 params.put("queryUser.userInfo", userInfo);
		httpClient.post(Address.UPDATE_MYMESSAGE, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	 }
	
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午9:36:19
	 * 方法说明:连网获取我的订单数据
	 */
	private void getMyOrderList(int currentPage,int userId,String trxStatus){
		RequestParams params =new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("queryTrxorder.userId", userId);
		params.put("queryTrxorder.trxStatus", trxStatus);
		httpClient.post(Address.MY_ORDER_LIST, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
							PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
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
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午9:47:33
	 * 方法说明:连接网络获取创建订单数据
	 */
	private void getCreateOrder(int userId,int courseId,String payType){
		RequestParams params=new RequestParams();
		params.put("userId", userId);
		params.put("courseId", courseId);
		params.put("payType", payType);
		httpClient.post(Address.CREATE_ORDER, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午9:55:46
	 * 方法说明:连接网络获取支付前检测数据
	 */
	private void getPaymentDetection(int userId,int orderId,String payType){
		RequestParams params =new RequestParams();
		params.put("userId", userId);
		params.put("orderId", orderId);
		params.put("payType", payType);
		httpClient.post(Address.PAYMENT_DETECTION, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午10:09:40
	 * 方法说明:连接网络获取支付成功回调的数据
	 */
	private void getPaysuccessCall(int userId,String totalFee,String outTradeNo,String payType,String orderNo){
		RequestParams params=new RequestParams();
		params.put("userId", userId);
		params.put("totalFee", totalFee);
		params.put("outTradeNo", outTradeNo);
		params.put("payType", payType);
		params.put("orderNo", orderNo);
		httpClient.post(Address.PAYSUCCESS_CALL, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午9:05:09
	 * 方法说明:修改密码的方法
	 */
	private void getUpdate_Password(int userId,String newpwd,String oldpwd){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("newpwd", newpwd);
		params.put("oldpwd", oldpwd);
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
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
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
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午9:56:50
	 * 方法说明:个人资料的方法
	 */
	public void getMy_Message(int userId){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		httpClient.post(Address.MY_MESSAGE, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
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
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午10:05:52
	 * 方法说明:修改头像的方法
	 */
	private void getUpdate_Head(int userId,String avatar) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("avatar", avatar);
		httpClient.post(Address.UPDATE_HEAD, params, new TextHttpResponseHandler() {
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
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午10:17:11
	 * 方法说明:账户信息
	 */
	private void getUser_Message(int userId,String currentPage){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		httpClient.post(Address.USER_MESSAGE, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						CourseEntity parseObject = JSON.parseObject(data, CourseEntity.class);
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
	 * @author ming
	 * 修改人:
	 * 时间:2015年10月13日 上午9:35:06
	 * 类说明:获取讲师列表的方法
	 */
	private void getTeacherList(int currentPage,String name,int isStar){
		RequestParams params=new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("teacher.name", name);
		params.put("teacher.isStar", isStar);
		httpClient.post(Address.TEACHER_LIST, params, new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.showProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					HttpUtils.showProgressDialog(progressDialog);
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
						
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
						HttpUtils.showProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author ming
	 * 修改人:
	 * 时间:2015年10月13日 上午9:49:08
	 * 类说明:获取讲师详情的方法
	 */
	private void getTeacherInfo(long teacherId){
		RequestParams params=new RequestParams();
		params.put("teacherId", teacherId);
		httpClient.post(Address.TEACHER_DETAILS, params, new TextHttpResponseHandler() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.showProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					HttpUtils.showProgressDialog(progressDialog);
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.showProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午10:57:51
	 * 方法说明:课程评论列表的方法
	 */
	private void getCourse_Comment_List(int courseId,String currentPage){
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("page.currentPage", currentPage);
		httpClient.post(Address.COURSE_COLLECT_LIST, params, new TextHttpResponseHandler() {
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
	 * @author bishuang
	 * 修改人:
	 * 时间:2015-10-13 上午11:06:58
	 * 方法说明:添加课程评论的方法
	 */
	private void getAdd_Course_Comment(int userId,String content,String kpointId,String courseId){
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("content", content);
		params.put("kpointId", kpointId);
		params.put("courseId", courseId);
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
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午10:44:23
	 * 方法说明:连接网络获取帮助反馈的数据
	 */
	private void getHelpFeedback(int userId,String contact,String userContent){
		RequestParams params=new RequestParams();
		params.put("userFeedback.userId", userId);
		params.put("contact", contact);
		params.put("userFeedback.content", userContent);
		httpClient.post(Address.HELP_FEEDBACK, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午10:50:43
	 * 方法说明:连接网络获取支付宝信息的方法
	 */
	private void getAlipayInfo(){
		httpClient.get(Address.ALIPAY_INFO, new TextHttpResponseHandler() {
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
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
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
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午10:52:47
	 * 方法说明:连接网络获取课程收藏列表的方法
	 */
	private void getCourseCollect_list(int currentPage,int userId){
		RequestParams params=new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("userId", userId);
		httpClient.post(Address.COURSE_COLLECT_LIST, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午11:09:02
	 * 方法说明:连接网络获取添加课程收藏的方法
	 */
	private void getAddCourseCollect(int courseId,int userId){
		RequestParams params =new RequestParams();
		params.put("courseId", courseId);
		params.put("userId", userId);
		httpClient.post(Address.ADD_COURSE_COLLECT, params,new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午11:15:31
	 * 方法说明:连接网络获取删除课程收藏的方法
	 */
	private void getDeleteCourseCollect(String ids){
		RequestParams params =new RequestParams();
		params.put("ids", ids);
		httpClient.post(Address.DELETE_COURSE_COLLECT, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午11:21:36
	 * 方法说明:连接网络获取课程播放记录列表的方法
	 */
	private void getCoursePlayRecordList(int currentPage,int userId){
		RequestParams params=new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("userId", userId);
		httpClient.post(Address.COURSE_PLAY_RECORD_LIST, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
	/**
	 * @author 刘常启
	 * 修改人:
	 * 时间:2015-10-13 上午11:30:07
	 * 方法说明:连接网络获取删除课程播放记录的方法
	 */
	private void getDeleteCoursePlayRecord(String ids){
		RequestParams params=new RequestParams();
		params.put("ids", ids);
		httpClient.post(Address.DELETE_COURSE_PLAY_RECORD, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}
}
