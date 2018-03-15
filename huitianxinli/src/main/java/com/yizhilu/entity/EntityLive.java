package com.yizhilu.entity;

import java.io.Serializable;
import java.util.List;

import android.R.integer;

public class EntityLive implements Serializable{

	private static final long serialVersionUID = 1L;
	//直播详情
	private int id;
	private String logo;
	private int endCount;
	private String title;
	private List<EntityLive> teacherList;
	private boolean isOk;
	private String name;
	private String context;
	private String currentPrice;
	private List<EntityLive> towList;
	private String courseBeginTime;
	private int lessionnum;
	private String courseEndTime;
	private List<EntityLive> livingList;
	//老师详情
	private int userId;
	private String education;
	private String career;
	private int isStar;
	private String picPath;
	private int status;
	private String createTime;
	private String updateTime;
	private int sort;
	//课程目录
	private int towLiveId;
	private int livePlayStatu;
	private String towLiveEndTime;
	private String towLiveName;
	private int towLiveParentId;
	private String  towLiveBeginTime;
	private String teacherName;
	private int minute;
	private int second;
	private int isLookBack;
	
	public int getIsLookBack() {
		return isLookBack;
	}
	public void setIsLookBack(int isLookBack) {
		this.isLookBack = isLookBack;
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
	public int getTowLiveId() {
		return towLiveId;
	}
	public void setTowLiveId(int towLiveId) {
		this.towLiveId = towLiveId;
	}
	public int getLivePlayStatu() {
		return livePlayStatu;
	}
	public void setLivePlayStatu(int livePlayStatu) {
		this.livePlayStatu = livePlayStatu;
	}
	public String getTowLiveEndTime() {
		return towLiveEndTime;
	}
	public void setTowLiveEndTime(String towLiveEndTime) {
		this.towLiveEndTime = towLiveEndTime;
	}
	public String getTowLiveName() {
		return towLiveName;
	}
	public void setTowLiveName(String towLiveName) {
		this.towLiveName = towLiveName;
	}
	public int getTowLiveParentId() {
		return towLiveParentId;
	}
	public void setTowLiveParentId(int towLiveParentId) {
		this.towLiveParentId = towLiveParentId;
	}
	public String getTowLiveBeginTime() {
		return towLiveBeginTime;
	}
	public void setTowLiveBeginTime(String towLiveBeginTime) {
		this.towLiveBeginTime = towLiveBeginTime;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public List<EntityLive> getTowList() {
		return towList;
	}
	public void setTowList(List<EntityLive> towList) {
		this.towList = towList;
	}
	public List<EntityLive> getLivingList() {
		return livingList;
	}
	public void setLivingList(List<EntityLive> livingList) {
		this.livingList = livingList;
	}
	public boolean isOk() {
		return isOk;
	}
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public int getIsStar() {
		return isStar;
	}
	public void setIsStar(int isStar) {
		this.isStar = isStar;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
	public int getEndCount() {
		return endCount;
	}
	public void setEndCount(int endCount) {
		this.endCount = endCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<EntityLive> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<EntityLive> teacherList) {
		this.teacherList = teacherList;
	}
	public boolean isIsOk() {
		return isOk;
	}
	public void setIsOk(boolean isOk) {
		this.isOk = isOk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getCourseBeginTime() {
		return courseBeginTime;
	}
	public void setCourseBeginTime(String courseBeginTime) {
		this.courseBeginTime = courseBeginTime;
	}
	public int getLessionnum() {
		return lessionnum;
	}
	public void setLessionnum(int lessionnum) {
		this.lessionnum = lessionnum;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}

	
}
