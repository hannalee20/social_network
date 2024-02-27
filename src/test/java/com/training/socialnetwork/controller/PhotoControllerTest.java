package com.training.socialnetwork.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.impl.PhotoService;

@WebMvcTest(PhotoController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhotoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private PhotoService photoService;

	@BeforeAll
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void uploadPhotoSuccess() throws Exception {
		int userId = 1;
		MockMultipartFile file = new MockMultipartFile("file", "url", MediaType.APPLICATION_JSON_VALUE, "some value".getBytes());

		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());
		
		PhotoUploadResponseDto photoUploadResponseDto = new PhotoUploadResponseDto();
		photoUploadResponseDto.setPhotoId(1);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("image", file);

		when(photoService.uploadPhoto(file, userId)).thenReturn(photoUploadResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/photo/upload").file(photo1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA)
				).andExpect(status().isCreated());
	}
}
