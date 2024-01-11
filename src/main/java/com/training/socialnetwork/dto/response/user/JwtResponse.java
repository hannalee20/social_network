package com.training.socialnetwork.dto.response.user;

public class JwtResponse {

	private String jwt;

	public JwtResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
}
