package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.training.socialnetwork.util.contanst.Constant;

public class UserUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userId;
	private String realName;
	private Date birthDate;
	private int gender;
	private String address;
	private String email;
	private String university;
	private String job;
	private String status;
	private String about;
	private String avatarUrl;

	@NotNull(message = Constant.INVALID_ID)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Pattern(regexp = "^[A-Z]([a-zA-Z]+)?$", message = Constant.INVALID_REAL_NAME)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = Constant.INVALID_EMAIL)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
