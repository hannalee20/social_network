package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.training.socialnetwork.util.constant.Constant;

public class UserResetPasswordRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String token;

	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 6, max = 16, message = Constant.INVALID)
	@NotBlank
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
