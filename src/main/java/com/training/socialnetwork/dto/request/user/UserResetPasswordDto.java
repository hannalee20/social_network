package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

public class UserResetPasswordDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String token;
	private String newPassword;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
