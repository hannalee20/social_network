package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.user.CustomUserDetail;
import com.training.socialnetwork.dto.request.user.UserLoginDto;
import com.training.socialnetwork.dto.request.user.UserTokenDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ICommentService commentService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private Authentication authentication;

	@MockBean
	private UserDetails userDetails;

	@MockBean
	private IUserService userService;

	@MockBean
	private CustomUserDetailService customUserDetailService;

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
	public void createCommentSuccess() throws Exception {
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

		when(commentService.createComment(anyInt(), any(), any())).thenReturn(commentCreatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("commentCreateDto", commentCreateDto);
		requestBody.putIfAbsent("photo", photo);

		String request = JSonHelper.toJson(requestBody).orElse("");

		mockMvc.perform(post("/comment/create").header("AUTHORIZATION", "Bearer " + token)
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

		when(commentService.createComment(anyInt(), any(), any())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("commentCreateDto", commentCreateDto);
		requestBody.putIfAbsent("photo", photo);

		String request = JSonHelper.toJson(requestBody).orElse("");

		mockMvc.perform(post("/comment/create").header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.MULTIPART_FORM_DATA).content(request))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void deleteCommentSuccess() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(delete("/comment/delete/{commentId}", commentId).header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isOk()).andExpect(content().string(Constant.DELETE_SUCCESSFULLY));
	}

	@Test
	public void deleteCommentFail() throws Exception {
		int commentId = 1;

		when(commentService.deleteComment(anyInt(), anyInt())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(delete("/comment/delete/{commentId}", commentId).header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).param("commentId", Integer.toString(commentId)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void updateCommentSuccess() throws Exception {
		String content = "update content";
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());

		CommentUpdatedDto commentUpdatedDto = new CommentUpdatedDto();
		commentUpdatedDto.setContent(content);
		commentUpdatedDto.setPhotoUrl(photo1.getOriginalFilename());

		when(commentService.updateComment(any(), any(), anyInt(), anyInt())).thenReturn(commentUpdatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/comment/update/{commentId}", 1).file(photo1)
				.header("AUTHORIZATION", "Bearer " + token).contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentUpdatedDto.getContent()))
				.andExpect(jsonPath("$.photoUrl").value(commentUpdatedDto.getPhotoUrl()));
	}

	@Test
	public void updateCommentFail() throws Exception {
		String content = "update content";
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());

		when(commentService.updateComment(any(), any(), anyInt(), anyInt())).thenThrow(new Exception());
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/comment/update/{commentId}", 1).file(photo1)
				.header("AUTHORIZATION", "Bearer " + token).contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isInternalServerError());
	}

	@Test
	public void getCommentDetailSuccess() throws Exception {
		CommentDetailDto commentDetailDto = new CommentDetailDto();
		commentDetailDto.setContent("comment content");
		commentDetailDto.setCommentId(1);
		commentDetailDto.setUserId(1);
		commentDetailDto.setPostId(1);

		when(commentService.getCommentDetail(anyInt())).thenReturn(commentDetailDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(get("/comment/detail/{commentId}", 1).header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value(commentDetailDto.getContent()));
	}
}
