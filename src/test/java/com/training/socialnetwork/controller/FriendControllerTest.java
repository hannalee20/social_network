package com.training.socialnetwork.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.response.friend.FriendListResponseDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestListResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IFriendService;

@WebMvcTest(FriendController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private IFriendService friendService;
	
	@BeforeAll
	void setUp() throws Exception {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

	@Test
	public void getFriendListSuccess() throws Exception {
		FriendListResponseDto friendListDto = new FriendListResponseDto();
		friendListDto.setFriendId(1);
		friendListDto.setUserId(2);
		friendListDto.setUsername("test");

		List<FriendListResponseDto> friendList = new ArrayList<>();
		friendList.add(friendListDto);

		Page<FriendListResponseDto> page = new PageImpl<FriendListResponseDto>(friendList);
		when(friendService.findAllFriendWithStatus(anyInt(), any())).thenReturn(page);

		mockMvc.perform(get("/friend/all-friends").header("Authorization", "Bearer dummyToken"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.friendList", hasSize(1)));
	}
	
	@Test
	public void getFriendListFail() throws Exception {
		when(friendService.findAllFriendWithStatus(anyInt(), any())).thenThrow(new RuntimeException());

		mockMvc.perform(get("/friend/all-friends").header("Authorization", "Bearer dummyToken"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void createFriendRequestSuccess() throws Exception {
		int recievedUserId = 2;
		
		int status = 1;

		when(friendService.createFriendRequest(anyInt(), anyInt())).thenReturn(status);

		mockMvc.perform(post("/friend/add-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("recievedUserId", Integer.toString(recievedUserId)))
				.andExpect(status().isCreated());
	}

	@Test
	public void createFriendRequestFail() throws Exception {
		int recievedUserId = 2;

		when(friendService.createFriendRequest(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(post("/friend/add-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("recievedUserId", Integer.toString(recievedUserId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void acceptFriendSuccess() throws Exception {
		int sentUserId = 2;

		when(friendService.acceptFriendRequest(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/friend/accept-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("sentUserId", Integer.toString(sentUserId)))
				.andExpect(status().isOk());
	}

	@Test
	public void acceptFriendFail() throws Exception {
		int sentUserId = 2;

		when(friendService.acceptFriendRequest(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(post("/friend/accept-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("sentUserId", Integer.toString(sentUserId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void refuseFriendRequestSuccess() throws Exception {
		int sentUserId = 2;

		when(friendService.refuseFriendRequest(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/friend/refuse-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("sentUserId", Integer.toString(sentUserId)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void refuseFriendRequestFail() throws Exception {
		int sentUserId = 2;

		when(friendService.refuseFriendRequest(anyInt(), anyInt())).thenThrow(new RuntimeException());

		mockMvc.perform(post("/friend/refuse-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("sentUserId", Integer.toString(sentUserId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void removeFriendSuccess() throws Exception {
		int friendUserId = 2;

		when(friendService.unfriend(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/friend/remove-friend").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("friendUserId", Integer.toString(friendUserId)))
				.andExpect(status().isOk());
	}

	@Test
	public void removeFriendFail() throws Exception {
		int friendUserId = 2;

		when(friendService.unfriend(anyInt(), anyInt())).thenThrow(new RuntimeException());

		mockMvc.perform(post("/friend/remove-friend").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("friendUserId", Integer.toString(friendUserId)))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void getFriendRequestListSuccess() throws Exception {
		FriendRequestListResponseDto friendRequestDto = new FriendRequestListResponseDto();
		friendRequestDto.setUserId(2);
		friendRequestDto.setUsername("test");

		List<FriendRequestListResponseDto> friendRequestList = new ArrayList<>();
		friendRequestList.add(friendRequestDto);
		
		Page<FriendRequestListResponseDto> friendRequestPage = new PageImpl<FriendRequestListResponseDto>(friendRequestList);

		when(friendService.findAllAddFriendRequest(anyInt(), any())).thenReturn(friendRequestPage);

		mockMvc.perform(get("/friend/all-friend-request").header("Authorization", "Bearer dummyToken"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.friendList", hasSize(1)));
	}
	
	@Test
	public void getFriendRequestListFail() throws Exception {
		FriendRequestListResponseDto friendRequestDto = new FriendRequestListResponseDto();
		friendRequestDto.setUserId(2);
		friendRequestDto.setUsername("test");

		List<FriendRequestListResponseDto> friendRequestList = new ArrayList<>();
		friendRequestList.add(friendRequestDto);

		when(friendService.findAllAddFriendRequest(anyInt(), any())).thenThrow(new RuntimeException());

		mockMvc.perform(get("/friend/all-friend-request").header("Authorization", "Bearer dummyToken"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void removeFriendRequestSuccess() throws Exception {
		int recievedUserId = 2;

		when(friendService.removeFriendRequest(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/friend/remove-friend-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("recievedUserId", Integer.toString(recievedUserId)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void removeFriendRequestFail() throws Exception {
		int recievedUserId = 2;

		when(friendService.removeFriendRequest(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(post("/friend/remove-friend-request").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("recievedUserId", Integer.toString(recievedUserId)))
				.andExpect(status().isInternalServerError());
	}
}
