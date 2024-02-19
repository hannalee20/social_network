package com.training.socialnetwork.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.photo.PhotoUploadedDto;

public interface IPhotoService {

	PhotoUploadedDto uploadPhoto(MultipartFile file) throws IOException;
	
	Resource downloadPhoto(int photoId) throws IOException;
}
