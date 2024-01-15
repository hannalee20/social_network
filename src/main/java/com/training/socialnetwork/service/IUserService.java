package com.training.socialnetwork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.dto.response.user.UserSearchDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;

public interface IUserService {
	
	UserRegistedDto createUser(UserRegisterDto user) throws Exception ;
	
	boolean loginUser(String username, String password) throws Exception;
	
	UserUpdatedDto updateInfo(UserUpdateDto user, MultipartFile image, int userId, int loggedInUserId) throws Exception;
	
	UserDetailDto getInfo(int userId) throws Exception;
	
	List<UserSearchDto> searchUser(int userId, String keyword);
	
	UserReportDto getReportUser(int userId);
	
	String forgotPassword(String email, int userId) throws Exception;
	
	String resetPassword(String token, String newPassword) throws Exception;
}
