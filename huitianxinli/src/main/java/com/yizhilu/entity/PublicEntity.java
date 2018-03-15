package com.yizhilu.entity;

import java.io.Serializable;

/**
 * @author 杨财宾
 * 修改人:
 * 时间:2015-8-29 下午1:43:54
 * 类说明: 公共的实体类
 */
public class PublicEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String message;
	private boolean success;
	private EntityPublic entity;
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
	public EntityPublic getEntity() {
		return entity;
	}
	public void setEntity(EntityPublic entity) {
		this.entity = entity;
	}
}
