package com.training.socialnetwork.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;
import com.training.socialnetwork.service.impl.PhotoService;
import com.training.socialnetwork.utils.JSonHelper;

@WebMvcTest(PhotoController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhotoControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private PhotoService photoService;
	
	@BeforeAll
	void setUp() throws Exception {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void uploadPhotoSuccess() throws Exception {
		int userId = 1;
		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "some data".getBytes());
		
		PhotoUploadResponseDto photoUploadResponseDto = new PhotoUploadResponseDto();
		photoUploadResponseDto.setPhotoId(1);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.putIfAbsent("image", photo);
		
		String request = JSonHelper.toJson(requestBody).orElse("");
		
		when(photoService.uploadPhoto(photo,userId)).thenReturn(photoUploadResponseDto);
		
		mockMvc.perform(post("/photo/upload").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.content(request))
				.andExpect(status().isCreated()).andReturn();
	}
}
