package com.training.socialnetwork.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User createUser(User user) {
		if(userRepository.findByUsername(user.getUsername()) != null){
			return null;
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(1);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		return userRepository.save(user);
	}

	@Override
	public boolean loginUser(String username, String password) {
		User user = userRepository.findByUsername(username);
		if(user != null && user.getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateInfo(User user) {
		if(userRepository.findById(user.getUserId()) != null) {
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public User getInfo(int userId) {
		Optional<User> optional = userRepository.findById(userId);
		User user = optional.get();
		return user;
	}

	@Override
	public List<User> searchUser(int userId, String keyword) {
		return userRepository.findAllUserLike(userId, keyword);
	}
}
