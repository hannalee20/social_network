package com.training.socialnetwork.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.training.socialnetwork.dto.request.user.UserRegisterRequestDto;
import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.dto.response.user.UserDetailResponseDto;
import com.training.socialnetwork.dto.response.user.UserForgotPasswordResponseDto;
import com.training.socialnetwork.dto.response.user.UserRegisterResponseDto;
import com.training.socialnetwork.dto.response.user.UserReportResponseDto;
import com.training.socialnetwork.dto.response.user.UserSearchResponseDto;
import com.training.socialnetwork.dto.response.user.UserUpdateResponseDto;

public interface IUserService {
	
	UserRegisterResponseDto createUser(UserRegisterRequestDto user) throws Exception ;
	
	boolean loginUser(String username, String password) throws Exception;
	
	UserUpdateResponseDto updateInfo(UserUpdateRequestDto user, int userId, int loggedInUserId) throws Exception;
	
	UserDetailResponseDto getInfo(int userId) throws Exception;
	
	List<UserSearchResponseDto> searchUser(int userId, String keyword, Pageable paging);
	
	UserReportResponseDto getReportUser(int userId);
	
	UserForgotPasswordResponseDto forgotPassword(String email) throws Exception;
	
	String resetPassword(String token, String newPassword) throws Exception;
}
