package com.yizhilu.entity;

import java.io.Serializable;

public class EntityUserAccount implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int userId;
	private String createTime;
	private String lastUpdateTime;
	private double balance;
	private double forzenAmount;
	private double cashAmount;
	private double vmAmount;
	private String accountStatus;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getForzenAmount() {
		return forzenAmount;
	}
	public void setForzenAmount(double forzenAmount) {
		this.forzenAmount = forzenAmount;
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
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	

}

