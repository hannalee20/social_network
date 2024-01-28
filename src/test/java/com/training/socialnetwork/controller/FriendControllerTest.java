package com.training.socialnetwork.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.training.socialnetwork.dto.request.user.CustomUserDetail;
import com.training.socialnetwork.dto.request.user.UserLoginDto;
import com.training.socialnetwork.dto.request.user.UserTokenDto;
import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtils jwtUtils;

	@MockBean
	private IFriendService friendService;

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

	@MockBean
	private IPostService postService;

	private String token;

	@BeforeAll
	void setUp() throws Exception {
		UserLoginDto userLoginDto = new UserLoginDto();
		userLoginDto.setUsername("test");
		userLoginDto.setPassword("123456");

		when(userService.loginUser(userLoginDto.getUsername(), userLoginDto.getPassword())).thenReturn(true);
		String otpRequest = JSonHelper.toJson(userLoginDto).orElse("");

		MvcResult result = mockMvc
				.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(otpRequest))
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

		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userTokenDto.getUsername(), userTokenDto.getPassword())))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(customUserDetail);

		MvcResult tokenResult = mockMvc
				.perform(post("/user/token").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isOk()).andReturn();
		token = tokenResult.getResponse().getContentAsString().substring(8, 133);
	}

	@Test
	public void getFriendListSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);

		FriendListDto friendListDto = new FriendListDto();
		friendListDto.setFriendId(1);
		friendListDto.setUserId(2);
		friendListDto.setUsername("test");

		List<FriendListDto> friendList = new ArrayList<>();
		friendList.add(friendListDto);

		when(friendService.findAllFriendWithStatus(loggedInUserId, null)).thenReturn(friendList);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(get("/friend/all-friends").header("AUTHORIZATION", "Bearer " + token))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void createFriendRequestSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		int userId2 = 2;

		when(friendService.createFriendRequest(loggedInUserId, userId2)).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(post("/friend/add-request").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("userId2", Integer.toString(userId2)))
				.andExpect(status().isCreated()).andReturn();
	}

	@Test
	public void acceptFriendSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		int userId1 = 2;

		when(friendService.acceptFriendRequest(userId1, loggedInUserId)).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(post("/friend/accept-request").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("userId1", Integer.toString(userId1)))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void refuseFriendRequestSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		int userId1 = 2;

		when(friendService.refuseFriendRequest(userId1, loggedInUserId)).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(post("/friend/refuse-request").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("userId1", Integer.toString(userId1)))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void removeFriend() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		int userId1 = 2;

		when(friendService.unfriend(userId1, loggedInUserId)).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(post("/friend/remove-friend").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("userId1", Integer.toString(userId1)))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void getFriendRequestListSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);

		FriendRequestDto friendRequestDto = new FriendRequestDto();
		friendRequestDto.setUserId(2);
		friendRequestDto.setUsername("test");

		List<FriendRequestDto> friendRequestList = new ArrayList<>();
		friendRequestList.add(friendRequestDto);

		when(friendService.findAllAddFriendRequest(loggedInUserId, null)).thenReturn(friendRequestList);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(get("/friend/all-friend-request").header("AUTHORIZATION", "Bearer " + token))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void removeFriendRequestSuccess() throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(token);
		int userId1 = 2;

		when(friendService.removeFriendRequest(userId1, loggedInUserId)).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(post("/friend/remove-friend-request").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("userId1", Integer.toString(userId1)))
				.andExpect(status().isOk()).andReturn();
	}
}
