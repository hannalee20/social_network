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
	
	@Override
	public UserEntity createUser(UserEntity userEntity) {
		if(userRepository.findByUsername(userEntity.getUsername()) != null){
			return null;
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
		String encryptPassword = encoder.encode(userEntity.getPassword());
		userEntity.setPassword(encryptPassword);
		return userRepository.save(userEntity);
	}

	@Override
	public boolean loginUser(String username, String password) {
		UserEntity userEntity = userRepository.findByUsername(username);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
		String encryptPassword = encoder.encode(userEntity.getPassword());
		if(userEntity != null && userEntity.getPassword().equals(encryptPassword)) {
			return true;
		}
		return false;
	}

	@Override
	public void updateInfo(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	@Override
	public UserEntity getInfo(int userId) {
		Optional<UserEntity> optional = userRepository.findById(userId);
		UserEntity userEntity = optional.get();
		return userEntity;
	}

}
