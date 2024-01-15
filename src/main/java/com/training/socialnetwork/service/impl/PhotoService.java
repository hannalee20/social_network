package com.training.socialnetwork.service.impl;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.service.IPhotoService;

@Service
public class PhotoService implements IPhotoService{

	@Autowired
	private PhotoRepository photoRepository;
	
	public Photo savePhoto(MultipartFile file) throws IOException {
		String name = StringUtils.cleanPath(file.getOriginalFilename());
		Photo photo = new Photo();
		photo.setName(name);
		photo.setType(file.getContentType());
		photo.setData(file.getBytes());
		
		return photoRepository.save(photo);
	}
	
	public Stream<Photo> getPhoto(int postId) {
		return photoRepository.findByPostPostId(postId);
	}
}
