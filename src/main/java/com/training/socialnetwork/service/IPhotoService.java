package com.training.socialnetwork.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;

public interface IPhotoService {

	PhotoUploadResponseDto uploadPhoto(MultipartFile file, int userId) throws IOException;
	
	Resource downloadPhoto(int photoId) throws IOException;
	
	boolean deletePhoto(int photoId, int userId);
	
	Map<String, Object> getPhotoList(int userId, Pageable paging); 
}
