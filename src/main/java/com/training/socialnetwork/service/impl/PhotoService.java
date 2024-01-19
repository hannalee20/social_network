package com.training.socialnetwork.service.impl;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.service.IPhotoService;

@Service
@Transactional
public class PhotoService implements IPhotoService{

	@Autowired
	private PhotoRepository photoRepository;
	
	public Photo savePhoto(MultipartFile file) throws IOException {
		String name = StringUtils.cleanPath(file.getOriginalFilename());
		Photo photo = new Photo();
		photo.setName(name);
		
		return photoRepository.save(photo);
	}
	
}
