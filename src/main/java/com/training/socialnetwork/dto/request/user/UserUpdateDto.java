package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.training.socialnetwork.util.constant.Constant;

@JsonInclude(Include.NON_NULL)
public class UserUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "^[A-Z]([a-zA-Z]+)?$", message = Constant.INVALID_REAL_NAME)
	private String realName;
	
	private Date birthDate;
	
	@Pattern(regexp = "^(Male|Female|male|female)$", message = Constant.GENDER_INVALID_MESSAGE)
	private String sex;
	
	private String address;
	
	private String university;
	
	private String job;
	
	private String status;
	
	private String about;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

}
