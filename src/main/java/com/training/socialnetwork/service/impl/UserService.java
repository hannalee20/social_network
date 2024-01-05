package com.training.socialnetwork.service.impl;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserLoggedInDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserRegistedDto createUser(UserRegisterDto userRegisterDto) throws Exception {
		if(userRepository.findByUsername(userRegisterDto.getUsername()) != null){
			throw new Exception(Constant.SERVER_ERROR);
		}
		User user = new User();
		user.setUsername(userRegisterDto.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
		user.setEmail(userRegisterDto.getEmail());
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
	public UserLoggedInDto loginUser(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username);
		if(user != null && user.getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
			return modelMapper.map(user, UserLoggedInDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public UserUpdatedDto updateInfo(UserUpdateDto userUpdateDto, int userId) throws Exception {
		User userToUpdate = userRepository.findById(userUpdateDto.getUserId()).orElse(null);
		User loggedInUser = userRepository.findById(userId).orElse(null);
		
		if(userToUpdate == null || loggedInUser == null || userToUpdate.getUserId() != loggedInUser.getUserId()) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		User user = modelMapper.map(userUpdateDto, User.class);
			
		user.setUsername(userToUpdate.getUsername());
		user.setPassword(userToUpdate.getPassword());
		user.setRole(userToUpdate.getRole());
		
		User userUpdated = userRepository.save(user);
		
		if (userUpdated != null) {
			return modelMapper.map(userUpdated, UserUpdatedDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public UserDetailDto getInfo(int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);
		
		if(user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		return modelMapper.map(user, UserDetailDto.class);
	}

	@Override
	public List<User> searchUser(int userId, String keyword) {
		return userRepository.findAllUserLike(userId, keyword);
	}
}
