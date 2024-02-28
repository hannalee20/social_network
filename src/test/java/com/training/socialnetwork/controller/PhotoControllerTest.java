package com.training.socialnetwork.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.training.socialnetwork.dto.response.photo.PhotoListResponseDto;
import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.impl.PhotoService;
import com.training.socialnetwork.util.exception.CustomException;

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
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());

		PhotoUploadResponseDto photoUploadResponseDto = new PhotoUploadResponseDto();
		photoUploadResponseDto.setPhotoId(1);
		photoUploadResponseDto.setCreateDate(new Date());
		photoUploadResponseDto.setUpdateDate(new Date());

		when(photoService.uploadPhoto(photo1, userId)).thenReturn(photoUploadResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/photo/upload").file(photo1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isCreated());
	}

	@Test
	public void uploadPhotoFail() throws Exception {
		MockMultipartFile photo1 = new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data",
				"some xml".getBytes());

		PhotoUploadResponseDto photoUploadResponseDto = new PhotoUploadResponseDto();
		photoUploadResponseDto.setPhotoId(1);

		when(photoService.uploadPhoto(any(), anyInt())).thenThrow(new IOException());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/photo/upload").file(photo1)
				.header("Authorization", "Bearer dummyToken").contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void downloadPhotoSuccess() throws Exception {
		int photoId = 1;
		File file = new File("XXX" + (new Random().nextInt()) + ".txt");
	    file.createNewFile();
		Resource resource = new UrlResource(file.toURI());

		when(photoService.downloadPhoto(anyInt())).thenReturn(resource);

		mockMvc.perform(get("/photo/download/{photoId}", photoId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void downloadPhotoFail() throws Exception {
		int photoId = 1;
		Resource resource = null;

		when(photoService.downloadPhoto(anyInt())).thenReturn(resource);

		mockMvc.perform(get("/photo/download/{photoId}", photoId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
	
	@Test
	public void downloadPhotoFail2() throws Exception {
		int photoId = 1;

		when(photoService.downloadPhoto(anyInt())).thenThrow(new IOException());

		mockMvc.perform(get("/photo/download/{photoId}", photoId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void deletePhotoSuccess() throws Exception {
		Photo photo = new Photo();
		photo.setPhotoId(1);
		
		when(photoService.deletePhoto(anyInt(), anyInt())).thenReturn(true);
		
		mockMvc.perform(delete("/photo/{photoId}", photo.getPhotoId()).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void deletePhotoFail() throws Exception {
		int photoId = 1;
		
		when(photoService.deletePhoto(anyInt(), anyInt())).thenThrow(new CustomException(HttpStatus.FORBIDDEN, null));
		
		mockMvc.perform(delete("/photo/{photoId}", photoId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}
	
	@Test
	public void deletePhotoFail2() throws Exception {
		int photoId = 1;
		
		when(photoService.deletePhoto(anyInt(), anyInt())).thenThrow(new RuntimeException());
		
		mockMvc.perform(delete("/photo/{photoId}", photoId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void getPhotoListSuccess() throws Exception {
		PhotoListResponseDto photo = new PhotoListResponseDto();
		photo.setPhotoId(1);
		photo.setCreateDate(new Date());
		photo.setUpdateDate(new Date());
		
		PhotoListResponseDto photo2 = new PhotoListResponseDto();
		photo2.setPhotoId(2);
		
		List<PhotoListResponseDto> photoList = new ArrayList<>();
		photoList.add(photo);
		photoList.add(photo2);
		
		Page<PhotoListResponseDto> photoListDtoPage = new PageImpl<PhotoListResponseDto>(photoList);
		
		Map<String, Object> result = new HashMap<>();
		result.put("photoList", photoListDtoPage.getContent());
		
		when(photoService.getPhotoList(anyInt(), any())).thenReturn(result);
		
		mockMvc.perform(get("/photo/all-photos").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	
	@Test
	public void getPhotoListFail() throws Exception {
		
		when(photoService.getPhotoList(anyInt(), any())).thenThrow(new RuntimeException());
		
		mockMvc.perform(get("/photo/all-photos").header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
		
	}
	
	@Test
	public void getOtherUserPhotoListSuccess() throws Exception {
		int userId = 1;
		
		PhotoListResponseDto photo = new PhotoListResponseDto();
		photo.setPhotoId(1);
		
		PhotoListResponseDto photo2 = new PhotoListResponseDto();
		photo.setPhotoId(2);
		
		List<PhotoListResponseDto> photoList = new ArrayList<>();
		photoList.add(photo);
		photoList.add(photo2);
		
		Page<PhotoListResponseDto> photoListDtoPage = new PageImpl<PhotoListResponseDto>(photoList);
		
		Map<String, Object> result = new HashMap<>();
		result.put("photoList", photoListDtoPage.getContent());
		
		when(photoService.getPhotoList(anyInt(), any())).thenReturn(result);
		
		mockMvc.perform(get("/photo/all-photos/{userId}", userId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
	}
	
	@Test
	public void getOtherUserPhotoListFail() throws Exception {
		int userId = 1;
		
		when(photoService.getPhotoList(anyInt(), any())).thenThrow(new RuntimeException());
		
		mockMvc.perform(get("/photo/all-photos/{userId}", userId).header("Authorization", "Bearer dummyToken")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
		
	}
}
