package com.training.socialnetwork.dto.request.user;

import javax.validation.constraints.Pattern;

import com.training.socialnetwork.util.constant.Constant;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserUpdateRequestDto {

	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^$|^[a-zA-Z]{2,}(?: [a-zA-Z]+){0,2}$", message = Constant.INVALID_REAL_NAME)
	private String realName;

	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}", message = Constant.BIRTH_DATE_INVALID_MESSAGE)
	private String birthDate;

	@Schema(type = "string", example = " ")
	@Pattern(regexp = "^$|^(Male|Female|male|female)$", message = Constant.GENDER_INVALID_MESSAGE)
	private String gender;

	@Schema(type = "string", example = " ")
	private String address;

	@Schema(type = "string", example = " ")
	private String university;

	@Schema(type = "string", example = " ")
	private String job;

	@Schema(type = "string", example = " ")
	private String status;

	@Schema(type = "string", example = " ")
	private String about;

	private Integer avatar;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Integer getAvatar() {
		return avatar;
	}

	public void setAvatar(Integer avatar) {
		this.avatar = avatar;
	}

}
