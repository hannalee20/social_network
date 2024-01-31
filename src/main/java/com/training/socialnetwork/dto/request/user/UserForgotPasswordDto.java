package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserForgotPasswordDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Email
	@NotBlank
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
