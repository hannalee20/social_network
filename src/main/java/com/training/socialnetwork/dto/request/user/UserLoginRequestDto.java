package com.training.socialnetwork.dto.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.training.socialnetwork.util.constant.Constant;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserLoginRequestDto {

	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 4, max = 16, message = Constant.INVALID)
	@NotBlank
	private String username;

	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 6, max = 16, message = Constant.INVALID)
	@NotBlank
	private String password;

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

}
