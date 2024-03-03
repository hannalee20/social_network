package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.training.socialnetwork.dto.request.post.PostCreateRequestDto;
import com.training.socialnetwork.dto.request.post.PostUpdateRequestDto;
import com.training.socialnetwork.dto.response.post.PostCreateResponseDto;
import com.training.socialnetwork.dto.response.post.PostDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostListResponseDto;
import com.training.socialnetwork.dto.response.post.PostUpdateResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.mapper.ObjectUtils;
import com.training.socialnetwork.utils.JSonHelper;

@WebMvcTest(PostController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private IPostService postService;

	@MockBean
	private ObjectUtils objectUtils;
	
	@MockBean
	private JwtUtils jwtUtils;

	@BeforeAll
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void createPostSuccess() throws Exception {
		String content = "create content";
		List<Integer> photoIdList = new ArrayList<>();
		photoIdList.add(1);
		photoIdList.add(2);
		
		PostCreateRequestDto postCreateDto = new PostCreateRequestDto();
		postCreateDto.setContent(content);
		postCreateDto.setPhotoIdList(photoIdList);

		PostCreateResponseDto postCreatedDto = new PostCreateResponseDto();
		postCreatedDto.setPostId(1);
		postCreatedDto.setUserId(1);
		postCreatedDto.setContent(content);
		postCreatedDto.setPhotoIdList(photoIdList);
		postCreatedDto.setUsername("test");

		String request = JSonHelper.toJson(postCreateDto).orElse("");
		when(postService.createPost(anyInt(), any())).thenReturn(postCreatedDto);

		mockMvc.perform(post("/post/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.postId").value(postCreatedDto.getPostId()))
				.andExpect(jsonPath("$.userId").value(postCreatedDto.getUserId()))
				.andExpect(jsonPath("$.content").value(postCreatedDto.getContent()))
				.andExpect(jsonPath("$.username").value(postCreatedDto.getUsername()));
	}

	@Test
	public void createPostFail() throws Exception {
		int userId = 1;
		String content = "Test post content";
		List<Integer> photoIdList = new ArrayList<>();
		photoIdList.add(1);
		photoIdList.add(2);
		
		PostCreateRequestDto postCreateDto = new PostCreateRequestDto();
		postCreateDto.setContent(content);
		postCreateDto.setPhotoIdList(photoIdList);
		
		String request = JSonHelper.toJson(postCreateDto).orElse("");
		
		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.createPost(anyInt(), any())).thenThrow(new Exception("Some error message"));

		mockMvc.perform(post("/post/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void createPostFail2() throws Exception {
		int userId = 1;
		String content = "Test post content";
		List<Integer> photoIdList = new ArrayList<>();
		photoIdList.add(1);
		photoIdList.add(2);
		
		PostCreateRequestDto postCreateDto = new PostCreateRequestDto();
		postCreateDto.setContent(content);
		postCreateDto.setPhotoIdList(photoIdList);
		
		String request = JSonHelper.toJson(postCreateDto).orElse("");
		
		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.createPost(anyInt(), any())).thenThrow(new CustomException(HttpStatus.NOT_FOUND, ""));

		mockMvc.perform(post("/post/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void getTimelineSuccess() throws Exception {
		List<PostListResponseDto> postList = new ArrayList<>();
		PostListResponseDto postListDto = new PostListResponseDto();
		postListDto.setContent("content");
		postListDto.setUserId(1);
		postListDto.setPostId(1);
		postListDto.setUsername("test");
		postListDto.setCommentCount(2);
		postListDto.setLikeCount(3);
		postListDto.setCreateDate(new Date());
		postListDto.setUpdateDate(new Date());
		postList.add(postListDto);

		Map<String, Object> result = new HashMap<>();
		result.put("postList", postListDto.getContent());
		
		when(postService.getTimeline(anyInt(), any())).thenReturn(result);

		mockMvc.perform(get("/post/timeline").header("Authorization", "Bearer dummyToken")).andExpect(status().isOk());
	}

	@Test
	public void getTimelineFail2() throws Exception {
		int userId = 1;

		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.getTimeline(anyInt(), any())).thenThrow(new RuntimeException("Some error message"));

		mockMvc.perform(get("/post/timeline").header("Authorization", "Bearer dummyToken"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void getPostDetailSuccess() throws Exception {
		PostDetailResponseDto postDetailDto = new PostDetailResponseDto();
		postDetailDto.setPostId(1);
		postDetailDto.setUserId(1);
		postDetailDto.setUsername("test");
		postDetailDto.setContent("content");
		postDetailDto.setCreateDate(new Date());

		when(postService.getPost(anyInt())).thenReturn(postDetailDto);

		mockMvc.perform(get("/post/detail/{postId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getPostDetailFail() throws Exception {
		int postId = 1;

		when(postService.getPost(anyInt())).thenThrow(new Exception("Some error message"));

		mockMvc.perform(get("/post/detail/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void getPostDetailFail2() throws Exception {
		int postId = 1;

		when(postService.getPost(anyInt())).thenThrow(new CustomException(HttpStatus.NOT_FOUND, ""));

		mockMvc.perform(get("/post/detail/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void updatePostSuccess() throws Exception {
		int postId = 1;
		String content = "update content";
		
		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		postUpdateDto.setContent(content);
		
		String request = JSonHelper.toJson(postUpdateDto).orElse("");

		PostUpdateResponseDto postUpdatedDto = new PostUpdateResponseDto();
		postUpdatedDto.setPostId(1);
		postUpdatedDto.setUserId(1);
		postUpdatedDto.setUsername("test");
		postUpdatedDto.setContent(content);
		postUpdatedDto.setCreateDate(new Date());
		postUpdatedDto.setUpdateDate(new Date());

		when(postService.updatePost(any(), anyInt(), anyInt())).thenReturn(postUpdatedDto);

		mockMvc.perform(put("/post/update/{postId}", postId).header("Authorization", "Bearer dummyToken").contentType(MediaType.APPLICATION_JSON)
				.content(request)).andExpect(status().isOk());
	}

	@Test
	public void updatePostFail() throws Exception {
		int userId = 1;
		int postId = 1;

		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		String request = JSonHelper.toJson(postUpdateDto).orElse("");
		
		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.updatePost(any(), anyInt(), anyInt())).thenThrow(new Exception("Some error message"));

		mockMvc.perform(put("/post/update/{postId}", postId).header("Authorization", "Bearer dummyToken").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void updatePostFail2() throws Exception {
		int userId = 1;
		int postId = 1;

		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		String request = JSonHelper.toJson(postUpdateDto).orElse("");
		
		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.updatePost(any(), anyInt(), anyInt())).thenThrow(new CustomException(HttpStatus.FORBIDDEN, ""));

		mockMvc.perform(put("/post/update/{postId}", postId).header("Authorization", "Bearer dummyToken").contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isForbidden());
	}

	@Test
	public void deletePostSuccess() throws Exception {
		int postId = 1;

		when(postService.deletePost(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(delete("/post/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePostFail() throws Exception {
		int userId = 1;
		int postId = 1;

		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.deletePost(anyInt(), anyInt())).thenThrow(new RuntimeException("Some error message"));

		mockMvc.perform(delete("/post/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void deletePostFail2() throws Exception {
		int userId = 1;
		int postId = 1;

		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(postService.deletePost(anyInt(), anyInt())).thenThrow(new CustomException(HttpStatus.NOT_FOUND, ""));

		mockMvc.perform(delete("/post/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isNotFound());
	}
}
