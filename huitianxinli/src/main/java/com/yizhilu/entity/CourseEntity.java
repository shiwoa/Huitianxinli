package com.yizhilu.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-12 上午10:07:40
 * 类说明:课程的总实体
 */
public class CourseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String message;
	private boolean success;
	private List<EntityCourse> entity;
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
	public List<EntityCourse> getEntity() {
		return entity;
	}
	public void setEntity(List<EntityCourse> entity) {
		this.entity = entity;
	}
}
