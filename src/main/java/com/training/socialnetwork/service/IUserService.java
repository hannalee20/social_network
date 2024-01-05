package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserLoggedInDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.User;

public interface IUserService {
	
	UserRegistedDto createUser(UserRegisterDto user) throws Exception ;
	
	UserLoggedInDto loginUser(String username, String password) throws Exception;
	
	UserUpdatedDto updateInfo(UserUpdateDto user, int userId) throws Exception;
	
	UserDetailDto getInfo(int userId) throws Exception;
	
	List<User> searchUser(int userId, String keyword);
}
