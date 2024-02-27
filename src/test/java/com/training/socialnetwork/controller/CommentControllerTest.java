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

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.request.comment.CommentCreateRequestDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentCreateResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdateResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.mapper.ObjectMapperUtils;
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
	
	@MockBean
	private ObjectMapperUtils objectMapper;

    @BeforeAll
	void setUp() throws Exception {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
	@Test
	public void createCommentSuccess() throws Exception {
		int userId = 1;
		CommentCreateRequestDto commentCreateDto = new CommentCreateRequestDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("create content");
		commentCreateDto.setPhotoId(1);

		CommentCreateResponseDto commentCreatedDto = new CommentCreateResponseDto();
		commentCreatedDto.setCommentId(1);
		commentCreatedDto.setPostId(1);
		commentCreatedDto.setUserId(1);
		commentCreatedDto.setUsername("test");
		commentCreatedDto.setContent("create content");
		commentCreatedDto.setPhotoId(1);

		when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
		when(commentService.createComment(anyInt(), any())).thenReturn(commentCreatedDto);
		
		String request = JSonHelper.toJson(commentCreateDto).orElse("");

		mockMvc.perform(post("/comment/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value(commentCreatedDto.getUsername()))
				.andExpect(jsonPath("$.commentId").value(commentCreatedDto.getCommentId()))
				.andExpect(jsonPath("$.postId").value(commentCreatedDto.getPostId()))
				.andExpect(jsonPath("$.content").value(commentCreatedDto.getContent()));
	}

	@Test
	public void createCommentFail() throws Exception {
		CommentCreateRequestDto commentCreateDto = new CommentCreateRequestDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("create content");
		commentCreateDto.setPhotoId(1);

		when(commentService.createComment(anyInt(), any())).thenThrow(new Exception());

		String request = JSonHelper.toJson(commentCreateDto).orElse("");

		mockMvc.perform(post("/comment/create").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void deleteCommentSuccess() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(delete("/comment/{commentId}", commentId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteCommentFail() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(delete("/comment/{commentId}", commentId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void updateCommentSuccess() throws Exception {
		String content = "update content";
		
		CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto();
		commentUpdateDto.setContent(content);
		commentUpdateDto.setPhotoId(1);
		
		CommentUpdateResponseDto commentUpdatedDto = new CommentUpdateResponseDto();
		commentUpdatedDto.setCommentId(1);
		commentUpdatedDto.setPostId(1);
		commentUpdatedDto.setUserId(1);
		commentUpdatedDto.setContent(content);
		commentUpdatedDto.setUpdateDate(new Date());

		String request = JSonHelper.toJson(commentUpdateDto).orElse("");
		
		when(commentService.updateComment(any(), anyInt(), anyInt())).thenReturn(commentUpdatedDto);

		mockMvc.perform(put("/comment/update/{commentId}", 1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.APPLICATION_JSON)
				.content(request)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentUpdatedDto.getContent()));
	}

	@Test
	public void updateCommentFail() throws Exception {
		String content = "update content";

		CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto();
		commentUpdateDto.setContent(content);
		commentUpdateDto.setPhotoId(1);
		
		String request = JSonHelper.toJson(commentUpdateDto).orElse("");
		
		when(commentService.updateComment(any(), anyInt(), anyInt())).thenThrow(new Exception());

		mockMvc.perform(put("/comment/update/{commentId}", 1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.APPLICATION_JSON)
				.content(request)).andExpect(status().isInternalServerError());
	}

	@Test
	public void getCommentDetailSuccess() throws Exception {
		CommentDetailResponseDto commentDetailDto = new CommentDetailResponseDto();
		commentDetailDto.setContent("comment content");
		commentDetailDto.setCommentId(1);
		commentDetailDto.setUserId(1);
		commentDetailDto.setPostId(1);
		commentDetailDto.setPhotoId(1);

		when(commentService.getCommentDetail(anyInt())).thenReturn(commentDetailDto);

		mockMvc.perform(get("/comment/detail/{commentId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentDetailDto.getContent()));
	}
	
	@Test
	public void getCommentDetailFail() throws Exception {
		CommentDetailResponseDto commentDetailDto = new CommentDetailResponseDto();
		commentDetailDto.setContent("comment content");
		commentDetailDto.setCommentId(1);
		commentDetailDto.setUserId(1);
		commentDetailDto.setPostId(1);

		when(commentService.getCommentDetail(anyInt())).thenThrow(new Exception());

		mockMvc.perform(get("/comment/detail/{commentId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
}
