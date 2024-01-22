package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

public class UserTokenDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private int otp;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

}
