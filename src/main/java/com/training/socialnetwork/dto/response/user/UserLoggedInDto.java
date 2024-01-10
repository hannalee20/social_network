package com.training.socialnetwork.dto.response.user;

import java.util.List;

public class UserLoggedInDto {

	private int userId;
	private String username;
	private List<String> roles;

	public UserLoggedInDto(int userId, String username, List<String> roles) {
		super();
		this.userId = userId;
		this.username = username;
		this.roles = roles;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
