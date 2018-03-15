package com.yizhilu.huitianxinli;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ScreenUtil;

public class PersonalInformationActivity extends BaseActivity {

	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private ProgressDialog progressDialog; // 获取数据显示dialog
	private ImageView personal_image; // 用户的头像
	private TextView title_text, nickName, name, sex, phoneNumber, qianMing;// 标题,昵称
																			// 姓名
																			// 年龄
																			// 手机号
																			// 签名
	private List<EntityPublic> list; // 公共实体类
	private ImageLoader imageLoader; // 加载头像的对象
	private LinearLayout password, back_layout, nickNameLayout,name_layout,jianjie_layout,sex_layout,remove_binding_layout; // 密码,返回,昵称的布局,姓名,简介,性别的布局,解除绑定
	private int userId, CAMER = 0, PICHER = 1; // 用户Id,照相机,相册
	private Dialog bottom_choice_dialog; // 选择头像时弹出
	private EntityPublic userExpandDto; // 用户信息的实体
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_personal_information);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0);
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		progressDialog = new ProgressDialog(this); // 获取数据显示dialog
		imageLoader = ImageLoader.getInstance(); // 加载头像的对象
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		title_text.setText(getResources().getString(
				R.string.personal_information)); // 设置标题
		personal_image = (ImageView) findViewById(R.id.personal_image); // 用户的头像
		nickName = (TextView) findViewById(R.id.personal_tv_nickname); // 昵称
		remove_binding_layout = (LinearLayout) findViewById(R.id.remove_binding_layout);  //解除绑定
		name = (TextView) findViewById(R.id.personal_tv_name);// 姓名
		sex = (TextView) findViewById(R.id.personal_sex);// 性别
		phoneNumber = (TextView) findViewById(R.id.personal_tv_phoneNumber);// 手机号
		qianMing = (TextView) findViewById(R.id.personal_tv_qianMing); // 签名
		password = (LinearLayout) findViewById(R.id.change_password); // 密码
		nickNameLayout = (LinearLayout) findViewById(R.id.nickNameLayout); // 昵称的布局
		name_layout = (LinearLayout) findViewById(R.id.name_layout);  //姓名的布局
		jianjie_layout = (LinearLayout) findViewById(R.id.jianjie_layout);  //简介
		sex_layout = (LinearLayout) findViewById(R.id.sex_layout);  //性别的布局
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 联网获取个人信息的方法
		getMy_Message(userId); // 个人信息
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回
		password.setOnClickListener(this);
		personal_image.setOnClickListener(this); // 头像
		nickNameLayout.setOnClickListener(this); // 昵称的布局
		name_layout.setOnClickListener(this);  //姓名的布局
		jianjie_layout.setOnClickListener(this);  //简介
		sex_layout.setOnClickListener(this);  //性别的布局
		remove_binding_layout.setOnClickListener(this);  //解除绑定
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-19 下午2:10:21 方法说明:获取个人信息的类
	 */
	private void getMy_Message(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("lala", Address.MY_MESSAGE+"?"+params.toString());
		httpClient.post(Address.MY_MESSAGE, params,
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
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								if (publicEntity.isSuccess()) {
									userExpandDto = publicEntity.getEntity()
											.getUserExpandDto();
									imageLoader.displayImage(Address.IMAGE_NET
											+ userExpandDto.getAvatar(),
											personal_image,
											HttpUtils.getUserHeadDisPlay());
									nickName.setText(userExpandDto
											.getShowname());
									name.setText(userExpandDto.getRealname());
									if (userExpandDto.getGender() == 0) {
										sex.setText("男");
									} else {
										sex.setText("女");
									}
									phoneNumber.setText(userExpandDto
											.getMobile());
									qianMing.setText(userExpandDto
											.getUserInfo());
								}else{
									ConstantUtils.showMsg(PersonalInformationActivity.this, message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
						ConstantUtils.showMsg(PersonalInformationActivity.this, "获取数据失败,请检查网络设置!");
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;
		case R.id.change_password: // 密码
			intent.setClass(PersonalInformationActivity.this,
					ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.personal_image: // 修改头像
			// 弹出修改头像的方法
			showBottomAlertDialog();
			break;
		case R.id.nickNameLayout: // 昵称
			if(userExpandDto == null){
				ConstantUtils.showMsg(PersonalInformationActivity.this, "没有获取到用户信息");
				return;
			}
			intent.setClass(PersonalInformationActivity.this,
					AmendUserMessageActivity.class);
			intent.putExtra("title", "修改昵称");
			intent.putExtra("message", userExpandDto.getShowname());
			startActivity(intent);
			break;
		case R.id.name_layout:  //姓名
			if(userExpandDto == null){
				ConstantUtils.showMsg(PersonalInformationActivity.this, "没有获取到用户信息");
				return;
			}
			intent.setClass(PersonalInformationActivity.this,
					AmendUserMessageActivity.class);
			intent.putExtra("title", "修改姓名");
			intent.putExtra("message", userExpandDto.getRealname());
			startActivity(intent);
			break;
		case R.id.jianjie_layout:  //简介
			if(userExpandDto == null){
				ConstantUtils.showMsg(PersonalInformationActivity.this, "没有获取到用户信息");
				return;
			}
			intent.setClass(PersonalInformationActivity.this,
					AmendUserMessageActivity.class);
			intent.putExtra("title", "个性签名");
			intent.putExtra("message", userExpandDto
					.getUserInfo());
			startActivity(intent);
			break;
		case R.id.sex_layout:  //性别的布局
			if(userExpandDto == null){
				ConstantUtils.showMsg(PersonalInformationActivity.this, "没有获取到用户信息");
				return;
			}
			intent.setClass(PersonalInformationActivity.this,
					AmendUserMessageActivity.class);
			intent.putExtra("title", "选择性别");
			intent.putExtra("sex", userExpandDto.getGender());
			startActivity(intent);
			break;
		case R.id.remove_binding_layout:  //第三方账户绑定
			if(userExpandDto == null){
				ConstantUtils.showMsg(PersonalInformationActivity.this, "没有获取到用户信息");
				return;
			}
			intent.setClass(PersonalInformationActivity.this, RemoveBindingActivity.class);
			intent.putExtra("EntityPublic", userExpandDto);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	Handler ishandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String imageUrl = (String) msg.obj;
				Log.i("lala", imageUrl);
				// 上传成功修改头像的方法
				modifyIcon(userId, imageUrl);
			} else {
				ConstantUtils.showMsg(PersonalInformationActivity.this,
						"上传头像错误！");
			}
		}
	};

	/**
	 * @author bin 修改人: 时间:2015-9-30 上午9:25:05 方法说明:修改头像的方法
	 */
	private void modifyIcon(final int userId, String path) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("avatar", path);
		Log.i("lala", Address.UPDATE_HEAD+"?"+params.toString());
		httpClient.post(Address.UPDATE_HEAD, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						if (!TextUtils.isEmpty(data)) {
							PublicEntity allEntity = JSON.parseObject(data,
									PublicEntity.class);
							String message = allEntity.getMessage();
							ConstantUtils.showMsg(
									PersonalInformationActivity.this, message);
							// 联网获取个人信息的方法
							getMy_Message(userId); // 个人信息
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	/**
	 * @author bin 修改人: 时间:2015-10-29 下午1:31:12 方法说明:弹出选择相册还是照相机
	 */
	private void showBottomAlertDialog() {
		if (bottom_choice_dialog != null && bottom_choice_dialog.isShowing()) {
			bottom_choice_dialog.dismiss();
			bottom_choice_dialog = null;
		}
		bottom_choice_dialog = new Dialog(PersonalInformationActivity.this,
				R.style.custom_dialog);
		bottom_choice_dialog.show();
		WindowManager.LayoutParams lp = bottom_choice_dialog.getWindow()
				.getAttributes();
		lp.width = ScreenUtil.getInstance(PersonalInformationActivity.this)
				.getScreenWidth();// 定义宽度
		bottom_choice_dialog.getWindow().setAttributes(lp);
		bottom_choice_dialog.getWindow().setGravity(Gravity.CENTER);
		bottom_choice_dialog.setContentView(R.layout.alertdialog_bottom_menu);

		Button btn_take_photo = (Button) bottom_choice_dialog
				.findViewById(R.id.btn_takephto);
		btn_take_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMER);
				bottom_choice_dialog.dismiss();
			}
		});
		Button btn_file_selected = (Button) bottom_choice_dialog
				.findViewById(R.id.btn_file_select);
		btn_file_selected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("image/*");
//				// 根据版本号不同使用不同的Action
//				if (Build.VERSION.SDK_INT < 19) {
//					intent.setAction(Intent.ACTION_GET_CONTENT);
//				} else {
//					intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//				}
//				startActivityForResult(intent, PICHER);
//				bottom_choice_dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
				startActivityForResult(intent, PICHER);

				bottom_choice_dialog.dismiss();
			}
		});
	}

	/**
	 * 选择完头像调用的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == PICHER) {
				if (data != null) {
					String pathurl = getPath(data);
					// 上传头像的方法
					uploadIcon(pathurl);
//					Bitmap loacalBitmap = getLoacalBitmap(pathurl);
//					@SuppressWarnings("static-access")
//					Bitmap sBitmap = loacalBitmap.createScaledBitmap(
//							loacalBitmap, 150, 150, true);
//					// corner = ScreenUtil.toRoundCorner(sBitmap, 100);
//					personal_image.setImageBitmap(loacalBitmap);
				}
			} else {
				if (!Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					return;
				}
				@SuppressWarnings("unused")
				DateFormat dateFormat = new DateFormat();
				String name = DateFormat.format("yyyy-MM-dd hhmmss",
						Calendar.getInstance(Locale.CHINA))
						+ ".jpg";
				Bundle bundle = data.getExtras();
				// 获取相机返回的数据，并转换为图片格式
				Bitmap bitmap = (Bitmap) bundle.get("data");
//				@SuppressWarnings("static-access")
//				Bitmap bitmaps = bitmap.createScaledBitmap(bitmap, 150, 150,
//						true);
//				Bitmap roundCorner = ScreenUtil.toRoundCorner(bitmaps, 100);
//				personal_image.setImageBitmap(bitmap);
				FileOutputStream fout = null;
				File file = new File("/sdcard/pintu");
				String filename = file.getPath() + "/" + name;
				file.mkdirs();
				try {
					fout = new FileOutputStream(filename);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
					uploadIcon(filename);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						fout.flush();
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午7:39:20 方法说明:返回选择相册图片url
	 */
	public String getPath(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumns = { MediaStore.Images.Media.DATA };
		Cursor c = getContentResolver().query(selectedImage, filePathColumns,
				null, null, null);
		c.moveToFirst();
		int columnIndex = c.getColumnIndex(filePathColumns[0]);
		String picturePath = c.getString(columnIndex);
		c.close();
		return picturePath;
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午7:45:14 方法说明: 图片上传
	 */
	public void uploadIcon(final String path) {
//		HttpUtils.showProgressDialog(progressDialog);
//		final Map<String, String> map = new HashMap<String, String>();
//		map.put("base", "mavendemo");
//		map.put("param", "appavatar");
//		new Thread() {
//			@Override
//			public void run() {
//				String backPath = doPost(Address.UP_IMAGE_NET, map, new File(
//						path));
//				HttpUtils.exitProgressDialog(progressDialog);
//				Message message = new Message();
//				if (!TextUtils.isEmpty(backPath)) {
//					Looper.prepare();
//					message.what = 0;
//					message.obj = backPath;
//					ishandler.sendMessage(message);
//					Looper.loop();
//				} else {
//					ishandler.obtainMessage(1).sendToTarget();
//					Looper.prepare();
//					Looper.loop();
//				}
//			}
//		}.start();
		File file = new File(path);
		RequestParams params = new RequestParams();
		params.put("base", "mavendemo");
		params.put("param", "appavatar");
		try {
			params.put("fileupload", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		httpClient.post(Address.UP_IMAGE_NET, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					// 上传成功修改头像的方法
					modifyIcon(userId, data);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private String doPost(String url, Map<String, String> param, File file) {
		HttpPost post = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		@SuppressWarnings("deprecation")
		MultipartEntity entity = new MultipartEntity();
		StringBuffer sb = new StringBuffer();
		try {
			if (param != null && !param.isEmpty()) {
				for (Map.Entry<String, String> entry : param.entrySet()) {
					entity.addPart(entry.getKey(),
							new StringBody(entry.getValue()));
				}
			}
			if (file != null && file.exists()) {
				entity.addPart("fileupload", new FileBody(file));
			}
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK) {
				HttpEntity result = response.getEntity();
				if (result != null) {
					InputStream is = result.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String tempLine;
					while ((tempLine = br.readLine()) != null) {
						sb.append(tempLine);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		post.abort();
		return sb.toString();
	}
}
