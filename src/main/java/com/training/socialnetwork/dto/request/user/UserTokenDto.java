package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.training.socialnetwork.util.constant.Constant;

public class UserTokenDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 4, max = 16, message = Constant.INVALID)
	@NotBlank
	private String username;

	@Pattern(regexp = "^([a-zA-Z0-9]+)$")
	@Size(min = 6, max = 16, message = Constant.INVALID)
	@NotBlank
	private String password;

	@Pattern(regexp = "^[0-9]*$")
	@Size(min = 100000, max = 999999, message = Constant.INVALID)
	@NotBlank
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
