package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.User;

public interface IUserService {
	
	UserRegistedDto createUser(User user) throws Exception ;
	
	UserUpdatedDto loginUser(String username, String password) throws Exception;
	
	boolean updateInfo(User user, int userId);
	
	User getInfo(int userId);
	
	List<User> searchUser(int userId, String keyword);
}
