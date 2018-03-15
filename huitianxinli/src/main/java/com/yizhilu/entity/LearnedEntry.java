package com.yizhilu.entity;

import java.io.Serializable;

public class LearnedEntry implements Serializable {
	int userId;
	String nickname;
	String email;
	String mobile;
	String avatar;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "LearnedEntry [userId=" + userId + ", nickname=" + nickname
				+ ", email=" + email + ", mobile=" + mobile + ", avatar="
				+ avatar + "]";
	}
	
	

}
