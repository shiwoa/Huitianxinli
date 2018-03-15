package com.yizhilu.entity;

import java.io.Serializable;

public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
	private boolean success;
	private String entity;

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

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
