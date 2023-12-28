package com.training.socialnetwork.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.UserEntity;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserEntity createUser(UserEntity userEntity) {
		if(userRepository.findByUsername(userEntity.getUsername()) != null){
			return null;
		}
		userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
		userEntity.setRole(1);
		return userRepository.save(userEntity);
	}

	@Override
	public boolean loginUser(String username, String password) {
		UserEntity userEntity = userRepository.findByUsername(username);
		if(userEntity != null && userEntity.getPassword().equals(bCryptPasswordEncoder.encode(userEntity.getPassword()))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateInfo(UserEntity userEntity) {
		if(userRepository.findById(userEntity.getUserId()) != null) {
			userRepository.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public UserEntity getInfo(int userId) {
		Optional<UserEntity> optional = userRepository.findById(userId);
		UserEntity userEntity = optional.get();
		return userEntity;
	}

}
