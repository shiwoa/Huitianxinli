package com.yizhilu.entity;

import java.io.Serializable;
import java.util.List;

import android.R.integer;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-12 上午10:11:12
 * 类说明:课程的实体
 */
public class EntityCourse implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String logo;
	private int recommendId;
	private int pagePlaycount;
	private int orderNum;
	private String degree;
	private String component;
	private String readyTime;
	private String cookTime;
	private int courseId;
	private String courseName;
	private String courseMobileImgUrl;
	private String title;
	private List<TeacherEntity> teacherList;
	private String name;
	private String mobileLogo;
	private int lessionnum;
	private int isPay;
	private float sourceprice;
	private float currentprice;
	private int losetype;
	private int pageViewcount;
	private String context;
	private int parentId;
	private int type;
	private int pageCommentcount;
	private int status;
	private String addTime;
	private int sort;
	private int playcount;
	private int playCount;
	private int playNum;
	private int isfree;
	private String videotype;
	private String videourl;
	private int teacherId;
	private int courseMinutes;
	private int courseSeconds;
	private String videojson;
	private String meta;
	private String description;
	private String picture;
	private String createTime;
	private String updateTime;
	private String author;
	private int clickTimes;
	private List<EntityCourse> childKpoints;
	private int subjectId;
	private String subjectName;
	private int level;
	private String currentPirce;
	private String courseImgUrl;
	private String courseTitle;
	private String lessionNum;
	private String loseAbsTime;
	private String loseTime;
	private int favouriteId;
	private String fileType;
	private float currentPrice;
	private int playCounts;
	private int comcount;
	private int audtion;
	//音频
	private int audioId;
	private String audioName;
	private String summary;
	private String info;
	private String imgUrl;
	private int commentNum;
	private int lookNum;
	private int pageLookNum;
	private int buyNum;
	private int pageBuyNum;
	private int pagePlayNum;
	private int validityTime;
	private int price;
	private int nowPrice;
	private String teacherName;
	//直播
	private String oneLiveName;
	private int	isavaliable;
	private String	addtime;
	private String	coursetag;
	private String	packageLogo;
	private String	updateuser;
	private int	pageBuycount;
	private String	sellType;
	private String	liveBeginTime;
	private String	liveEndTime;
	private int	couponId;
	private String	examLink;
	private String	courseYear;
	private int	viewcount;
	private int	count;
	private	int OneCount;
	private int	endOneCount;
	private int	liveId;
	private String	liveName;
	private String	liveUrl;
	private String	token;
	private String	startTime;
	private String	endTime;
	private int minute;
	private int	second;
	private int	liveType;
	private int	livePlayStatu;
	private int kpointId;
	public String getOneLiveName() {
		return oneLiveName;
	}
	public void setOneLiveName(String oneLiveName) {
		this.oneLiveName = oneLiveName;
	}
	public int getIsavaliable() {
		return isavaliable;
	}
	public void setIsavaliable(int isavaliable) {
		this.isavaliable = isavaliable;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getCoursetag() {
		return coursetag;
	}
	public void setCoursetag(String coursetag) {
		this.coursetag = coursetag;
	}
	public String getPackageLogo() {
		return packageLogo;
	}
	public void setPackageLogo(String packageLogo) {
		this.packageLogo = packageLogo;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public int getPageBuycount() {
		return pageBuycount;
	}
	public void setPageBuycount(int pageBuycount) {
		this.pageBuycount = pageBuycount;
	}
	public String getSellType() {
		return sellType;
	}
	public void setSellType(String sellType) {
		this.sellType = sellType;
	}
	public String getLiveBeginTime() {
		return liveBeginTime;
	}
	public void setLiveBeginTime(String liveBeginTime) {
		this.liveBeginTime = liveBeginTime;
	}
	public String getLiveEndTime() {
		return liveEndTime;
	}
	public void setLiveEndTime(String liveEndTime) {
		this.liveEndTime = liveEndTime;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getExamLink() {
		return examLink;
	}
	public void setExamLink(String examLink) {
		this.examLink = examLink;
	}
	public String getCourseYear() {
		return courseYear;
	}
	public void setCourseYear(String courseYear) {
		this.courseYear = courseYear;
	}
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getOneCount() {
		return OneCount;
	}
	public void setOneCount(int oneCount) {
		OneCount = oneCount;
	}
	public int getEndOneCount() {
		return endOneCount;
	}
	public void setEndOneCount(int endOneCount) {
		this.endOneCount = endOneCount;
	}
	public int getLiveId() {
		return liveId;
	}
	public void setLiveId(int liveId) {
		this.liveId = liveId;
	}
	public String getLiveName() {
		return liveName;
	}
	public void setLiveName(String liveName) {
		this.liveName = liveName;
	}
	public String getLiveUrl() {
		return liveUrl;
	}
	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getLiveType() {
		return liveType;
	}
	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}
	public int getLivePlayStatu() {
		return livePlayStatu;
	}
	public void setLivePlayStatu(int livePlayStatu) {
		this.livePlayStatu = livePlayStatu;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public int getAudioId() {
		return audioId;
	}
	public void setAudioId(int audioId) {
		this.audioId = audioId;
	}
	public String getAudioName() {
		return audioName;
	}
	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
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
	public int getPagePlayNum() {
		return pagePlayNum;
	}
	public void setPagePlayNum(int pagePlayNum) {
		this.pagePlayNum = pagePlayNum;
	}
	public int getValidityTime() {
		return validityTime;
	}
	public void setValidityTime(int validityTime) {
		this.validityTime = validityTime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(int nowPrice) {
		this.nowPrice = nowPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}
	public int getPlayNum() {
		return playNum;
	}
	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public String getLoseTime() {
		return loseTime;
	}
	public void setLoseTime(String loseTime) {
		this.loseTime = loseTime;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getLoseAbsTime() {
		return loseAbsTime;
	}
	public void setLoseAbsTime(String loseAbsTime) {
		this.loseAbsTime = loseAbsTime;
	}
	public int getFavouriteId() {
		return favouriteId;
	}
	public void setFavouriteId(int favouriteId) {
		this.favouriteId = favouriteId;
	}
	public String getCurrentPirce() {
		return currentPirce;
	}
	public void setCurrentPirce(String currentPirce) {
		this.currentPirce = currentPirce;
	}
	public String getCourseImgUrl() {
		return courseImgUrl;
	}
	public void setCourseImgUrl(String courseImgUrl) {
		this.courseImgUrl = courseImgUrl;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getLessionNum() {
		return lessionNum;
	}
	public void setLessionNum(String lessionNum) {
		this.lessionNum = lessionNum;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getClickTimes() {
		return clickTimes;
	}
	public void setClickTimes(int clickTimes) {
		this.clickTimes = clickTimes;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getPlaycount() {
		return playcount;
	}
	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}
	public int getIsfree() {
		return isfree;
	}
	public void setIsfree(int isfree) {
		this.isfree = isfree;
	}
	public String getVideotype() {
		return videotype;
	}
	public void setVideotype(String videotype) {
		this.videotype = videotype;
	}
	public String getVideourl() {
		return videourl;
	}
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public int getCourseMinutes() {
		return courseMinutes;
	}
	public void setCourseMinutes(int courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	public int getCourseSeconds() {
		return courseSeconds;
	}
	public void setCourseSeconds(int courseSeconds) {
		this.courseSeconds = courseSeconds;
	}
	public String getVideojson() {
		return videojson;
	}
	public void setVideojson(String videojson) {
		this.videojson = videojson;
	}
	public List<EntityCourse> getChildKpoints() {
		return childKpoints;
	}
	public void setChildKpoints(List<EntityCourse> childKpoints) {
		this.childKpoints = childKpoints;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public int getIsPay() {
		return isPay;
	}
	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}
	public float getSourceprice() {
		return sourceprice;
	}
	public void setSourceprice(float sourceprice) {
		this.sourceprice = sourceprice;
	}
	public float getCurrentprice() {
		return currentprice;
	}
	public void setCurrentprice(float currentprice) {
		this.currentprice = currentprice;
	}
	public int getLosetype() {
		return losetype;
	}
	public void setLosetype(int losetype) {
		this.losetype = losetype;
	}
	public int getPageViewcount() {
		return pageViewcount;
	}
	public void setPageViewcount(int pageViewcount) {
		this.pageViewcount = pageViewcount;
	}
	public List<TeacherEntity> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<TeacherEntity> teacherList) {
		this.teacherList = teacherList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileLogo() {
		return mobileLogo;
	}
	public void setMobileLogo(String mobileLogo) {
		this.mobileLogo = mobileLogo;
	}
	public int getLessionnum() {
		return lessionnum;
	}
	public void setLessionnum(int lessionnum) {
		this.lessionnum = lessionnum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(int recommendId) {
		this.recommendId = recommendId;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Float currentPrice) {
		this.currentPrice = currentPrice;
	}
	public int getKpointId() {
		return kpointId;
	}
	public void setKpointId(int kpointId) {
		this.kpointId = kpointId;
	}
	public int getPagePlaycount() {
		return pagePlaycount;
	}
	public void setPagePlaycount(int pagePlaycount) {
		this.pagePlaycount = pagePlaycount;
	}
	public int getPlayCounts() {
		return playCounts;
	}
	public void setPlayCounts(int playCounts) {
		this.playCounts = playCounts;
	}
	public int getComcount() {
		return comcount;
	}
	public void setComcount(int comcount) {
		this.comcount = comcount;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(String readyTime) {
		this.readyTime = readyTime;
	}
	public String getCookTime() {
		return cookTime;
	}
	public void setCookTime(String cookTime) {
		this.cookTime = cookTime;
	}
	public int getPageCommentcount() {
		return pageCommentcount;
	}
	public void setPageCommentcount(int pageCommentcount) {
		this.pageCommentcount = pageCommentcount;
	}
	public String getCourseMobileImgUrl() {
		return courseMobileImgUrl;
	}
	public void setCourseMobileImgUrl(String courseMobileImgUrl) {
		this.courseMobileImgUrl = courseMobileImgUrl;
	}
	public int getAudtion() {
		return audtion;
	}
	public void setAudtion(int audtion) {
		this.audtion = audtion;
	}
	

}
