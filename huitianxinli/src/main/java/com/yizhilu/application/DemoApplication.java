package com.yizhilu.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yizhilu.baoli.PolyvDemoService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author 杨财宾 时间:2015-8-29 类说明:程序的入口
 */
public class DemoApplication extends Application {
	private static List<Activity> activityList; // 存放每个类的集合
	private static DemoApplication instance; // 本类的对象
	private static ImageLoaderConfiguration configuration; // 获取图片的对象
	private CrashHandler crashHandler; // 异常类的对象
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;
	public static Boolean isNetWork = false;
	private Dialog dialog;
	// private static AudioPlayService audioPlayServicce; //音频播放的服务类
	private static AsyncHttpClient httpClient; // 联网获取数据
	private int drmServerPort;
	// ------------------------播放记录-----------------------------
	// private static DaoMaster daoMaster;
	// private static DaoSession daoSession;
	public static SQLiteDatabase db;
	// 数据库名，表名是自动被创建的
	public static final String DB_NAME = "dbname.db";

	// ------------------------播放记录-----------------------------

	public int getDrmServerPort() {

		return drmServerPort;
	}

	public void setDrmServerPort(int drmServerPort) {
		this.drmServerPort = drmServerPort;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		PolyvSDKClient client = PolyvSDKClient.getInstance();
		client.stopService(getApplicationContext(), PolyvDemoService.class);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化控件的方法
		initView();
		// 初始化保利视频
		initPolyvCilent();
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:初始化控件的方法
	 */
	private void initView() {
		activityList = new ArrayList<Activity>();
		// 实例化加载图片对象的方法
		initImageloader(this);
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:单例模式中获取唯一的DemoApplication实例
	 */
	public static DemoApplication getInstance() {
		if (null == instance) {
			instance = new DemoApplication();
		}
		return instance;
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:添加Activity到容器中
	 */
	public void addActivity(Activity activity) {
		if (activityList != null) {
			activityList.add(activity);
		}
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:移除Activity
	 */
	public void removeActivity(Activity activity) {
		if (activityList != null) {
			activityList.remove(activity);
		}
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:遍历所有Activity并finish
	 */
	public void exit() {
		if (activityList != null) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:finish传入的activity
	 */
	public void finishActivity(Activity activity) {
		if (activityList != null) {
			for (Activity activity2 : activityList) {
				if (activity2 == activity) {
					activity.finish();
				}
			}
		}
	}

	public List<Activity> getActivity() {
		if (activityList != null) {
			return activityList;
		}
		return new ArrayList<Activity>();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-16 下午3:45:57 方法说明:单点登录退出的操作
	 */
	public void SingleLoginExit(Activity mainActivity) {
		if (activityList != null) {
			for (Activity activity : activityList) {
				if (activity == mainActivity) {
					continue;
				}
				activity.finish();
			}
		}
	}

	/**
	 * @author 杨财宾 时间:2015-8-29 方法说明:实例化加载图片的对象
	 */
	public static void initImageloader(Context context) {
		if (configuration == null) {
			configuration = new ImageLoaderConfiguration.Builder(context).threadPoolSize(5)
					.memoryCache(new WeakMemoryCache()).build();
			ImageLoader.getInstance().init(configuration);
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-2-25 上午10:26:38 方法说明:乐视
	 */
	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps != null) {
			for (RunningAppProcessInfo procInfo : runningApps) {
				if (procInfo.pid == pid) {
					return procInfo.processName;
				}
			}
		}
		return null;
	}

	/**
	 * 保利威视
	 */
	public void initPolyvCilent() {
		// 网络方式取得SDK加密串，（推荐）
		// new LoadConfigTask().execute();
		PolyvSDKClient client = PolyvSDKClient.getInstance();
		// 设置SDK加密串
		// client.setConfig("你的SDK加密串");
		// client.setConfig("iPGXfu3KLEOeCW4KXzkWGl1UYgrJP7hRxUfsJGldI6DEWJpYfhaXvMA+32YIYqAOocWd051v5XUAU17LoVlgZCSEVNkx11g7CxYadcFPYPozslnQhFjkxzzjOt7lUPsW");
		// client.setConfig("vJu14TqV1liDDZ7bCFZxSpHlW5/UNWQeYqrsMGtNkRTvO5OEDJBarfCjM0KaLq24ZBFXabQMsLLE2ZNJ6sazttXqlcBmQiyXD2TEis5Q1W9gwhzpldnWcaSixYYWbCiZie0fE4IlcB9GOBN4P3OXHQ\u003d\u003d");
		client.setConfig(
				"f1w9Ax4AD5AkEgcdKfdnK/ohJ7Uu8uYzFIeuwSvjUdX8HyyFm0jIDKg5QiQ/qfxMAi2h4m6S4dmRCiqWDPEnWvZLbxwpjC6YRySr4hzGd/qjGPyieRA/4ZeGgzFvy2qbu5dPhZUE9ARwGVfBMFegCA\u003d\u003d");
		// 初始化数据库服务
		client.initDatabaseService(this);
		// 启动服务
		client.startService(getApplicationContext(), PolyvDemoService.class);
		// 启动Bugly
		client.initCrashReport(getApplicationContext());
		// 启动Bugly后，在学员登录时设置学员id
		// client.crashReportSetUserId(userId);
		// 获取SD卡信息
		PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

			@Override
			public void callback() {
				if (PolyvDevMountInfo.getInstance().isSDCardAvaiable() == false) {
					return;
				}

				long remainedSpareInMB = 100;
				if (PolyvDevMountInfo.getInstance().getSDCardAvailSpace() * 1024 < remainedSpareInMB) {
					return;
				}

				StringBuilder dirPath = new StringBuilder();
				dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator)
						.append("polyvdownload");
				File saveDir = new File(dirPath.toString());
				if (saveDir.exists() == false) {
					saveDir.mkdir();
				}

				// 如果生成不了文件夹，可能是外部SD卡需要写入特定目录/storage/sdcard1/Android/data/包名/
				if (saveDir.exists() == false) {
					dirPath.delete(0, dirPath.length());
					dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator)
							.append("Android").append(File.separator).append("data").append(File.separator)
							.append(getPackageName()).append(File.separator).append("polyvdownload");
					saveDir = new File(dirPath.toString());
					getExternalFilesDir(null); // 生成包名目录
					saveDir.mkdirs();
				}

				if (saveDir.exists() == false) {
					return;
				}

				// 下载文件的目录
				PolyvSDKClient.getInstance().setDownloadDir(saveDir);
			}
		});
	}

	// public static AudioPlayService getAudioPlayServicce() {
	// return audioPlayServicce;
	// }
	//
	// public static void setAudioPlayServicce(AudioPlayService
	// audioPlayServicce) {
	// DemoApplication.audioPlayServicce = audioPlayServicce;
	// }

	/**
	 * @author bin 时间: 2016/5/26 14:10 方法说明:获取联网获取对象
	 */
	public static AsyncHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = new AsyncHttpClient();
		}
		return httpClient;
	}

	// --------------添加播放记录的数据库初始化----------------------
	// public static DaoMaster getDaoMaster(Context context) {
	// if (daoMaster == null) {
	// OpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME, null);
	// daoMaster = new DaoMaster(helper.getWritableDatabase());
	// }
	// return daoMaster;
	// }
	//
	//
	// public static DaoSession getDaoSession(Context context) {
	// if (daoSession == null) {
	// if (daoMaster == null) {
	// daoMaster = getDaoMaster(context);
	// }
	// daoSession = daoMaster.newSession();
	// }
	// return daoSession;
	// }
	//
	//
	// public static SQLiteDatabase getSQLDatebase(Context context) {
	// if (daoSession == null) {
	// if (daoMaster == null) {
	// daoMaster = getDaoMaster(context);
	// }
	// db = daoMaster.getDatabase();
	// }
	// return db;
	// }
	//
}
