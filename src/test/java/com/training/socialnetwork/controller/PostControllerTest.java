package com.training.socialnetwork.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.training.socialnetwork.dto.request.user.CustomUserDetail;
import com.training.socialnetwork.dto.request.user.UserLoginDto;
import com.training.socialnetwork.dto.request.user.UserTokenDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.service.IUserService;
import com.training.socialnetwork.service.impl.CustomUserDetailService;
import com.training.socialnetwork.utils.JSonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

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
	public void createPostSuccess() throws Exception {
		String content = "create content";
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());
		MockMultipartFile photo2 = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data",
				"some xml".getBytes());
		List<String> photoUrlList = new ArrayList<>();
		photoUrlList.add(photo1.getOriginalFilename());
		photoUrlList.add(photo2.getOriginalFilename());

		PostCreatedDto postCreatedDto = new PostCreatedDto();
		postCreatedDto.setPostId(1);
		postCreatedDto.setUserId(1);
		postCreatedDto.setContent(content);
		postCreatedDto.setPhotoUrl(photoUrlList);
		postCreatedDto.setUsername("test");

		when(postService.createPost(anyInt(), any(), any())).thenReturn(postCreatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/post/create").file(photo1).file(photo2)
				.header("AUTHORIZATION", "Bearer " + token).contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isCreated())
				.andExpect(jsonPath("$postId").value(postCreatedDto.getPostId()))
				.andExpect(jsonPath("$userId").value(postCreatedDto.getUserId()))
				.andExpect(jsonPath("$content").value(postCreatedDto.getContent()))
				.andExpect(jsonPath("$username").value(postCreatedDto.getUsername()));
	}

	@Test
	public void getTimelineSuccess() throws Exception {
		List<PostListDto> postList = new ArrayList<>();
		PostListDto postListDto = new PostListDto();
		postListDto.setUserId(1);
		postListDto.setPostId(1);
		postListDto.setUsername("test");
		postListDto.setCommentCount(2);
		postListDto.setLikeCount(3);
		
		when(postService.getTimeline(anyInt(), any())).thenReturn(postList);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(get("/post/timeline").header("AUTHORIZATION", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void getPostDetailSuccess() throws Exception {
		PostDetailDto postDetailDto = new PostDetailDto();

		when(postService.getPost(anyInt())).thenReturn(postDetailDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

//		String request = JSonHelper.toJson(postId).orElse("");

		mockMvc.perform(get("/post/detail/{postId}", 1).header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void updatePostSuccess() throws Exception {
		int postId = 1;
		String content = "update content";
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());
		MockMultipartFile photo2 = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data",
				"some xml".getBytes());

		PostUpdatedDto postUpdatedDto = new PostUpdatedDto();

		when(postService.updatePost(any(), any(), anyInt(), anyInt())).thenReturn(postUpdatedDto);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/post/update/{postId}", postId).file(photo1)
				.file(photo2).header("AUTHORIZATION", "Bearer " + token).contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isOk());
	}

	@Test
	public void deletePostSuccess() throws Exception {
		int postId = 1;

		when(postService.deletePost(anyInt(), anyInt())).thenReturn(true);
		when(customUserDetailService.loadUserByUserId(1)).thenReturn(userDetails);

		mockMvc.perform(delete("/post/delete/{postId}", postId).header("AUTHORIZATION", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.param("postId", Integer.toString(postId))).andExpect(status().isOk());
	}
}
