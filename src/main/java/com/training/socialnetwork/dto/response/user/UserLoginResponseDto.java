package com.training.socialnetwork.dto.response.user;

public class UserLoginResponseDto {

	private String otp;

	public UserLoginResponseDto(String otp) {
		this.otp = otp;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	
}
