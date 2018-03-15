package com.yizhilu.entity;

import java.io.Serializable;

public class EntityAccList implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int userId;
	private int trxorderId;
	private String requestId;
	private String outTradeNo;
	private int accountId;
	private String createTime;
	private int isDisplay;
	private double balance;
	private double cashAmount;
	private double vmAmount;
	private double trxAmount;
	private String description;
	private String actHistoryType;
	private String bizType;
	private int version;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getTrxorderId() {
		return trxorderId;
	}
	public void setTrxorderId(int trxorderId) {
		this.trxorderId = trxorderId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(double cashAmount) {
		this.cashAmount = cashAmount;
	}
	public double getVmAmount() {
		return vmAmount;
	}
	public void setVmAmount(double vmAmount) {
		this.vmAmount = vmAmount;
	}
	public double getTrxAmount() {
		return trxAmount;
	}
	public void setTrxAmount(double trxAmount) {
		this.trxAmount = trxAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActHistoryType() {
		return actHistoryType;
	}
	public void setActHistoryType(String actHistoryType) {
		this.actHistoryType = actHistoryType;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	

}
