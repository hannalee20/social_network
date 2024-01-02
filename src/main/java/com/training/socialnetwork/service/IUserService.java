package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.entity.User;

public interface IUserService {
	
	User createUser(User user);
	
	boolean loginUser(String username, String password);
	
	boolean updateInfo(User user, int userId);
	
	User getInfo(int userId);
	
	List<User> searchUser(int userId, String keyword);
}
