package com.training.socialnetwork.service;

import com.training.socialnetwork.entity.UserEntity;

public interface IUserService {
	UserEntity createUser(UserEntity userEntity);
	
	boolean loginUser(String username, String password);
	
	boolean updateInfo(UserEntity userEntity);
	
	public UserEntity getInfo(int userId);
}
