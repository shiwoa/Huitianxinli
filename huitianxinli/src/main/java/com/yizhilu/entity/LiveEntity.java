package com.yizhilu.entity;

import java.io.Serializable;

public class LiveEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
	private boolean success;
	private EntityLive entity;
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
	public EntityLive getEntity() {
		return entity;
	}
	public void setEntity(EntityLive entity) {
		this.entity = entity;
	}
	
}
