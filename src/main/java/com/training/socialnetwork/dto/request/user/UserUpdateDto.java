package com.training.socialnetwork.dto.request.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.training.socialnetwork.util.constant.Constant;

@JsonInclude(Include.NON_NULL)
public class UserUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String realName;
	
	private Date birthDate;
	
	private String gender;
	
	private String address;
	
	private String university;
	
	private String job;
	
	private String status;
	
	private String about;

	@Pattern(regexp = "^[A-Z]([a-zA-Z]+)?$", message = Constant.INVALID_REAL_NAME)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Pattern(regexp = "^(Male|Female|male|female)$", message = Constant.GENDER_INVALID_MESSAGE)
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

}
