package com.training.socialnetwork.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
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
import com.training.socialnetwork.security.OtpUtils;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.service.impl.UserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@WebMvcTest(UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@MockBean
	private Authentication authentication;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private UserDetails userDetails;
	
	@MockBean
	private JwtUtils jwtUtils;
	
	@MockBean
	private OtpUtils otpUtils;

	@MockBean
	private UserService userService;
	
	@MockBean
	private CustomUserDetailService customUserDetailService;
	
	private String token;

	@BeforeAll
	void setUp() throws Exception {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		UserLoginDto userLoginDto = new UserLoginDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");
		
		int otp1 = 643876;
		when(userService.loginUser(any(), any())).thenReturn(true);
		when(otpUtils.generateOtp(any())).thenReturn(otp1);
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
		token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2MjU5NjgyLCJleHAiOjE3MDYzNDYwODF9.sVl5ksy4pXHHU9Bdx41AoDzAvs9gc5v3-NlAJG8p7DQ";
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userTokenDto.getUsername(), userTokenDto.getPassword())))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(customUserDetail);
		when(otpUtils.getOtp(any())).thenReturn(otp1);
		when(jwtUtils.generateToken(any())).thenReturn(token);
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

		when(userService.createUser(any())).thenReturn(userRegistedDto);
		String request = JSonHelper.toJson(userRegisterDto).orElse("");
		
		mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isCreated())
				.andDo(print())
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
				.andExpect(content().string(Constant.INVALID_USERNAME_OR_PASSWORD));
	}
	
	@Test
	public void getUserInfoSuccess() throws Exception {
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
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.userId").value(1))
				.andExpect(jsonPath("$.realName").value("Test"))
				.andExpect(jsonPath("$.gender").value("female"));
	}
	
	@Test
	public void getUserInfoFail() throws Exception {
		int userId = 1;
		
		when(userService.getInfo(anyInt())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		String request = JSonHelper.toJson(userId).orElse("");
		mockMvc.perform(get("/user/detail/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void updateUserSuccess() throws Exception {
		int userId = 1;
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setBirthDate(new Date());
		userUpdateDto.setAddress("HCM");
		
		MultipartFile avatar = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
		
		UserUpdatedDto userUpdatedDto = new UserUpdatedDto();
		userUpdatedDto.setAddress("hanoi");
		userUpdatedDto.setJob("developer");
		
		when(userService.updateInfo(any(), any(), anyInt(), anyInt())).thenReturn(userUpdatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("userUpdateDto", userUpdateDto);
		requestBody.putIfAbsent("avatar", avatar);
		requestBody.putIfAbsent("userId", userId);
		
		String request = JSonHelper.toJson(requestBody).orElse("");
		mockMvc.perform(patch("/user/update/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.content(request))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.address").value(userUpdatedDto.getAddress()))
				.andExpect(jsonPath("$.job").value(userUpdatedDto.getJob()));
	}
	
	@Test
	public void updateUserFail() throws Exception {
		int userId = 1;
		
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setBirthDate(new Date());
		userUpdateDto.setAddress("HCM");
		
		MultipartFile avatar = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
		
		when(userService.updateInfo(any(), any(), anyInt(), anyInt())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("userUpdateDto", userUpdateDto);
		requestBody.putIfAbsent("avatar", avatar);
		requestBody.putIfAbsent("userId", userId);
		
		String request = JSonHelper.toJson(requestBody).orElse("");
		mockMvc.perform(patch("/user/update/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.content(request))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void searchUserSuccess() throws Exception {
		String keyword = "test";
		
		UserSearchDto userSearchDto1 = new UserSearchDto();
		userSearchDto1.setUserId(4);
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
		
		when(userService.searchUser(anyInt(), any(), any())).thenReturn(userSearchList);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/search")
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.param("keyword", keyword))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$", hasSize(3)));
	}
	
	@Test
	public void searchUserFail() throws Exception {
		String keyword = "test";
		
		when(userService.searchUser(anyInt(), any(), any())).thenThrow(new RuntimeException());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/search")
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.param("keyword", keyword))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void forgotPasswordSuccess() throws Exception {
		UserForgotPasswordDto userForgotPasswordDto = new UserForgotPasswordDto();
		userForgotPasswordDto.setEmail("test@gmail.com");
		
		when(userService.forgotPassword(any())).thenReturn(anyString());
		
		String request = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void forgotPasswordFail() throws Exception {
		UserForgotPasswordDto userForgotPasswordDto = new UserForgotPasswordDto();
		userForgotPasswordDto.setEmail("test@gmail.com");
		
		when(userService.forgotPassword(any())).thenThrow(new Exception());
		
		String request = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void resetPasswordSuccess() throws Exception {
		UserForgotPasswordDto userForgotPasswordDto = new UserForgotPasswordDto();
		userForgotPasswordDto.setEmail("test@gmail.com");
		
		String tokenForgotPassword = UUID.randomUUID().toString();
		when(userService.forgotPassword(any())).thenReturn(tokenForgotPassword);
		
		String requestForgotPassword = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		MvcResult tokenResult = mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestForgotPassword))
				.andExpect(status().isOk()).andReturn();
		
		String tokenResetPassword = tokenResult.getResponse().getContentAsString().substring(48);
		UserResetPasswordDto userResetPasswordDto = new UserResetPasswordDto();
		userResetPasswordDto.setToken(tokenResetPassword);
		userResetPasswordDto.setNewPassword("newpassword");
		
		when(userService.resetPassword(any(), any())).thenReturn(Constant.RESET_PASSWORD_SUCCESSFULLY);
		String requestResetPassword = JSonHelper.toJson(userResetPasswordDto).orElse("");
		
		mockMvc.perform(put("/user/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestResetPassword))
				.andExpect(status().isOk())
				.andExpect(content().string(Constant.RESET_PASSWORD_SUCCESSFULLY));
	}
	
	@Test
	public void resetPasswordFail() throws Exception {
		UserResetPasswordDto userResetPasswordDto = new UserResetPasswordDto();
		userResetPasswordDto.setToken("wrong token");
		userResetPasswordDto.setNewPassword("newpassword");
		
		when(userService.resetPassword(any(), any())).thenThrow(new Exception(Constant.TOKEN_INVALID));
		String requestResetPassword = JSonHelper.toJson(userResetPasswordDto).orElse("");
		
		mockMvc.perform(put("/user/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestResetPassword))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void getReportUserSuccess() throws Exception {
		UserReportDto userReportDto = new UserReportDto();
		userReportDto.setCommentCount(1);
		userReportDto.setFriendCount(2);
		userReportDto.setLikeCount(3);
		userReportDto.setPostCount(4);
		
		
		when(userService.getReportUser(anyInt())).thenReturn(userReportDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/export-report")
				.header("AUTHORIZATION", "Bearer " + token))
				.andExpect(status().isOk());
	}
}