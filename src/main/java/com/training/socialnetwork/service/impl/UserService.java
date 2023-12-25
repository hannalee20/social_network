package com.training.socialnetwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.UserEntity;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserEntity createAccount(UserEntity userEntity) {
		if(userRepository.findByUsername(userEntity.getUsername()) != null){
			return null;
		}
		return userRepository.save(userEntity);
	}

}
