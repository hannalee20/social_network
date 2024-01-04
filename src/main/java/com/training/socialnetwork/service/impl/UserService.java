package com.training.socialnetwork.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.contanst.Constant;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserRegistedDto createUser(User user) throws Exception {
		if(userRepository.findByUsername(user.getUsername()) != null){
			throw new Exception(Constant.SERVER_ERROR);
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(Constant.ROLE_USER);
		user.setCreateDate(new Date());
		user.setUpdateDate(new Date());
		
		User userRegisted = userRepository.save(user);
		
		if (userRegisted != null) {
			return modelMapper.map(userRegisted, UserRegistedDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public UserUpdatedDto loginUser(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username);
		if(user != null && user.getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
			return modelMapper.map(user, UserUpdatedDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public boolean updateInfo(User user, int userId) {
		User userToUpdate = userRepository.findById(user.getUserId()).orElse(null);
		User loggedInUser = userRepository.findById(userId).orElse(null);
		
		if(userToUpdate == null || loggedInUser == null || userToUpdate.getUserId() != loggedInUser.getUserId()) {
			return false;
		}
		
		user.setUsername(userToUpdate.getUsername());
		user.setPassword(userToUpdate.getPassword());
		user.setEmail(userToUpdate.getEmail());
		user.setRole(userToUpdate.getRole());
		
		return userRepository.save(user) != null;
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
