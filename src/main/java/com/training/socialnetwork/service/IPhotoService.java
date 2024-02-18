package com.training.socialnetwork.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IPhotoService {

	int uploadPhoto(MultipartFile file) throws IOException;
	
	Resource downloadPhoto(String photoCode) throws IOException;
}
