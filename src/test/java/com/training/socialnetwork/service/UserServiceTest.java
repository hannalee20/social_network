package com.training.socialnetwork.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.training.socialnetwork.dto.request.user.UserLoginDto;
import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserTokenDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.security.OtpUtils;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
//@WebMvcTest(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public OtpUtils otpUtils;
	
	@Mock
	private Authentication authentication;

	@Mock
	private AuthenticationManager authenticationManager;

//	@Mock
//	private ObjectMapper objectMapper;
//	
//	@Mock
//	private ModelMapper modelMapper;

	@Mock
	UserRepository userRepository;

//	@Mock
//	RoleRepository roleRepository;

	@MockBean
	IUserService userService;

//	@MockBean
//	UserController controller;

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
		String request = JSonHelper.toJson(userRegisterDto).orElse("");
		String expectedResponse = JSonHelper.toJson(userRegistedDto).orElse("");

		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isCreated())
//				.andExpect(content().json(expectedResponse));
				.andExpect(jsonPath("$.userId").value(userRegistedDto.getUserId()))
				.andExpect(jsonPath("$.username").value(userRegistedDto.getUsername()))
				.andExpect(jsonPath("$.email").value(userRegistedDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userRegistedDto.getRole()));
	}

	@Test
	public void loginSuccess() throws Exception {

		UserLoginDto userLoginDto = new UserLoginDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");
		
		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(true);
		String request = JSonHelper.toJson(userLoginDto).orElse("");
		
		mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk());
//				.andExpect(jsonPath("$.otp").value(String.valueOf(otp)));
	}
	
	@Test
	public void loginFail() throws Exception {
		UserLoginDto userLoginDto = new UserLoginDto();
		
		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(false);
		String request = JSonHelper.toJson(userLoginDto).orElse("");
		
		mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(Constant.INVALID_USERNAME_OR_PASSWORD));
	}
	
	@Test
	public void getTokenSuccess() throws Exception {
		UserTokenDto userTokenDto = new UserTokenDto();
		userTokenDto.setUsername("test");
		userTokenDto.setPassword("123456");
		String request = JSonHelper.toJson(userTokenDto).orElse("");
		userTokenDto.setOtp(231453);
		
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userTokenDto.getUsername(), userTokenDto.getPassword())))
				.thenReturn(authentication);
		mockMvc.perform(post("/user/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk());
//				.andExpect(content().json(Constant.INVALID_USERNAME_OR_PASSWORD));
	}
	
}