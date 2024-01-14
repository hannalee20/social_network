package com.training.socialnetwork.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.entity.Photo;

public interface IPhotoService {
	
	Photo savePhoto(MultipartFile file) throws IOException;
	
	Stream<Photo> getPhoto(int id);
}
