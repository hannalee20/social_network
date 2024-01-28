package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

public class UserForgotPasswordDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
