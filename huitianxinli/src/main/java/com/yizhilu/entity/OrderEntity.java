package com.yizhilu.entity;

import java.io.Serializable;
import java.util.List;

public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<EntityCourse> trxorderDetailList;
	private int id;
	private String createTime;
	private String orderAmount;
	private String couponAmount;
	private String amount;
	private int couponCodeId;
	private String requestId;
	private String trxStatus;
	private String payType;
	private int version;
	private int orderType;
	private String userName;
	private int orderId;
	private String orderNo;
	private int userId;
	private String price;
	private String realPrice;
	private String payStatus;
	private String payTime;
	private String outTradeNo;
	private String reqchanle;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getReqchanle() {
		return reqchanle;
	}
	public void setReqchanle(String reqchanle) {
		this.reqchanle = reqchanle;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<EntityCourse> getTrxorderDetailList() {
		return trxorderDetailList;
	}
	public void setTrxorderDetailList(List<EntityCourse> trxorderDetailList) {
		this.trxorderDetailList = trxorderDetailList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}
	public int getCouponCodeId() {
		return couponCodeId;
	}
	public void setCouponCodeId(int couponCodeId) {
		this.couponCodeId = couponCodeId;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
}
