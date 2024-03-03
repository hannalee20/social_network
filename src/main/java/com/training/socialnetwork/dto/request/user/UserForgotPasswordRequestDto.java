package com.training.socialnetwork.dto.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserForgotPasswordRequestDto {
	
	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	@NotBlank
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
