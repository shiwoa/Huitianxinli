package com.yizhilu.entity;

import java.io.Serializable;

/**
 * @author CaiBin
 *	优惠券的实体
 */
public class CouponEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int couponId;
	private int status;
	private String requestId;
	private int trxorderId;
	private int userId;
	private String createTime;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
}
