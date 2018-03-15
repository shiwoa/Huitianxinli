package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.mob.tools.utils.UIHandler;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.utils.Address;

public class ShareDialog extends Dialog implements
		android.view.View.OnClickListener, OnItemClickListener,
		PlatformActionListener, Callback {

	private Context context;
	private String texts[] = null;
	private int images[] = null;
	private GridView gridView;
	private ImageView cancel; // 取消
	private ArrayList<HashMap<String, Object>> lstImageItem;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private String shareUrl; // 分享的地址
	private EntityCourse course; // 课程的实体
	private EntityPublic topicCourse;
	private boolean isCourse, isInformation, isAbout, isTopic; // 课程,资讯,关于我们

	public ShareDialog(Context context) {
		super(context);
	}

	public ShareDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share);
		// 初始化控件的方法
		initView();
		// 添加点击事件的方法
		addOnClick();
	}

	public void setEntityCourse(EntityCourse course, boolean isCourse,
			boolean isInformation, boolean isAbout) {
		this.course = course;
		this.isCourse = isCourse;
		this.isInformation = isInformation;
		this.isAbout = isAbout;
	}

	public void setTopicCourse(EntityPublic course, boolean isCourse,
			boolean isInformation, boolean isAbout, boolean isTopic) {
		this.topicCourse = course;
		this.isCourse = isCourse;
		this.isInformation = isInformation;
		this.isAbout = isAbout;
		this.isTopic = isTopic;
	}

	/**
	 * @author bin 修改人: 时间:2016-2-19 下午5:38:00 方法说明:初始化控件的方法
	 */
	public void initView() {
		ShareSDK.initSDK(context);
		texts = new String[] { "QQ好友", "QQ空间", "微信好友", "微信朋友圈", "新浪微博" };
		images = new int[] { R.drawable.share_qq, R.drawable.share_qqzone,
				R.drawable.share_weixin, R.drawable.share_weixinp,
				R.drawable.share_xinweibo };
		gridView = (GridView) findViewById(R.id.gridview);
		cancel = (ImageView) findViewById(R.id.cancel); // 取消
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", texts[i]);
			map.put("itemImage", images[i]);
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,
				R.layout.item_share, new String[] { "itemImage", "itemText" },
				new int[] { R.id.itemImage, R.id.itemText });

		gridView.setAdapter(saImageItems);
	}

	/**
	 * @author bin 修改人: 时间:2016-2-19 下午5:38:11 方法说明:添加点击事件的方法
	 */
	public void addOnClick() {
		cancel.setOnClickListener(this); // 取消
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel: // 取消
			this.cancel();
			break;

		default:
			break;
		}
	}

	// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
	// oks.setNotification(R.drawable.ic_launcher,
	// getString(R.string.app_name));
	// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	// oks.setTitle(course.getName());
	// // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	// oks.setTitleUrl(Address.COURSE_SHARE + course.getId());
	// // text是分享文本，所有平台都需要这个字段
	// oks.setText(course.getName() + "\n" + Address.COURSE_SHARE
	// + course.getId());
	// // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	// oks.setImagePath(Address.IMAGE_NET + course.getMobileLogo());//
	// 确保SDcard下面存在此张图片
	// // url仅在微信（包括好友和朋友圈）中使用
	// oks.setUrl(Address.COURSE_SHARE + course.getId());
	// // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	// oks.setComment(course.getName());
	// // site是分享此内容的网站名称，仅在QQ空间使用
	// oks.setSite(course.getName());
	// // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	// oks.setSiteUrl(Address.IMAGE_NET + course.getMobileLogo());
	// oks.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
	// // 启动分享GUI
	// oks.show(this);

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ShareParams sp = new ShareParams();
		switch (position) {
		case 0: // qq好友
			if (isCourse) {
				sp.setTitle(course.getName()); // 设置标题
				sp.setTitleUrl(Address.COURSE_SHARE + course.getId()); // 标题的超链接
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
			} else if (isInformation) {
				sp.setTitle(course.getTitle()); // 设置标题
				sp.setTitleUrl(Address.INFORMATION_SHARE + course.getId()); // 标题的超链接
				sp.setText(Address.COURSE_SHARE + course.getId());
				if (course.getPicture() != null) {
					// sp.setImagePath(Address.IMAGE_NET + course.getPicture());
					sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
				} else {
					sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
					sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				}
			} else if (isAbout) {
				sp.setTitle(context.getResources().getString(R.string.app_name)); // 设置标题
				sp.setTitleUrl(Address.ABOUT_SHARE); // 标题的超链接
				sp.setText(context.getResources().getString(R.string.app_name)
						+ "软件\n" + Address.ABOUT_SHARE);
				sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isTopic) {
				sp.setTitle(topicCourse.getTitle()); // 设置标题
				Log.i("lala", com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId()
						+ "-----社区------");
				sp.setTitleUrl(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId()); // 标题的超链接
				sp.setText(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId());
				sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			}
			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			qq.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qq.share(sp);
			ShareDialog.this.cancel();
			break;
		case 1: // qq空间
			// ShareParams sp = new ShareParams();
			Log.i("wulala", "课程" + isCourse + "消息" + isInformation + "关于"
					+ isAbout);
			if (isCourse) {
				sp.setTitle(course.getName()); // 设置标题
				sp.setTitleUrl(Address.COURSE_SHARE + course.getId()); // 标题的超链接
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
				sp.setSite(course.getName());
				sp.setSiteUrl(Address.IMAGE_NET + course.getMobileLogo());

			} else if (isInformation) {
				sp.setTitle(course.getTitle()); // 设置标题
				sp.setTitleUrl(Address.INFORMATION_SHARE + course.getId()); // 标题的超链接
				sp.setText(Address.INFORMATION_SHARE + course.getId());
				sp.setSite(course.getTitle());
				Log.i("wulala", Address.INFORMATION_SHARE + course.getId());
				if (course.getPicture() != null) {
					sp.setImagePath(Address.IMAGE_NET + course.getPicture());
					sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
				} else {
					sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
					sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				}
			} else if (isAbout) {
				sp.setTitle(context.getResources().getString(R.string.app_name)); // 设置标题
				sp.setTitleUrl(Address.ABOUT_SHARE); // 标题的超链接
				sp.setText(context.getResources().getString(R.string.app_name)
						+ "软件\n" + Address.ABOUT_SHARE);
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				sp.setSite(context.getResources().getString(R.string.app_name));
				sp.setSiteUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isTopic) {
				sp.setTitle(topicCourse.getTitle()); // 设置标题
				sp.setTitleUrl(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId()); // 标题的超链接
				sp.setText(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId());
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				sp.setSite(topicCourse.getTitle());
				sp.setSiteUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			}
			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);
			ShareDialog.this.cancel();
			break;
		case 2: // 微信
			// ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			if (isCourse) {
				sp.setTitle(course.getName()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setUrl(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
			} else if (isInformation) {
				sp.setTitle(course.getTitle()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setUrl(Address.INFORMATION_SHARE + course.getId());
				Log.i("xm", course.getPicture() + "---pic");
				if (course.getPicture() != null) {
					// sp.setImagePath(Address.IMAGE_NET + course.getPicture());
					sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
				} else {
					sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
					sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				}
			} else if (isAbout) {
				sp.setTitle(context.getResources().getString(R.string.app_name)); // 标题
				sp.setText(context.getResources().getString(R.string.app_name)
						+ "软件\n" + Address.ABOUT_SHARE);
				sp.setUrl(Address.ABOUT_SHARE);
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isTopic) {
				sp.setTitle(topicCourse.getTitle()); // 设置标题
				sp.setText(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId());
				sp.setUrl(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId());
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			}
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			wechat.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			wechat.share(sp);
			ShareDialog.this.cancel();
			break;
		case 3: // 微信朋友圈
			// ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			if (isCourse) {
				sp.setTitle(course.getName()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setUrl(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
			} else if (isInformation) {
				sp.setTitle(course.getTitle()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setUrl(Address.INFORMATION_SHARE + course.getId());
				if (course.getPicture() != null) {
					// sp.setImagePath(Address.IMAGE_NET + course.getPicture());
					sp.setImageUrl(Address.IMAGE_NET + course.getPicture());
				} else {
					sp.setImagePath("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
					sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
				}
			} else if (isAbout) {
				sp.setTitle(context.getResources().getString(R.string.app_name)); // 标题
				sp.setText(context.getResources().getString(R.string.app_name)
						+ "软件\n" + Address.ABOUT_SHARE);
				sp.setUrl(Address.ABOUT_SHARE);
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isTopic) {
				Log.i("lala", "微信的朋友圈");
				sp.setTitle(topicCourse.getTitle()); // 设置标题
				sp.setText("分享");
				sp.setUrl(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId()); // 标题的超链接
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");

			}
			Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
			wechatMoment.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			wechatMoment.share(sp);
			ShareDialog.this.cancel();
			break;
		case 4: // 新浪微博
			// ShareParams sp = new ShareParams();
			if (isCourse) {
				sp.setTitle(course.getName()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl(Address.IMAGE_NET + course.getMobileLogo());
				// sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isInformation) {
				sp.setTitle(course.getTitle()); // 标题
				sp.setText(Address.COURSE_SHARE + course.getId());
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isAbout) {
				sp.setTitle(context.getResources().getString(R.string.app_name)); // 标题
				sp.setText(context.getResources().getString(R.string.app_name)
						+ "软件\n" + Address.ABOUT_SHARE);
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			} else if (isTopic) {
				sp.setTitle(topicCourse.getTitle()); // 设置标题
				sp.setText(com.yizhilu.community.utils.Address.SNSHOST
						+ "/front/topic/topicInfor/" + topicCourse.getId()); // 标题的超链接
				sp.setImageUrl("http://static.huitianedu.com/upload/eduplat/websiteLogo/20160913/1473738944730319283.png");
			}
			Platform sign = ShareSDK.getPlatform(SinaWeibo.NAME);
			sign.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			sign.share(sp);
			ShareDialog.this.cancel();
			break;
		default:
			break;
		}

		// if (position == 0) { // qq好友
		// ShareParams sp = new ShareParams();
		// sp.setTitle("测试分享的标题");
		// sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
		// sp.setText("Text文本内容 http://www.baidu.com");
		// Platform qzone = ShareSDK.getPlatform(QQ.NAME);
		// qzone.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// qzone.share(sp);
		//
		// } else if (position == 1) { // qq空间
		// ShareParams sp = new ShareParams();
		// sp.setTitle("测试分享的标题");
		// sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
		// sp.setText("Text文本内容 http://www.baidu.com");
		// sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// sp.setSite("sharesdk");
		// sp.setSiteUrl("http://sharesdk.cn");
		// Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		// qzone.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// qzone.share(sp);
		//
		// } else if (position == 2) { // 腾讯微博
		// ShareParams sp = new ShareParams();
		// sp.setTitle("测试分享的文本");
		// sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
		// Platform weibo = ShareSDK.getPlatform(TencentWeibo.NAME);
		// weibo.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// weibo.removeAccount(true);
		// ShareSDK.removeCookieOnAuthorize(true);
		// weibo.share(sp);
		//
		// } else if (position == 3) { // 微信好友
		// ShareParams sp = new ShareParams();
		// sp.setText("测试分享的文本");
		// Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
		// weibo.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// weibo.share(sp);
		// } else if (position == 4) { // 微信朋友圈
		// ShareParams sp = new ShareParams();
		// Platform weibo = ShareSDK.getPlatform(WechatMoments.NAME);
		// sp.setText("Text文本内容 http://www.baidu.com");
		// sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// weibo.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// weibo.share(sp);
		//
		// } else if (position == 5) { // 新浪微博
		// ShareParams sp = new ShareParams();
		// sp.setText("测试分享的文本");
		// // sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
		// Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		// weibo.setPlatformActionListener(this); // 设置分享事件回调
		// // 执行图文分享
		// weibo.share(sp);
		// }

	}

	@Override
	public void onCancel(Platform platform, int arg1) {
		// 取消
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "oncancel............");
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		// 成功
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onComplete............");
	}

	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		// 失敗
		// 打印错误信息,print the error msg
		t.printStackTrace();
		// 错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onError............");
	}

	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			Toast.makeText(context, "分享成功", 10000).show();
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
			Toast.makeText(context, "分享失败", 10000).show();
		}
			break;
		case 3: {
			// 取消
			Toast.makeText(context, "分享取消", 10000).show();
		}
			break;
		}

		return false;

	}

}
