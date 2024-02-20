package com.training.socialnetwork.dto.response.user;

public class UserGetTokenResponseDto {

	private String jwt;

	public UserGetTokenResponseDto(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}
