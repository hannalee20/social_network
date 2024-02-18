package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@WebMvcTest(CommentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
    private ICommentService commentService;

	@MockBean
    private JwtUtils jwtUtils;

    @BeforeAll
	void setUp() throws Exception {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
	@Test
	public void createCommentSuccess() throws Exception {
		int userId = 1;
		CommentCreateDto commentCreateDto = new CommentCreateDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("create content");

		MockMultipartFile photo = new MockMultipartFile("data", "photo.png", "multipart/form-data",
				"some data".getBytes());

		CommentCreatedDto commentCreatedDto = new CommentCreatedDto();
		commentCreatedDto.setCommentId(1);
		commentCreatedDto.setPostId(1);
		commentCreatedDto.setUserId(1);
		commentCreatedDto.setUsername("test");
		commentCreatedDto.setContent("create content");
		commentCreatedDto.setPhotoUrl("photo.png");

		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(commentService.createComment(anyInt(), any())).thenReturn(commentCreatedDto);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("commentCreateDto", commentCreateDto);
		requestBody.putIfAbsent("photo", photo);

		String request = JSonHelper.toJson(requestBody).orElse("");

		mockMvc.perform(post("/comment/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.MULTIPART_FORM_DATA).content(request)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value(commentCreatedDto.getUsername()))
				.andExpect(jsonPath("$.commentId").value(commentCreatedDto.getCommentId()))
				.andExpect(jsonPath("$.postId").value(commentCreatedDto.getPostId()))
				.andExpect(jsonPath("$.content").value(commentCreatedDto.getContent()))
				.andExpect(jsonPath("$.photoUrl").value(commentCreatedDto.getPhotoUrl()));
	}

	@Test
	public void createCommentFail() throws Exception {
		CommentCreateDto commentCreateDto = new CommentCreateDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("create content");

		MockMultipartFile photo = new MockMultipartFile("data", "photo.png", "multipart/form-data",
				"some data".getBytes());

		when(commentService.createComment(anyInt(), any())).thenThrow(new Exception());

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("commentCreateDto", commentCreateDto);
		requestBody.putIfAbsent("photo", photo);

		String request = JSonHelper.toJson(requestBody).orElse("");

		mockMvc.perform(post("/comment/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.MULTIPART_FORM_DATA).content(request))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void deleteCommentSuccess() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(delete("/comment/delete/{commentId}", commentId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isOk()).andExpect(content().string(Constant.DELETE_SUCCESSFULLY));
	}

	@Test
	public void deleteCommentFail() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(delete("/comment/delete/{commentId}", commentId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void updateCommentSuccess() throws Exception {
		String content = "update content";
		CommentUpdatedDto commentUpdatedDto = new CommentUpdatedDto();
		commentUpdatedDto.setCommentId(1);
		commentUpdatedDto.setPostId(1);
		commentUpdatedDto.setUserId(1);
		commentUpdatedDto.setContent(content);
		commentUpdatedDto.setUpdateDate(new Date());

		when(commentService.updateComment(any(), anyInt(), anyInt())).thenReturn(commentUpdatedDto);

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/comment/update/{commentId}", 1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentUpdatedDto.getContent()));
	}

	@Test
	public void updateCommentFail() throws Exception {
		String content = "update content";

		when(commentService.updateComment(any(), anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/comment/update/{commentId}", 1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isInternalServerError());
	}

	@Test
	public void getCommentDetailSuccess() throws Exception {
		CommentDetailDto commentDetailDto = new CommentDetailDto();
		commentDetailDto.setContent("comment content");
		commentDetailDto.setCommentId(1);
		commentDetailDto.setUserId(1);
		commentDetailDto.setPostId(1);
		commentDetailDto.setPhotoUrl("test");

		when(commentService.getCommentDetail(anyInt())).thenReturn(commentDetailDto);

		mockMvc.perform(get("/comment/detail/{commentId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentDetailDto.getContent()));
	}
	
	@Test
	public void getCommentDetailFail() throws Exception {
		CommentDetailDto commentDetailDto = new CommentDetailDto();
		commentDetailDto.setContent("comment content");
		commentDetailDto.setCommentId(1);
		commentDetailDto.setUserId(1);
		commentDetailDto.setPostId(1);

		when(commentService.getCommentDetail(anyInt())).thenThrow(new Exception());

		mockMvc.perform(get("/comment/detail/{commentId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
}
