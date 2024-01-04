package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.training.socialnetwork.util.contanst.Constant;

public class UserLoginDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 4, max = 16, message = Constant.INVALID_USERNAME_OR_PASSWORD)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Size(min = 4, max = 16, message = Constant.INVALID_USERNAME_OR_PASSWORD)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
