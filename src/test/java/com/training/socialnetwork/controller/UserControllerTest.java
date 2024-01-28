package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.user.CustomUserDetail;
import com.training.socialnetwork.dto.request.user.UserForgotPasswordDto;
import com.training.socialnetwork.dto.request.user.UserLoginDto;
import com.training.socialnetwork.dto.request.user.UserRegisterDto;
import com.training.socialnetwork.dto.request.user.UserResetPasswordDto;
import com.training.socialnetwork.dto.request.user.UserTokenDto;
import com.training.socialnetwork.dto.request.user.UserUpdateDto;
import com.training.socialnetwork.dto.response.user.UserDetailDto;
import com.training.socialnetwork.dto.response.user.UserRegistedDto;
import com.training.socialnetwork.dto.response.user.UserReportDto;
import com.training.socialnetwork.dto.response.user.UserSearchDto;
import com.training.socialnetwork.dto.response.user.UserUpdatedDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@MockBean
	private Authentication authentication;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserDetails userDetails;

	@MockBean
	private IUserService userService;
	
	@MockBean
	private CustomUserDetailService customUserDetailService;
	
	private String token;

	@BeforeAll
	void setUp() throws Exception {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
		UserLoginDto userLoginDto = new UserLoginDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");
		
		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(true);
		String otpRequest = JSonHelper.toJson(userLoginDto).orElse("");
		
		MvcResult result = mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(otpRequest))
				.andExpect(status().isOk()).andReturn();
		
		String otp = result.getResponse().getContentAsString();
		UserTokenDto userTokenDto = new UserTokenDto();
		userTokenDto.setUsername("test");
		userTokenDto.setPassword("123456");
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		CustomUserDetail customUserDetail = new CustomUserDetail(1, "test", "123456", authorities);
		userTokenDto.setOtp(Integer.valueOf(otp.substring(8, 14)));
		
		String request = JSonHelper.toJson(userTokenDto).orElse("");
		
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userTokenDto.getUsername(), userTokenDto.getPassword())))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(customUserDetail);
		
		MvcResult tokenResult = mockMvc.perform(post("/user/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
		token = tokenResult.getResponse().getContentAsString().substring(8, 133);
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
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedResponse));
//				.andDo(print())
//				.andExpect(jsonPath("$.username").value(userRegistedDto.getUserId()))
//				.andExpect(jsonPath("$.username").value(userRegistedDto.getUsername()))
//				.andExpect(jsonPath("$.email").value(userRegistedDto.getEmail()))
//				.andExpect(jsonPath("$.role").value(userRegistedDto.getRole()));
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
				.andExpect(status().isBadRequest());
//				.andExpect(jsonPath("$.message").value(Constant.INVALID_USERNAME_OR_PASSWORD));
	}
	
	@Test
	public void getInfoUserSuccess() throws Exception {
		int userId = 1;
		UserDetailDto userDetailDto = new UserDetailDto();
		userDetailDto.setUserId(1);
		userDetailDto.setRealName("Test");
		userDetailDto.setGender("female");
		
		when(userService.getInfo(anyInt())).thenReturn(userDetailDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		String request = JSonHelper.toJson(userId).orElse("");
		mockMvc.perform(get("/user/detail/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
		
	}
	
	@Test
	public void updateUserSuccess() throws Exception {
		
		int userId = 1;
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setBirthDate(new Date());
		userUpdateDto.setAddress("HCM");
		
		MultipartFile avatar = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
		
		UserUpdatedDto userUpdatedDto = new UserUpdatedDto();

		when(userService.updateInfo(userUpdateDto, avatar, userId, loggedInUserId)).thenReturn(userUpdatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("userUpdateDto", userUpdateDto);
		requestBody.putIfAbsent("avatar", avatar);
		requestBody.putIfAbsent("userId", userId);
		
		String request2 = JSonHelper.toJson(requestBody).orElse("");
		mockMvc.perform(patch("/user/update/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.content(request2))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void searchUserTest() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		String keyword = "test";
		
		UserSearchDto userSearchDto1 = new UserSearchDto();
		userSearchDto1.setUserId(3);
		userSearchDto1.setUsername("test1");
		userSearchDto1.setFriendStatus(2);
		
		UserSearchDto userSearchDto2 = new UserSearchDto();
		userSearchDto2.setUserId(2);
		userSearchDto2.setUsername("test2");
		userSearchDto2.setFriendStatus(1);
		
		UserSearchDto userSearchDto3 = new UserSearchDto();
		userSearchDto3.setUserId(3);
		userSearchDto3.setUsername("test3");
		userSearchDto3.setFriendStatus(0);
		
		List<UserSearchDto> userSearchList = new ArrayList<>();
		userSearchList.add(userSearchDto1);
		userSearchList.add(userSearchDto2);
		userSearchList.add(userSearchDto3);
		
		when(userService.searchUser(loggedInUserId, keyword)).thenReturn(userSearchList);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/search")
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.param("keyword", keyword))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void forgotPasswordSuccess() throws Exception {
		String email = "test@gmail.com";
		UserForgotPasswordDto userForgotPasswordDto = new UserForgotPasswordDto();
		userForgotPasswordDto.setEmail(email);
		
		when(userService.forgotPassword(email)).thenReturn(anyString());
		
		String request = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void resetPasswordSuccess() throws Exception {
		String email = "test@gmail.com";
		UserForgotPasswordDto userForgotPasswordDto = new UserForgotPasswordDto();
		userForgotPasswordDto.setEmail(email);
		
		String tokenForgotPassword = UUID.randomUUID().toString();
		when(userService.forgotPassword(email)).thenReturn(tokenForgotPassword);
		
		String requestForgotPassword = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		MvcResult tokenResult = mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestForgotPassword))
				.andExpect(status().isOk()).andReturn();
		
		String tokenResetPassword = tokenResult.getResponse().getContentAsString().substring(48);
		UserResetPasswordDto userResetPasswordDto = new UserResetPasswordDto();
		userResetPasswordDto.setToken(tokenResetPassword);
		userResetPasswordDto.setNewPassword("newpassword");
		
		when(userService.resetPassword(userResetPasswordDto.getToken(), userResetPasswordDto.getNewPassword())).thenReturn(Constant.RESET_PASSWORD_SUCCESSFULLY);
		String requestResetPassword = JSonHelper.toJson(userResetPasswordDto).orElse("");
		
		mockMvc.perform(put("/user/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestResetPassword))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void getReportUserSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		
		UserReportDto userReportDto = new UserReportDto();
		userReportDto.setCommentCount(1);
		userReportDto.setFriendCount(2);
		userReportDto.setLikeCount(3);
		userReportDto.setPostCount(4);
		
		
		when(userService.getReportUser(loggedInUserId)).thenReturn(userReportDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/export-report")
				.header("AUTHORIZATION", "Bearer " + token))
				.andExpect(status().isOk()).andReturn();
	}
}