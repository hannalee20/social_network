package com.training.socialnetwork.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.socialnetwork.controller.UserController;
import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.RoleRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	RoleRepository roleRepository;
	
	@Mock
	IUserService userService;
	
	@MockBean
	UserController controller;
	
	@BeforeEach
	void setUp() {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}
	
	@Test
	public void createValidUser() throws Exception {
		UserRegisterDto userRegisterDto = new UserRegisterDto();
		
		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword(bCryptPasswordEncoder.encode("123456"));
		user.setEmail("test@gmail.com");
		
		UserRegistedDto userRegistedDto = new UserRegistedDto();
		userRegistedDto.setUserId(1);
		userRegistedDto.setUsername("test");
		userRegistedDto.setPassword(bCryptPasswordEncoder.encode("123456"));
		userRegistedDto.setEmail("test@gmail.com");
		userRegistedDto.setRole(Constant.ROLE_USER);
		
		when(userService.createUser(userRegisterDto)).thenReturn(userRegistedDto);
		String request = JSonHelper.toJson(userRegistedDto).orElse("");
		String expectedResponse = JSonHelper.toJson(userRegistedDto).orElse("");
		
		mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated())
				.andExpect(content().json(expectedResponse));
//		given(userService.createUser(any(UserRegisterDto.class))).willAnswer((invocation) -> invocation.getArgument(0));
//		when(userService.createUser(any(UserRegisterDto.class))).thenReturn(userRegisterDto);
//		ResultActions response = mockMvc.perform(post("/users/register")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(userRegisterDto)));
//		
//		response.andDo(print()).
//        	andExpect(status().isCreated());
//        .andExpect(jsonPath("$.username",
//                is(userRegisterDto.getUsername())))
//        .andExpect(jsonPath("$.lastName",
//                is(userRegisterDto.getPassword())))
//        .andExpect(jsonPath("$.email",
//                is(userRegisterDto.getEmail())));
	}
}
