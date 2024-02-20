package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.training.socialnetwork.util.constant.Constant;

public class UserRegisterRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 4, max = 16, message = Constant.INVALID_USERNAME_OR_PASSWORD)
	@NotBlank
	private String username;

	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 6, max = 16, message = Constant.INVALID)
	@NotBlank
	private String password;

	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	@NotBlank
	private String email;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
