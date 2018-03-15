package com.yizhilu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.slidingmenu.lib.R.bool;

/**
 * @author bin 修改人: 时间:2015-10-12 上午10:00:28 类说明:实体类中公共的实体
 */
public class EntityPublic implements Serializable {

	private static final long serialVersionUID = 1L;
	// 用户信息
	private int id;
	private String nickname;
	private String email;
	private int emailIsavalible;
	private String mobile;
	private int mobileIsavalible;
	private String password;
	private int isavalible;
	private LearnedEntry queryCustomer;
	private String customerkey;
	private List<EntityCourse> courseDtoList;
	private String createdate;
	private String userip;
	private String realname;
	private int gender;
	private String avatar;
	private String bannerUrl;
	private int studysubject;
	private int courseId;
	private int weiBoNum;
	private int fansNum;
	private int attentionNum;
	private int msgNum;
	private int sysMsgNum;
	private int pagePlaycount;
	private String lastSystemTime;
	private int unreadFansNum;
	private String showname;
	private String userInfo;
	private int cusId;
	private int commonFriendNum;
	private int friendId;
	private int mutual;
	private int cusNum;
	private int current;
	private int courseNum;
	private int courseStudyNum;
	private int studyNum;
	private String registerFrom;
	private String balance;
	private String addTime;
	private EntityPublic userExpandDto;
	private List<EntityPublic> letterList;
	// 课程列表
	private List<TeacherEntity> teacherList;
	private List<EntityCourse> courseList;
	private List<EntityCourse> favouriteCourses;
	private PageEntity page;
	private List<SubjectEntity> subjectList;
	private TeacherEntity teacher;
	// 课程详情
	private EntityCourse course;
	private int currentCourseId;
	private int defaultKpointId;
	private List<EntityCourse> courseKpoints;
	private boolean isok;
	private boolean isFav;
	private boolean notPayOrder;
	private List<EntityCourse> coursePackageList;
	private List<EntityCourse> articleList;
	// banner图
	private List<BannerEntity> indexCenterBanner;
	// 支付宝信息
	private String sellerEmail;
	private String alipaykey;
	private String alipaypartnerID;
	private String status;
	private String publickey;
	private String privatekey;
	// 我的订单
	private List<EntityCourse> studyList;
	private List<OrderEntity> trxorderList;
	private List<OrderEntity> orderList;
	private String orderNo;
	private String out_trade_no;
	private String bankAmount;
	private String payType;
	private int orderId;
	private int userId;
	private String content;
	private String createTime;
	private String shortContent;
	// 我的账户
	private List<EntityAccList> accList;
	private EntityUserAccount userAccount;
	private List<EntityPublic> assessList;
	// 版本更新
	private String android_url;
	private String ios_v;
	private String ios_url;
	private String android_v;
	private EntityPublic user;
	private int loginNum;
	private int examNum;
	private int noteNum;
	private int assessNum;
	// 优惠券
	public List<CouponEntity> couponList;
	// 验证
	private String fileType;
	private String videoUrl;
	private String videoType;
	private int kpointId;
	private String message;
	private boolean success;
	private List<EntityPublic> entity;
	private int couponId;
	private String requestId;
	private int trxorderId;
	private String couponCode;
	private String title;
	private String startTime;
	private String endTime;
	private int limitAmount;
	private String amount;
	private int useType;
	private int type;
	private String optuserName;
	private String remindStatus;
	// 待支付订单
	private EntityPublic trxorder;
	private List<EntityPublic> detailList;
	private String orderAmount;
	private String courseName;
	private String courseLogo;
	private EntityPublic couponCodeDTO;
	private String verifyRegEmailCode;
	private String verifyRegMobileCode;
	// 第三方
	private String name;
	private String profiletype;
	private String value;
	private int userid;
	private String profiledate;
	// 微信信息
	private String mobilePayKey;
	private String mobileAppId;
	private String mobileMchId;
	// 视频类型
	private EntityPublic CC;
	private EntityPublic POLYV;
	private EntityPublic P56;
	private EntityPublic LETV;
	// CC视频
	private String ccappID;
	private String ccappKEY;
	// 保利视频
	private String plUserId;
	private String appSdk;
	private String readtoken;
	private String secretkey;
	private String writetoken;
	// 56视频
	private String p56appID;
	private String p56appKEY;
	// 乐视视频
	private String user_unique;
	private String secret_key;
	private String keyType;
	private int count_4;
	private int count_1;
	private int count_3;
	private int count_2;
	// 登录
	private String memTime;
	private String downloadUrl;
	private String kType;
	private String versionNo;
	private String depict;
	private String lastLoginTime;
	// group
	private int groupNo;
	private int topicNo;
	private List<EntityPublic> topicList;
	private ArrayList<String> htmlImagesList;
	private List<EntityPublic> groupMembers;
	private int browseCounts;
	private int praiseCounts;
	private int commentCounts;
	private String htmlImages;
	private String imageUrl;
	private EntityPublic topic;
	private List<EntityPublic> commentDtoList;
	private String commentContent;
	private int praiseNumber;
	private List<EntityPublic> groupList;
	private String introduction;
	private int memberNum;
	private int topicCounts;
	private List<EntityPublic> topics;
	private int groupId;
	private String groupName;
	private String nickName;
	private EntityPublic groupCreator;
	private String showName;
	private EntityPublic group;
	private List<EntityPublic> allTopicList;
	private List<EntityPublic> smallGroupLeader;
	private EntityPublic groupLeader;
	private List<EntityPublic> joinGroupList;
	private List<EntityPublic> manageGroupList;
	private List<EntityPublic> hotGroupList;
	private int jobType;
	private int ifAudit;
	private int top;
	private int essence;
	private int fiery;
	private int whetherTheMembers;
	private boolean haveGroup;
	// 咨询
	private List<EntityPublic> articleTypeList;
	// 音频
	private List<EntityPublic> audioConditionList;
	private String audioName;
	private String imgUrl;
	private String Summary;
	private int audioId;
	private String subjectId;
	private String commentNum;
	private int lookNum;
	private int pageLookNum;
	private int buyNum;
	private int pageBuyNum;
	private int playNum;
	private int pagePlayNum;
	private String validityTime;
	private String sort;
	private String price;
	private String nowPrice;
	private EntityPublic audioCondition;
	private boolean isOk;
	private String info;
	private List<EntityPublic> audioNodeCommentList;
	private List<EntityPublic> allNodeList;
	private String nodeName;
	private String playUrl;
	private int nodeId;
	private List<EntityPublic> parentList;
	private List<EntityPublic> audioNodeList;
	private Boolean isCanPlay;
	private String teacherName;
	private List<EntityPublic> audioRecommend;
	private String minute;
	private String second;
	private String playcount;
	// 后台控制开关
	private String verifyExam;
	private String verifyCourseLive;
	private String verifyLogin;
	private String verifyApp;
	private String verifyTranspond;
	private String verifyLimitLogin;
	private String verifySensitive;
	private String verifyCourse;
	private String verifyGro;
	private String yee;
	private EntityPublic verifyShare;
	private String share;
	private String verifyH5;
	private String verifyTeacherMien;
	private String verifyPractice;
	private String verifyRegister;
	private String verifyAlipay;
	private String verifyEmail;
	private String verifykq;
	private String verifywx;
	private String verifyTeacherArticle;
	private String verifySns;
	private String visitorsToSeeTheCourse;
	private String verifyPhone;
	private String verifyCourseDiscuss;
	private String nodeSize;
	private Boolean haveAudio;
	private Boolean haveVideo;
	private boolean isSchool;
	public boolean getIsSchool() {
		return isSchool;
	}

	public void setIsSchool(boolean isSchool) {
		this.isSchool = isSchool;
	}

	//直播
	private List<EntityCourse> listCourseWeekLiveSum;
	//我的直播
	private List<EntityCourse> myLive;

	
	public List<EntityCourse> getMyLive() {
		return myLive;
	}

	public void setMyLive(List<EntityCourse> myLive) {
		this.myLive = myLive;
	}

	public List<EntityCourse> getListCourseWeekLiveSum() {
		return listCourseWeekLiveSum;
	}

	public void setListCourseWeekLiveSum(List<EntityCourse> listCourseWeekLiveSum) {
		this.listCourseWeekLiveSum = listCourseWeekLiveSum;
	}

	public String getNodeSize() {
		return nodeSize;
	}

	public void setNodeSize(String nodeSize) {
		this.nodeSize = nodeSize;
	}

	public String getVerifyExam() {
		return verifyExam;
	}

	public void setVerifyExam(String verifyExam) {
		this.verifyExam = verifyExam;
	}

	public String getVerifyCourseLive() {
		return verifyCourseLive;
	}

	public void setVerifyCourseLive(String verifyCourseLive) {
		this.verifyCourseLive = verifyCourseLive;
	}

	public String getVerifyLogin() {
		return verifyLogin;
	}

	public void setVerifyLogin(String verifyLogin) {
		this.verifyLogin = verifyLogin;
	}

	public String getVerifyApp() {
		return verifyApp;
	}

	public void setVerifyApp(String verifyApp) {
		this.verifyApp = verifyApp;
	}

	public String getVerifyTranspond() {
		return verifyTranspond;
	}

	public void setVerifyTranspond(String verifyTranspond) {
		this.verifyTranspond = verifyTranspond;
	}

	public String getVerifyLimitLogin() {
		return verifyLimitLogin;
	}

	public void setVerifyLimitLogin(String verifyLimitLogin) {
		this.verifyLimitLogin = verifyLimitLogin;
	}

	public String getVerifySensitive() {
		return verifySensitive;
	}

	public void setVerifySensitive(String verifySensitive) {
		this.verifySensitive = verifySensitive;
	}

	public String getVerifyCourse() {
		return verifyCourse;
	}

	public void setVerifyCourse(String verifyCourse) {
		this.verifyCourse = verifyCourse;
	}

	public String getVerifyGro() {
		return verifyGro;
	}

	public void setVerifyGro(String verifyGro) {
		this.verifyGro = verifyGro;
	}

	public String getYee() {
		return yee;
	}

	public void setYee(String yee) {
		this.yee = yee;
	}

	public EntityPublic getVerifyShare() {
		return verifyShare;
	}

	public void setVerifyShare(EntityPublic verifyShare) {
		this.verifyShare = verifyShare;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getVerifyH5() {
		return verifyH5;
	}

	public void setVerifyH5(String verifyH5) {
		this.verifyH5 = verifyH5;
	}

	public String getVerifyTeacherMien() {
		return verifyTeacherMien;
	}

	public void setVerifyTeacherMien(String verifyTeacherMien) {
		this.verifyTeacherMien = verifyTeacherMien;
	}

	public String getVerifyPractice() {
		return verifyPractice;
	}

	public void setVerifyPractice(String verifyPractice) {
		this.verifyPractice = verifyPractice;
	}

	public String getVerifyRegister() {
		return verifyRegister;
	}

	public void setVerifyRegister(String verifyRegister) {
		this.verifyRegister = verifyRegister;
	}

	public String getVerifyAlipay() {
		return verifyAlipay;
	}

	public void setVerifyAlipay(String verifyAlipay) {
		this.verifyAlipay = verifyAlipay;
	}

	public String getVerifyEmail() {
		return verifyEmail;
	}

	public void setVerifyEmail(String verifyEmail) {
		this.verifyEmail = verifyEmail;
	}

	public String getVerifykq() {
		return verifykq;
	}

	public void setVerifykq(String verifykq) {
		this.verifykq = verifykq;
	}

	public String getVerifywx() {
		return verifywx;
	}

	public void setVerifywx(String verifywx) {
		this.verifywx = verifywx;
	}

	public String getVerifyTeacherArticle() {
		return verifyTeacherArticle;
	}

	public void setVerifyTeacherArticle(String verifyTeacherArticle) {
		this.verifyTeacherArticle = verifyTeacherArticle;
	}

	public String getVerifySns() {
		return verifySns;
	}

	public void setVerifySns(String verifySns) {
		this.verifySns = verifySns;
	}

	public String getVisitorsToSeeTheCourse() {
		return visitorsToSeeTheCourse;
	}

	public void setVisitorsToSeeTheCourse(String visitorsToSeeTheCourse) {
		this.visitorsToSeeTheCourse = visitorsToSeeTheCourse;
	}

	public String getVerifyPhone() {
		return verifyPhone;
	}

	public void setVerifyPhone(String verifyPhone) {
		this.verifyPhone = verifyPhone;
	}

	public String getVerifyCourseDiscuss() {
		return verifyCourseDiscuss;
	}

	public void setVerifyCourseDiscuss(String verifyCourseDiscuss) {
		this.verifyCourseDiscuss = verifyCourseDiscuss;
	}

	public String getAudioName() {
		return audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public int getAudioId() {
		return audioId;
	}

	public void setAudioId(int audioId) {
		this.audioId = audioId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public int getLookNum() {
		return lookNum;
	}

	public void setLookNum(int lookNum) {
		this.lookNum = lookNum;
	}

	public int getPageLookNum() {
		return pageLookNum;
	}

	public void setPageLookNum(int pageLookNum) {
		this.pageLookNum = pageLookNum;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getPageBuyNum() {
		return pageBuyNum;
	}

	public void setPageBuyNum(int pageBuyNum) {
		this.pageBuyNum = pageBuyNum;
	}

	public int getPlayNum() {
		return playNum;
	}

	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}

	public int getPagePlayNum() {
		return pagePlayNum;
	}

	public void setPagePlayNum(int pagePlayNum) {
		this.pagePlayNum = pagePlayNum;
	}

	public String getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(String validityTime) {
		this.validityTime = validityTime;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}

	public List<EntityPublic> getArticleTypeList() {
		return articleTypeList;
	}

	public void setArticleTypeList(List<EntityPublic> articleTypeList) {
		this.articleTypeList = articleTypeList;
	}

	public List<EntityPublic> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<EntityPublic> groupList) {
		this.groupList = groupList;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getTopicCounts() {
		return topicCounts;
	}

	public void setTopicCounts(int topicCounts) {
		this.topicCounts = topicCounts;
	}

	public int getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public int getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(int topicNo) {
		this.topicNo = topicNo;
	}

	public List<EntityPublic> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<EntityPublic> topicList) {
		this.topicList = topicList;
	}

	public ArrayList<String> getHtmlImagesList() {
		return htmlImagesList;
	}

	public void setHtmlImagesList(ArrayList<String> htmlImagesList) {
		this.htmlImagesList = htmlImagesList;
	}

	public List<EntityPublic> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<EntityPublic> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public int getBrowseCounts() {
		return browseCounts;
	}

	public void setBrowseCounts(int browseCounts) {
		this.browseCounts = browseCounts;
	}

	public int getPraiseCounts() {
		return praiseCounts;
	}

	public void setPraiseCounts(int praiseCounts) {
		this.praiseCounts = praiseCounts;
	}

	public int getCommentCounts() {
		return commentCounts;
	}

	public void setCommentCounts(int commentCounts) {
		this.commentCounts = commentCounts;
	}

	public String getHtmlImages() {
		return htmlImages;
	}

	public void setHtmlImages(String htmlImages) {
		this.htmlImages = htmlImages;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public EntityPublic getTopic() {
		return topic;
	}

	public void setTopic(EntityPublic topic) {
		this.topic = topic;
	}

	public List<EntityPublic> getCommentDtoList() {
		return commentDtoList;
	}

	public void setCommentDtoList(List<EntityPublic> commentDtoList) {
		this.commentDtoList = commentDtoList;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public int getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(int praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public boolean isFav() {
		return isFav;
	}

	public void setIsFav(boolean isFav) {
		this.isFav = isFav;
	}

	public int getCount_4() {
		return count_4;
	}

	public void setCount_4(int count_4) {
		this.count_4 = count_4;
	}

	public int getCount_1() {
		return count_1;
	}

	public void setCount_1(int count_1) {
		this.count_1 = count_1;
	}

	public int getCount_3() {
		return count_3;
	}

	public void setCount_3(int count_3) {
		this.count_3 = count_3;
	}

	public int getCount_2() {
		return count_2;
	}

	public void setCount_2(int count_2) {
		this.count_2 = count_2;
	}

	public boolean isNotPayOrder() {
		return notPayOrder;
	}

	public void setNotPayOrder(boolean notPayOrder) {
		this.notPayOrder = notPayOrder;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getPlUserId() {
		return plUserId;
	}

	public void setPlUserId(String plUserId) {
		this.plUserId = plUserId;
	}

	public String getUser_unique() {
		return user_unique;
	}

	public void setUser_unique(String user_unique) {
		this.user_unique = user_unique;
	}

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getP56appID() {
		return p56appID;
	}

	public void setP56appID(String p56appID) {
		this.p56appID = p56appID;
	}

	public String getP56appKEY() {
		return p56appKEY;
	}

	public void setP56appKEY(String p56appKEY) {
		this.p56appKEY = p56appKEY;
	}

	public String getAppSdk() {
		return appSdk;
	}

	public void setAppSdk(String appSdk) {
		this.appSdk = appSdk;
	}

	public String getReadtoken() {
		return readtoken;
	}

	public void setReadtoken(String readtoken) {
		this.readtoken = readtoken;
	}

	public String getSecretkey() {
		return secretkey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public String getWritetoken() {
		return writetoken;
	}

	public void setWritetoken(String writetoken) {
		this.writetoken = writetoken;
	}

	public String getCcappID() {
		return ccappID;
	}

	public void setCcappID(String ccappID) {
		this.ccappID = ccappID;
	}

	public String getCcappKEY() {
		return ccappKEY;
	}

	public void setCcappKEY(String ccappKEY) {
		this.ccappKEY = ccappKEY;
	}

	public EntityPublic getCC() {
		return CC;
	}

	public void setCC(EntityPublic cC) {
		CC = cC;
	}

	public EntityPublic getPOLYV() {
		return POLYV;
	}

	public void setPOLYV(EntityPublic pOLYV) {
		POLYV = pOLYV;
	}

	public EntityPublic getP56() {
		return P56;
	}

	public void setP56(EntityPublic p56) {
		P56 = p56;
	}

	public EntityPublic getLETV() {
		return LETV;
	}

	public void setLETV(EntityPublic lETV) {
		LETV = lETV;
	}

	public String getMobilePayKey() {
		return mobilePayKey;
	}

	public void setMobilePayKey(String mobilePayKey) {
		this.mobilePayKey = mobilePayKey;
	}

	public String getMobileAppId() {
		return mobileAppId;
	}

	public void setMobileAppId(String mobileAppId) {
		this.mobileAppId = mobileAppId;
	}

	public String getMobileMchId() {
		return mobileMchId;
	}

	public void setMobileMchId(String mobileMchId) {
		this.mobileMchId = mobileMchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfiletype() {
		return profiletype;
	}

	public void setProfiletype(String profiletype) {
		this.profiletype = profiletype;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getProfiledate() {
		return profiledate;
	}

	public void setProfiledate(String profiledate) {
		this.profiledate = profiledate;
	}

	public int getStudyNum() {
		return studyNum;
	}

	public void setStudyNum(int studyNum) {
		this.studyNum = studyNum;
	}

	public String getVerifyRegEmailCode() {
		return verifyRegEmailCode;
	}

	public void setVerifyRegEmailCode(String verifyRegEmailCode) {
		this.verifyRegEmailCode = verifyRegEmailCode;
	}

	public String getVerifyRegMobileCode() {
		return verifyRegMobileCode;
	}

	public void setVerifyRegMobileCode(String verifyRegMobileCode) {
		this.verifyRegMobileCode = verifyRegMobileCode;
	}

	public EntityPublic getCouponCodeDTO() {
		return couponCodeDTO;
	}

	public void setCouponCodeDTO(EntityPublic couponCodeDTO) {
		this.couponCodeDTO = couponCodeDTO;
	}

	public String getCourseLogo() {
		return courseLogo;
	}

	public void setCourseLogo(String courseLogo) {
		this.courseLogo = courseLogo;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public EntityPublic getTrxorder() {
		return trxorder;
	}

	public void setTrxorder(EntityPublic trxorder) {
		this.trxorder = trxorder;
	}

	public List<EntityPublic> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<EntityPublic> detailList) {
		this.detailList = detailList;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public int getTrxorderId() {
		return trxorderId;
	}

	public void setTrxorderId(int trxorderId) {
		this.trxorderId = trxorderId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(int limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOptuserName() {
		return optuserName;
	}

	public void setOptuserName(String optuserName) {
		this.optuserName = optuserName;
	}

	public String getRemindStatus() {
		return remindStatus;
	}

	public void setRemindStatus(String remindStatus) {
		this.remindStatus = remindStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<EntityPublic> getEntity() {
		return entity;
	}

	public void setEntity(List<EntityPublic> entity) {
		this.entity = entity;
	}

	public int getKpointId() {
		return kpointId;
	}

	public void setKpointId(int kpointId) {
		this.kpointId = kpointId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public List<CouponEntity> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponEntity> couponList) {
		this.couponList = couponList;
	}

	public int getAssessNum() {
		return assessNum;
	}

	public void setAssessNum(int assessNum) {
		this.assessNum = assessNum;
	}

	public int getNoteNum() {
		return noteNum;
	}

	public void setNoteNum(int noteNum) {
		this.noteNum = noteNum;
	}

	public int getExamNum() {
		return examNum;
	}

	public void setExamNum(int examNum) {
		this.examNum = examNum;
	}

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}

	public EntityPublic getUser() {
		return user;
	}

	public void setUser(EntityPublic user) {
		this.user = user;
	}

	public String getAndroid_url() {
		return android_url;
	}

	public void setAndroid_url(String android_url) {
		this.android_url = android_url;
	}

	public String getIos_v() {
		return ios_v;
	}

	public void setIos_v(String ios_v) {
		this.ios_v = ios_v;
	}

	public String getIos_url() {
		return ios_url;
	}

	public void setIos_url(String ios_url) {
		this.ios_url = ios_url;
	}

	public String getAndroid_v() {
		return android_v;
	}

	public void setAndroid_v(String android_v) {
		this.android_v = android_v;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}

	public List<EntityPublic> getAssessList() {
		return assessList;
	}

	public void setAssessList(List<EntityPublic> assessList) {
		this.assessList = assessList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<OrderEntity> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderEntity> orderList) {
		this.orderList = orderList;
	}

	public List<OrderEntity> getTrxorderList() {
		return trxorderList;
	}

	public void setTrxorderList(List<OrderEntity> trxorderList) {
		this.trxorderList = trxorderList;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBankAmount() {
		return bankAmount;
	}

	public void setBankAmount(String bankAmount) {
		this.bankAmount = bankAmount;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<EntityAccList> getAccList() {
		return accList;
	}

	public void setAccList(List<EntityAccList> accList) {
		this.accList = accList;
	}

	public EntityUserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(EntityUserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public List<EntityCourse> getStudyList() {
		return studyList;
	}

	public void setStudyList(List<EntityCourse> studyList) {
		this.studyList = studyList;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public int getCourseStudyNum() {
		return courseStudyNum;
	}

	public void setCourseStudyNum(int courseStudyNum) {
		this.courseStudyNum = courseStudyNum;
	}

	public String getRegisterFrom() {
		return registerFrom;
	}

	public void setRegisterFrom(String registerFrom) {
		this.registerFrom = registerFrom;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public EntityPublic getUserExpandDto() {
		return userExpandDto;
	}

	public void setUserExpandDto(EntityPublic userExpandDto) {
		this.userExpandDto = userExpandDto;
	}

	public List<EntityCourse> getCourseKpoints() {
		return courseKpoints;
	}

	public void setCourseKpoints(List<EntityCourse> courseKpoints) {
		this.courseKpoints = courseKpoints;
	}

	public List<EntityCourse> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<EntityCourse> articleList) {
		this.articleList = articleList;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public EntityCourse getCourse() {
		return course;
	}

	public void setCourse(EntityCourse course) {
		this.course = course;
	}

	public int getCurrentCourseId() {
		return currentCourseId;
	}

	public void setCurrentCourseId(int currentCourseId) {
		this.currentCourseId = currentCourseId;
	}

	public int getDefaultKpointId() {
		return defaultKpointId;
	}

	public void setDefaultKpointId(int defaultKpointId) {
		this.defaultKpointId = defaultKpointId;
	}

	public boolean isIsok() {
		return isok;
	}

	public void setIsok(boolean isok) {
		this.isok = isok;
	}

	public List<EntityCourse> getCoursePackageList() {
		return coursePackageList;
	}

	public void setCoursePackageList(List<EntityCourse> coursePackageList) {
		this.coursePackageList = coursePackageList;
	}

	public List<TeacherEntity> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<TeacherEntity> teacherList) {
		this.teacherList = teacherList;
	}

	public List<EntityCourse> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<EntityCourse> courseList) {
		this.courseList = courseList;
	}

	public PageEntity getPage() {
		return page;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}

	public List<SubjectEntity> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<SubjectEntity> subjectList) {
		this.subjectList = subjectList;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public int getStudysubject() {
		return studysubject;
	}

	public void setStudysubject(int studysubject) {
		this.studysubject = studysubject;
	}

	public int getWeiBoNum() {
		return weiBoNum;
	}

	public void setWeiBoNum(int weiBoNum) {
		this.weiBoNum = weiBoNum;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	public int getAttentionNum() {
		return attentionNum;
	}

	public void setAttentionNum(int attentionNum) {
		this.attentionNum = attentionNum;
	}

	public int getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}

	public int getSysMsgNum() {
		return sysMsgNum;
	}

	public void setSysMsgNum(int sysMsgNum) {
		this.sysMsgNum = sysMsgNum;
	}

	public String getLastSystemTime() {
		return lastSystemTime;
	}

	public void setLastSystemTime(String lastSystemTime) {
		this.lastSystemTime = lastSystemTime;
	}

	public int getUnreadFansNum() {
		return unreadFansNum;
	}

	public void setUnreadFansNum(int unreadFansNum) {
		this.unreadFansNum = unreadFansNum;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public int getCusId() {
		return cusId;
	}

	public void setCusId(int cusId) {
		this.cusId = cusId;
	}

	public int getCommonFriendNum() {
		return commonFriendNum;
	}

	public void setCommonFriendNum(int commonFriendNum) {
		this.commonFriendNum = commonFriendNum;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public int getMutual() {
		return mutual;
	}

	public void setMutual(int mutual) {
		this.mutual = mutual;
	}

	public int getCusNum() {
		return cusNum;
	}

	public void setCusNum(int cusNum) {
		this.cusNum = cusNum;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEmailIsavalible() {
		return emailIsavalible;
	}

	public void setEmailIsavalible(int emailIsavalible) {
		this.emailIsavalible = emailIsavalible;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getMobileIsavalible() {
		return mobileIsavalible;
	}

	public void setMobileIsavalible(int mobileIsavalible) {
		this.mobileIsavalible = mobileIsavalible;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsavalible() {
		return isavalible;
	}

	public void setIsavalible(int isavalible) {
		this.isavalible = isavalible;
	}

	public String getCustomerkey() {
		return customerkey;
	}

	public void setCustomerkey(String customerkey) {
		this.customerkey = customerkey;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getAlipaykey() {
		return alipaykey;
	}

	public void setAlipaykey(String alipaykey) {
		this.alipaykey = alipaykey;
	}

	public String getAlipaypartnerID() {
		return alipaypartnerID;
	}

	public void setAlipaypartnerID(String alipaypartnerID) {
		this.alipaypartnerID = alipaypartnerID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	public String getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	public List<BannerEntity> getIndexCenterBanner() {
		return indexCenterBanner;
	}

	public void setIndexCenterBanner(List<BannerEntity> indexCenterBanner) {
		this.indexCenterBanner = indexCenterBanner;
	}

	public List<EntityCourse> getFavouriteCourses() {
		return favouriteCourses;
	}

	public void setFavouriteCourses(List<EntityCourse> favouriteCourses) {
		this.favouriteCourses = favouriteCourses;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public List<EntityPublic> getLetterList() {
		return letterList;
	}

	public void setLetterList(List<EntityPublic> letterList) {
		this.letterList = letterList;
	}

	public String getMemTime() {
		return memTime;
	}

	public void setMemTime(String memTime) {
		this.memTime = memTime;
	}

	public String getkType() {
		return kType;
	}

	public void setkType(String kType) {
		this.kType = kType;
	}

	public List<EntityPublic> getAudioConditionList() {
		return audioConditionList;
	}

	public void setAudioConditionList(List<EntityPublic> audioConditionList) {
		this.audioConditionList = audioConditionList;
	}

	public EntityPublic getAudioCondition() {
		return audioCondition;
	}

	public void setAudioCondition(EntityPublic audioCondition) {
		this.audioCondition = audioCondition;
	}

	public boolean isIsOk() {
		return isOk;
	}

	public void setIsOk(boolean isOk) {
		this.isOk = isOk;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<EntityPublic> getAudioNodeCommentList() {
		return audioNodeCommentList;
	}

	public void setAudioNodeCommentList(List<EntityPublic> audioNodeCommentList) {
		this.audioNodeCommentList = audioNodeCommentList;
	}

	public List<EntityPublic> getAllNodeList() {
		return allNodeList;
	}

	public void setAllNodeList(List<EntityPublic> allNodeList) {
		this.allNodeList = allNodeList;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<EntityPublic> getParentList() {
		return parentList;
	}

	public void setParentList(List<EntityPublic> parentList) {
		this.parentList = parentList;
	}

	public List<EntityPublic> getAudioNodeList() {
		return audioNodeList;
	}

	public void setAudioNodeList(List<EntityPublic> audioNodeList) {
		this.audioNodeList = audioNodeList;
	}

	public Boolean getIsCanPlay() {
		return isCanPlay;
	}

	public void setIsCanPlay(Boolean isCanPlay) {
		this.isCanPlay = isCanPlay;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public List<EntityPublic> getAudioRecommend() {
		return audioRecommend;
	}

	public void setAudioRecommend(List<EntityPublic> audioRecommend) {
		this.audioRecommend = audioRecommend;
	}

	public List<EntityPublic> getTopics() {
		return topics;
	}

	public void setTopics(List<EntityPublic> topics) {
		this.topics = topics;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public EntityPublic getGroupCreator() {
		return groupCreator;
	}

	public void setGroupCreator(EntityPublic groupCreator) {
		this.groupCreator = groupCreator;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public EntityPublic getGroup() {
		return group;
	}

	public void setGroup(EntityPublic group) {
		this.group = group;
	}

	public List<EntityPublic> getAllTopicList() {
		return allTopicList;
	}

	public void setAllTopicList(List<EntityPublic> allTopicList) {
		this.allTopicList = allTopicList;
	}

	public List<EntityPublic> getSmallGroupLeader() {
		return smallGroupLeader;
	}

	public void setSmallGroupLeader(List<EntityPublic> smallGroupLeader) {
		this.smallGroupLeader = smallGroupLeader;
	}

	public EntityPublic getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(EntityPublic groupLeader) {
		this.groupLeader = groupLeader;
	}

	public List<EntityPublic> getJoinGroupList() {
		return joinGroupList;
	}

	public void setJoinGroupList(List<EntityPublic> joinGroupList) {
		this.joinGroupList = joinGroupList;
	}

	public List<EntityPublic> getManageGroupList() {
		return manageGroupList;
	}

	public void setManageGroupList(List<EntityPublic> manageGroupList) {
		this.manageGroupList = manageGroupList;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getIfAudit() {
		return ifAudit;
	}

	public void setIfAudit(int ifAudit) {
		this.ifAudit = ifAudit;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public int getFiery() {
		return fiery;
	}

	public void setFiery(int fiery) {
		this.fiery = fiery;
	}

	public int getWhetherTheMembers() {
		return whetherTheMembers;
	}

	public void setWhetherTheMembers(int whetherTheMembers) {
		this.whetherTheMembers = whetherTheMembers;
	}

	public List<EntityPublic> getHotGroupList() {
		return hotGroupList;
	}

	public void setHotGroupList(List<EntityPublic> hotGroupList) {
		this.hotGroupList = hotGroupList;
	}

	public boolean isHaveGroup() {
		return haveGroup;
	}

	public void setHaveGroup(boolean haveGroup) {
		this.haveGroup = haveGroup;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getPlaycount() {
		return playcount;
	}

	public void setPlaycount(String playcount) {
		this.playcount = playcount;
	}

	public Boolean getHaveAudio() {
		return haveAudio;
	}

	public void setHaveAudio(Boolean haveAudio) {
		this.haveAudio = haveAudio;
	}

	public Boolean getHaveVideo() {
		return haveVideo;
	}

	public void setHaveVideo(Boolean haveVideo) {
		this.haveVideo = haveVideo;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public LearnedEntry getQueryCustomer() {
		return queryCustomer;
	}

	public void setQueryCustomer(LearnedEntry queryCustomer) {
		this.queryCustomer = queryCustomer;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public List<EntityCourse> getCourseDtoList() {
		return courseDtoList;
	}

	public void setCourseDtoList(List<EntityCourse> courseDtoList) {
		this.courseDtoList = courseDtoList;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getPagePlaycount() {
		return pagePlaycount;
	}

	public void setPagePlaycount(int pagePlaycount) {
		this.pagePlaycount = pagePlaycount;
	}

}
