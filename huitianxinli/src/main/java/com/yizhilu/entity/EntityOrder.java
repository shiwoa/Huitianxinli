package com.yizhilu.entity;

public class EntityOrder {
	private String requestId;
	private String trxStatus;
	private String courseImgUrl;
	private String courseName;
	private String currentPirce;
	private double couponAmount;
	private String createTime;
	
	public EntityOrder(String requestId, String trxStatus, String courseImgUrl,
			String courseName, String currentPirce, double couponAmount,
			String createTime) {
		super();
		this.requestId = requestId;
		this.trxStatus = trxStatus;
		this.courseImgUrl = courseImgUrl;
		this.courseName = courseName;
		this.currentPirce = currentPirce;
		this.couponAmount = couponAmount;
		this.createTime = createTime;
	}
	public EntityOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTrxStatus() {
		return trxStatus;
	}
	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}
	public String getCourseImgUrl() {
		return courseImgUrl;
	}
	public void setCourseImgUrl(String courseImgUrl) {
		this.courseImgUrl = courseImgUrl;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCurrentPirce() {
		return currentPirce;
	}
	public void setCurrentPirce(String currentPirce) {
		this.currentPirce = currentPirce;
	}
	public double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
