package com.training.socialnetwork.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.entity.Photo;

public interface IPhotoService {
	
	Photo savePhoto(MultipartFile file) throws IOException;
	
}
