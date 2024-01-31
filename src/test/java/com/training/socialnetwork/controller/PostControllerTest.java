package com.training.socialnetwork.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;

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
	private JwtUtils jwtUtils;

	@BeforeAll
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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

		mockMvc.perform(MockMvcRequestBuilders.multipart("/post/create").file(photo1).file(photo2)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.postId").value(postCreatedDto.getPostId()))
				.andExpect(jsonPath("$.userId").value(postCreatedDto.getUserId()))
				.andExpect(jsonPath("$.content").value(postCreatedDto.getContent()))
				.andExpect(jsonPath("$.username").value(postCreatedDto.getUsername()));
	}

	@Test
    public void createPostFail() throws Exception {
        int userId = 1;
        String content = "Test post content";
        MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());
		MockMultipartFile photo2 = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data",
				"some xml".getBytes());
		
        when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
        when(postService.createPost(anyInt(), any(), any())).thenThrow(new Exception("Some error message"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/post/create").file(photo1).file(photo2)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isInternalServerError());
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
		postList.add(postListDto);

		when(postService.getTimeline(anyInt(), any())).thenReturn(postList);

		mockMvc.perform(get("/post/timeline").header("Authorization", "Bearer dummyToken")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
    public void getTimelineFail() throws Exception {
        when(postService.getTimeline(anyInt(), any())).thenReturn(null);

        mockMvc.perform(get("/post/timeline")
        		.header("Authorization", "Bearer dummyToken")).andExpect(status().isNoContent())
                .andExpect(content().string(Constant.NO_RESULT));
    }

    @Test
    public void getTimelineFail2() throws Exception {
    	int userId = 1;

        when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
        when(postService.getTimeline(anyInt(), any())).thenThrow(new RuntimeException("Some error message"));

        mockMvc.perform(get("/post/timeline")
        		.header("Authorization", "Bearer dummyToken")).andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error message"));
    }
    
	@Test
	public void getPostDetailSuccess() throws Exception {
		PostDetailDto postDetailDto = new PostDetailDto();
		postDetailDto.setPostId(1);
		postDetailDto.setUserId(1);
		postDetailDto.setUsername("test");
		postDetailDto.setContent("content");
		postDetailDto.setPhotoUrl("test");
		postDetailDto.setCreateDate(new Date());

		when(postService.getPost(anyInt())).thenReturn(postDetailDto);

		mockMvc.perform(get("/post/detail/{postId}", 1).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
    public void getPostDetailFail() throws Exception {
        int postId = 1;

        when(postService.getPost(anyInt())).thenThrow(new Exception("Some error message"));

        mockMvc.perform(get("/post/detail/{postId}", postId)
        		.header("Authorization", "Bearer dummyToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error message"));
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
		postUpdatedDto.setPostId(1);
		postUpdatedDto.setUserId(1);
		postUpdatedDto.setUsername("test");
		postUpdatedDto.setContent(content);
		postUpdatedDto.setUpdateDate(new Date());

		when(postService.updatePost(any(), any(), anyInt(), anyInt())).thenReturn(postUpdatedDto);

		mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/post/update/{postId}", postId).file(photo1)
				.file(photo2).header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				.param("content", content)).andExpect(status().isOk());
	}
	
	@Test
    public void updatePostFail() throws Exception {
        int userId = 1;
        int postId = 1;
        MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());
		MockMultipartFile photo2 = new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data",
				"some xml".getBytes());
		
        when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
        when(postService.updatePost(any(), any(), anyInt(), anyInt())).thenThrow(new Exception("Some error message"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/post/update/{postId}", postId).file(photo1)
				.file(photo2).header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error message"));
    }

	@Test
	public void deletePostSuccess() throws Exception {
		int postId = 1;

		when(postService.deletePost(anyInt(), anyInt())).thenReturn(true);

		mockMvc.perform(delete("/post/delete/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON).param("postId", Integer.toString(postId)))
				.andExpect(status().isOk());
	}
	
	@Test
    public void deletePostFail() throws Exception {
        int userId = 1;
        int postId = 1;

        when(jwtUtils.getUserIdFromJwt(anyString())).thenReturn(userId);
        when(postService.deletePost(anyInt(), anyInt())).thenThrow(new RuntimeException("Some error message"));

        mockMvc.perform(delete("/post/delete/{postId}", postId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)
        		.param("postId", Integer.toString(postId)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error message"));
    }
}
