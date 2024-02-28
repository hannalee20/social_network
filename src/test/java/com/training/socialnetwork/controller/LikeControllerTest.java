package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ILikeService;
import com.training.socialnetwork.util.exception.CustomException;

@WebMvcTest(LikeController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private ILikeService likeService;

	@BeforeAll
	void setUp() throws Exception {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void addLikeSuccess() throws Exception {
		int postId = 1;

		when(likeService.addLikePost(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/like/add").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void addLikeFail() throws Exception {
		int postId = 1;

		when(likeService.addLikePost(anyInt(), anyInt())).thenThrow(new CustomException(HttpStatus.NOT_FOUND, ""));

		mockMvc.perform(post("/like/add").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	public void addLikeFail2() throws Exception {
		int postId = 1;

		when(likeService.addLikePost(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(post("/like/add").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isInternalServerError()).andReturn();
	}
	
	@Test
	public void unlikeSuccess() throws Exception {
		int postId = 1;

		when(likeService.addLikePost(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(post("/like/unlike").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void unlikeFail() throws Exception {
		int postId = 1;

		when(likeService.unlikePost(anyInt(), anyInt())).thenThrow(new CustomException(HttpStatus.NOT_FOUND, ""));

		mockMvc.perform(post("/like/unlike").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	public void unlikeFail2() throws Exception {
		int postId = 1;

		when(likeService.unlikePost(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(post("/like/unlike").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isInternalServerError()).andReturn();
	}
}
