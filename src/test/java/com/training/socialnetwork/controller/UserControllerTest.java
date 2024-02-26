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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.request.user.UserForgotPasswordRequestDto;
import com.training.socialnetwork.dto.request.user.UserGetTokenRequestDto;
import com.training.socialnetwork.dto.request.user.UserLoginRequestDto;
import com.training.socialnetwork.dto.request.user.UserRegisterRequestDto;
import com.training.socialnetwork.dto.request.user.UserResetPasswordRequestDto;
import com.training.socialnetwork.dto.request.user.UserUpdateRequestDto;
import com.training.socialnetwork.dto.response.user.UserDetailResponseDto;
import com.training.socialnetwork.dto.response.user.UserForgotPasswordResponseDto;
import com.training.socialnetwork.dto.response.user.UserRegisterResponseDto;
import com.training.socialnetwork.dto.response.user.UserReportResponseDto;
import com.training.socialnetwork.dto.response.user.UserSearchResponseDto;
import com.training.socialnetwork.dto.response.user.UserUpdateResponseDto;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.security.OtpUtils;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.service.impl.UserService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.mapper.ObjectMapperUtils;
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
	private ObjectMapperUtils objectMapper;

	@MockBean
	private UserService userService;
	
	@MockBean
	private CustomUserDetailService customUserDetailService;
	
	private String token;

	@BeforeAll
	void setUp() throws Exception {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		UserLoginRequestDto userLoginDto = new UserLoginRequestDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");
		
		when(userService.loginUser(any(), any())).thenReturn(true);
		when(otpUtils.generateOtp(any())).thenReturn(123456);
		String otpRequest = JSonHelper.toJson(userLoginDto).orElse("");
		
		mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(otpRequest))
				.andExpect(status().isOk()).andReturn();
		
		UserGetTokenRequestDto userTokenDto = new UserGetTokenRequestDto();
		userTokenDto.setUsername("test");
		userTokenDto.setPassword("123456");
		userTokenDto.setOtp(123456);
		
		String request = JSonHelper.toJson(userTokenDto).orElse("");
		
		token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzA2MjU5NjgyLCJleHAiOjE3MDYzNDYwODF9.sVl5ksy4pXHHU9Bdx41AoDzAvs9gc5v3-NlAJG8p7DQ";
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken("test", "123456"));
				
		when(otpUtils.getOtp("test")).thenReturn(123456);
		when(jwtUtils.generateToken(any(Authentication.class))).thenReturn(token);
		MvcResult tokenResult = mockMvc.perform(post("/user/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
		token = tokenResult.getResponse().getContentAsString().substring(8, 133);
	}

	@Test
	public void createValidUser() throws Exception {
		UserRegisterRequestDto userRegisterDto = new UserRegisterRequestDto();

		userRegisterDto.setUsername("test");
		userRegisterDto.setPassword("123456");
		userRegisterDto.setEmail("test@gmail.com");

		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword(bCryptPasswordEncoder.encode("123456"));
		user.setEmail("test@gmail.com");

		UserRegisterResponseDto userRegistedDto = new UserRegisterResponseDto();
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

		UserLoginRequestDto userLoginDto = new UserLoginRequestDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");
		
		String request = JSonHelper.toJson(userLoginDto).orElse("");
		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(true);
		
		mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk());
	}
	
	@Test
	public void loginFail() throws Exception {
		UserLoginRequestDto userLoginDto = new UserLoginRequestDto();
		
		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(false);
		String request = JSonHelper.toJson(userLoginDto).orElse("");
		
		mockMvc.perform(post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isBadRequest());
	}
	
	@Test
    public void testGetToken_Failure() throws Exception {
		UserGetTokenRequestDto userTokenDto = new UserGetTokenRequestDto();
		userTokenDto.setUsername("test");
		userTokenDto.setPassword("123456");
		userTokenDto.setOtp(123456);
		
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(otpUtils.getOtp(anyString())).thenReturn(123456);

        String request = JSonHelper.toJson(userTokenDto).orElse("");
        
        mockMvc.perform(post("/user/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Constant.INVALID));
    }
	
	@Test
	public void getUserInfoSuccess() throws Exception {
		int userId = 1;
		UserDetailResponseDto userDetailDto = new UserDetailResponseDto();
		userDetailDto.setUserId(1);
		userDetailDto.setUsername("test");
		userDetailDto.setRealName("Test");
		userDetailDto.setGender("female");
		userDetailDto.setBirthDate(LocalDate.now());
		userDetailDto.setEmail("test");
		userDetailDto.setAddress("hanoi");
		userDetailDto.setJob("developer");
		userDetailDto.setUniversity("test");
		userDetailDto.setAvatarUrl("test");
		userDetailDto.setStatus("test");
		userDetailDto.setAbout("");
		
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
		UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
		userUpdateDto.setBirthDate("2020-02-02");
		userUpdateDto.setAddress("HCM");
		userUpdateDto.setUniversity("test");
		userUpdateDto.setJob("test");
		userUpdateDto.setStatus("test");
		userUpdateDto.setAbout("");
		
		UserUpdateResponseDto userUpdatedDto = new UserUpdateResponseDto();
		userUpdatedDto.setUserId(1);
		userUpdatedDto.setBirthDate(LocalDate.now());
		userUpdatedDto.setSex("female");
		userUpdatedDto.setEmail("test");
		userUpdatedDto.setRealName("test");
		userUpdatedDto.setAddress("hanoi");
		userUpdatedDto.setJob("developer");
		userUpdatedDto.setUniversity("test");
		userUpdatedDto.setAvatar(1);
		userUpdatedDto.setStatus("test");
		userUpdatedDto.setAbout("");
		
		when(userService.updateInfo(any(), anyInt(), anyInt())).thenReturn(userUpdatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		String request = JSonHelper.toJson(userUpdateDto).orElse("");
		mockMvc.perform(patch("/user/update/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.address").value(userUpdatedDto.getAddress()))
				.andExpect(jsonPath("$.job").value(userUpdatedDto.getJob()));
	}
	
	@Test
	public void updateUserFail() throws Exception {
		int userId = 1;
		
		UserUpdateRequestDto userUpdateDto = new UserUpdateRequestDto();
		userUpdateDto.setBirthDate("2020-02-02");
		userUpdateDto.setAddress("HCM");
		
		when(userService.updateInfo(any(), anyInt(), anyInt())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		String request = JSonHelper.toJson(userUpdateDto).orElse("");
		mockMvc.perform(patch("/user/update/{userId}", userId)
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void searchUserSuccess() throws Exception {
		String keyword = "test";
		
		UserSearchResponseDto userSearchDto1 = new UserSearchResponseDto();
		userSearchDto1.setUserId(4);
		userSearchDto1.setUsername("test1");
		userSearchDto1.setFriendStatus("Send request");
		
		UserSearchResponseDto userSearchDto2 = new UserSearchResponseDto();
		userSearchDto2.setUserId(2);
		userSearchDto2.setUsername("test2");
		userSearchDto2.setFriendStatus("Friend");
		
		UserSearchResponseDto userSearchDto3 = new UserSearchResponseDto();
		userSearchDto3.setUserId(3);
		userSearchDto3.setUsername("test3");
		userSearchDto3.setFriendStatus("Sent request");
		
		List<UserSearchResponseDto> userSearchList = new ArrayList<>();
		userSearchList.add(userSearchDto1);
		userSearchList.add(userSearchDto2);
		userSearchList.add(userSearchDto3);
		
		Page<UserSearchResponseDto> userPage = new PageImpl<UserSearchResponseDto>(userSearchList);
		
		when(userService.searchUser(anyInt(), any(), any())).thenReturn(userPage);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);
		
		mockMvc.perform(get("/user/search")
				.header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.param("keyword", keyword))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.userList", hasSize(3)));
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
		UserForgotPasswordRequestDto userForgotPasswordDto = new UserForgotPasswordRequestDto();
		userForgotPasswordDto.setEmail("test@gmail.com");
		
		UserForgotPasswordResponseDto userForgotPasswordResponseDto = new UserForgotPasswordResponseDto();
		userForgotPasswordResponseDto.setToken("12345678");
		
		when(userService.forgotPassword(any())).thenReturn(userForgotPasswordResponseDto);
		
		String request = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void forgotPasswordFail() throws Exception {
		UserForgotPasswordRequestDto userForgotPasswordDto = new UserForgotPasswordRequestDto();
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
		UserForgotPasswordRequestDto userForgotPasswordDto = new UserForgotPasswordRequestDto();
		userForgotPasswordDto.setEmail("test@gmail.com");
		
		UserForgotPasswordResponseDto userForgotPasswordResponseDto = new UserForgotPasswordResponseDto();
		userForgotPasswordResponseDto.setToken("12345678");
		
		when(userService.forgotPassword(any())).thenReturn(userForgotPasswordResponseDto);
		
		String requestForgotPassword = JSonHelper.toJson(userForgotPasswordDto).orElse("");
		
		MvcResult tokenResult = mockMvc.perform(post("/user/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestForgotPassword))
				.andExpect(status().isOk()).andReturn();
		
		String tokenResetPassword = tokenResult.getResponse().getContentAsString().substring(10, 18);
		UserResetPasswordRequestDto userResetPasswordDto = new UserResetPasswordRequestDto();
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
		UserResetPasswordRequestDto userResetPasswordDto = new UserResetPasswordRequestDto();
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
		UserReportResponseDto userReportDto = new UserReportResponseDto();
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