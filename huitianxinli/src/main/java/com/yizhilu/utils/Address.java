package com.yizhilu.utils;

/**
 * @author 杨财宾 修改人: 时间:2015-8-29 下午1:37:10 类说明:接口地址的类
 */
public class Address {
	// 主域名  http://192.168.1.114:81/    http://www.huitianedu.com/app/
	public static String HOST = "http://www.huitianedu.com/app/";
	// 支付异步回掉的域名
	public static String HOST_PAY = "http://www.huitianedu.com/";
	// 访问图片
	public static String IMAGE_NET = "http://static.huitianedu.com";
	// 上传图片
	public static String UP_IMAGE_NET = "http://image.huitianedu.com";
	// 注册接口
	public static String REGISTER = HOST + "register";
	// 登录接口
	public static String LOGIN = HOST + "login";
	// 个人资料接口
	public static String MY_MESSAGE = HOST + "user/info";
	// 修改头像的接口
	public static String UPDATE_HEAD = HOST + "user/avatar";
	// 广告图
	public static String BANNER = HOST + "index/banner";
	// 推荐课程
	public static String RECOMMEND_COURSE = HOST + "index/course";
	// 首页公告
	public static String ANNOUNCEMENT = HOST + "index/article";
	// 课程列表
	public static String COURSE_LIST = HOST + "course/list";
	// 课程介绍
	public static String COURSE_CONTENT = HOST + "course/content/";
	// 课程播放记录列表
	public static String COURSE_PLAY_RECORD_LIST = HOST + "study/records";
	// 删除课程播放记录
	public static String DELETE_COURSE_PLAY_RECORD = HOST + "study/del";
	// 课程详情
	public static String COURSE_DETAILS = HOST + "course/info";
	// 验证播放节点
	public static String VERIFICATION_PLAY = HOST + "check/kpoint";
	// 资讯列表
	public static String INFORMATION_LIST = HOST + "article/list";
	// 资讯详情
	public static String INFORMATION_DETAILS = HOST + "article/info/";
	// 课程评论列表
	public static String COURSE_COMMENT_LIST = HOST + "course/assess/list";
	// 添加课程评论
	public static String ADD_COURSE_COMMENT = HOST + "course/assess/add";
	// 课程收藏列表
	public static String COURSE_COLLECT_LIST = HOST + "collection/list";
	// 添加课程收藏
	public static String ADD_COURSE_COLLECT = HOST + "collection/add";
	// 删除课程收藏
	public static String DELETE_COURSE_COLLECT = HOST + "collection/del";
	// 帮助反馈
	public static String HELP_FEEDBACK = HOST + "feedback/add";
	// 支付宝信息
	public static String ALIPAY_INFO = HOST + "alipay/info";
	// 微信信息
	public static String WEIXIN_INFO = HOST + "weixin/info";
	// 我购买的课程
	public static String MY_BUY_COURSE = HOST + "buy/courses";
	// 修改密码
	public static String UPDATE_PASSWORD = HOST + "user/update/pwd";
	// 修改个人信息
	public static String UPDATE_MYMESSAGE = HOST + "user/update/info";
	// 讲师列表
	public static String TEACHER_LIST = HOST + "teacher/list";
	// 讲师详情
	public static String TEACHER_DETAILS = HOST + "teacher/info";
	// 我的订单的接口
	public static String MY_ORDER_LIST = HOST + "order/list";
	// 创建订单的接口
	public static String CREATE_ORDER = HOST + "create/pay/order";
	// 支付前检测接口
	public static String PAYMENT_DETECTION = HOST + "order/payment";
	// 支付成功回调接口（注：在订单检测接口返回已支付成功的不能调用此接口）
	public static String PAYSUCCESS_CALL = HOST + "order/paysuccess";
	// 重新支付检验订单的接口
	public static String AGAINPAYVERIFICATIONORDER = HOST + "order/repayUpdateOrder";
	// 账户信息
	public static String USER_MESSAGE = HOST + "user/acc";
	// 播放记录
	public static String PLAY_HISTORY = HOST + "study/records";
	// 专业列表
	public static String MAJOR_LIST = HOST + "subject/list";
	// 课程里的教师列表
	public static String COURSE_TEACHER_LIST = HOST + "teacher/query";
	// 教师课程
	public static String TEACHER_COURSE = HOST + "course/teacher/list";
	// 请空课程
	public static String COLLECTION_CLEAN = HOST + "collection/clean";
	// 系统通告
	public static String SYSTEM_ANNOUNCEMENT = HOST + "user/letter";
	// 搜索
	public static String SEACRH = HOST + "search/result";
	// 检测版本更新
	public static String VERSIONUPDATE = HOST + "update/info";
	// 课程分享
	public static String COURSE_SHARE = HOST_PAY + "mobile/course/info/";
	// 资讯的分享
	public static String INFORMATION_SHARE = HOST_PAY + "mobile/article/";
	// 获取手机验证码
	public static String GET_PHONE_CODE = HOST + "sendMobileMessage";
	// 获取sgin
	public static String GET_SGIN = HOST + "getMobileKey";
	// 获取邮箱的验证码
	public static String GET_EMAIL_CODE = HOST + "sendEmailMessage";
	// 找回密码
	public static String GET_PASSWORD = HOST + "retrievePwd";
	// 个人简历
	public static String GET_PERSONAL_RESUME = HOST + "userResume";
	// js交互的接口
	public static String GET_JS = HOST + "limitLogin?";
	// 獲取優惠券
	public static String GET_USER_COUPON = HOST + "queryMyCouponCode";
	// 课程加载图文类型
	public static String GET_WEBVIEW_COURSE = HOST + "course/kpointWebView";
	// 添加登陆记录
	public static String ADD_LOGIN_RECORD = HOST_PAY + "api/appWebsite/addlogin";
	// 添加安装记录
	public static String ADD_INSTALL_RECORD = HOST_PAY + "api/install/addinstall";
	// 添加使用记录
	public static String ADD_APPLY_RECORD = HOST_PAY + "api/use/addUse";
	// 获取优惠券列表
	public static String GET_COUPON_LIST = HOST + "coupon/getCourseCoupon";
	// 第三方登录的接口
	public static String THIRDPARTYLOGIN_URL = HOST + "appLoginReturn";
	// 待支付订单
	public static String ORDER_NO_PAYMENT = HOST + "order/repay";
	// 获取邮箱或手机号接受验证码开关的接口
	public static String GETCODESWITCH = HOST + "emailMobileCodeSwitch";
	// 绑定已有账号
	public static String BINDINGEXISTACCOUNT = HOST + "loginBinding";
	// 绑定第三方注册接口
	public static String REGISTERBINDING = HOST + "appRegisterBinding";
	// 获取个人中心中绑定第三方接口
	public static String QUERYUSERBUNDLING = HOST + "queryUserBundling";
	// 解除绑定
	public static String UNBUNDLING = HOST + "unBundling";
	// 添加绑定接口
	public static String ADDBUNDLING = HOST + "addBundling";
	// 播放视频的类型
	public static String GET_PLAYVIDEO_TYPE = HOST + "video/playInfo";
	// 播放记录清空
	public static String PLAYHISTORY_CLEAN = HOST + "studyHistory/clean";
	// 删除播放记录
	public static String DELETE_COURSE_PLAYHISTORY = HOST + "studyHistory/del";
	// 注册类型的接口
	public static String REGIST_TYPE = HOST + "registerType";
	// 用户协议
	public static String USER_AGREEMENT = HOST + "user/queryUserProtocol";
	// 关于分享的链接
	public static String ABOUT_SHARE = "https://apple.huitianedu.com/index/index.html";
	// 取消收藏的接口
	public static String CANCEL_COLLECT = HOST + "collection/cleanFavorites";
	// 检测用户是否在其他地方登录
	public static String CHECK_USERISLOGIN = HOST + "check/userIsLogin";
	// 后台控制开关
	public static String WEBSITE_VERIFY_LIST = HOST + "website/verify/list";
	// 记录登录时间
	public static String LASTLOGINTIME = HOST + "lastLoginTime";
	// 判断是否为付费学院
	public static String RMBPLAYER = HOST + "validateIsPay";
	
	
	public static String ADD_STUDY_SCHEDULE = HOST+"/addStudySchedule";
	public static String ADD_TIME_RECORD = HOST+"/addTimeRecord";
	
}
