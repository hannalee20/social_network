package com.training.socialnetwork.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;
	
	@Test
	public void createValidUser() {
		UserRegisterDto userRegisterDto = new UserRegisterDto();
		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");
		
	}
}
