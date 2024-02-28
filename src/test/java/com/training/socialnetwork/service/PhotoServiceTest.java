package com.training.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;

import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.PhotoService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

	@InjectMocks
	private PhotoService photoService;
	
	@Mock
	private PhotoRepository photoRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ImageUtils imageUtils;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Test
	public void uploadPhotoSuccess() throws IOException {
		int userId = 1;
		User user = new User();
		user.setUserId(userId);
		
		MockMultipartFile photo = new MockMultipartFile("image1", "image1", "image/png", "image".getBytes());
		
		Photo photoToSave = new Photo();
		photoToSave.setName(photo.getOriginalFilename());
		photoToSave.setUser(user);
		photoToSave.setCreateDate(new Date());
		photoToSave.setUpdateDate(new Date());
		
		when(imageUtils.isValid(any())).thenReturn(true);
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(photoRepository.save(any())).thenReturn(photoToSave);
		
		photoService.uploadPhoto(photo, userId);
	}
	
	@Test
	public void downloadPhotoSuccess() throws IOException {
		int photoId = 1;
		
		photoService.downloadPhoto(photoId);
	}
	
	@Test
	public void deletePhotoSuccess() {
		int photoId = 1;
		int userId = 1;
		User user = new User();
		user.setUserId(1);
		
		Photo photo = new Photo();
		photo.setPhotoId(1);
		photo.setUser(user);
		
		when(photoRepository.findById(anyInt())).thenReturn(Optional.of(photo));
		when(photoRepository.save(any())).thenReturn(photo);
		
		photoService.deletePhoto(photoId, userId);
	}
	
	@Test
	public void deletePhotoFail() {
		int photoId = 1;
		int userId = 1;
		Photo photo = new Photo();
		photo.setPhotoId(1);
		photo.setDeleteFlg(Constant.DELETED_FlG);
		
		when(photoRepository.findById(anyInt())).thenReturn(Optional.of(photo));
		
		assertThrows(CustomException.class, () -> photoService.deletePhoto(photoId, userId));
	}
	
	@Test
	public void deletePhotoFail2() {
		int photoId = 1;
		int userId = 1;
		
		User user = new User();
		user.setUserId(2);
		
		Photo photo = new Photo();
		photo.setPhotoId(1);
		photo.setUser(user);
		
		when(photoRepository.findById(anyInt())).thenReturn(Optional.of(photo));
		
		assertThrows(CustomException.class, () -> photoService.deletePhoto(photoId, userId));
	}
	
	@Test
	public void getPhotoListSuccess() {
		int userId = 1;
		User user = new User();
		user.setUserId(1);
		
		Photo photo = new Photo();
		photo.setPhotoId(1);
		photo.setUser(user);
		
		List<Photo> photoList = new ArrayList<>();
		photoList.add(photo);
		
		Page<Photo> photoPage = new PageImpl<Photo>(photoList);
		
		when(photoRepository.findAllPhotosByUserId(anyInt(), any())).thenReturn(photoPage);
		
		photoService.getPhotoList(userId, null);
	}
}
