package com.training.socialnetwork.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.photo.PhotoListResponseDto;
import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IPhotoService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@Service
@Transactional
public class PhotoService implements IPhotoService {

	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageUtils imageUtils;
	
	@Autowired
	private ModelMapper modelMapper;

	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

	private Path foundFile;

	@Override
	public PhotoUploadResponseDto uploadPhoto(MultipartFile photo, int userId) throws IOException {
		if (!imageUtils.isValid(photo)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid photo");
		}
		Path staticPath = Paths.get("static");
		Path imagePath = Paths.get("images");

		if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
			Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
		}

		User user = userRepository.findById(userId).orElse(null);
		
		Photo photoToSave = new Photo();
		photoToSave.setName(photo.getOriginalFilename());
		photoToSave.setUser(user);
		photoToSave.setCreateDate(new Date());
		photoToSave.setUpdateDate(new Date());

		photoToSave = photoRepository.save(photoToSave);
		
		int photoId = photoToSave.getPhotoId();
		Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
				.resolve(photoId + "-" + photo.getOriginalFilename());
		try (OutputStream os = Files.newOutputStream(file)) {
			os.write(photo.getBytes());
		}

		PhotoUploadResponseDto uploadedPhotoDto = modelMapper.map(photoToSave, PhotoUploadResponseDto.class);
		
		return uploadedPhotoDto;
	}

	@Override
	public Resource downloadPhoto(int photoId) throws IOException {
		Path staticPath = Paths.get("static");
		Path imagePath = Paths.get("images");

		Files.list(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)).forEach(file -> {
			if (file.getFileName().toString().startsWith(Integer.toString(photoId))) {
				foundFile = file;
				return;
			}
		});

		if (foundFile != null) {
			return new UrlResource(foundFile.toUri());
		}

		return null;
	}

	@Override
	public boolean deletePhoto(int photoId, int userId) {
		Photo photo = photoRepository.findById(photoId).orElse(null);
		
		if (photo == null || photo.getDeleteFlg() == Constant.DELETED_FlG) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exis");
		}
		if (photo.getUser().getUserId() != userId) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		photo.setDeleteFlg(Constant.DELETED_FlG);
		photo.setUpdateDate(new Date());

		return photoRepository.save(photo) != null;
	}

	@Override
	public Map<String, Object> getPhotoList(int userId, Pageable paging) {
		Page<Photo> photoList = photoRepository.findAllPhotosByUserId(userId, paging);
		
		List<PhotoListResponseDto> photoListDtos = new ArrayList<>();
		for (Photo photo : photoList) {
			PhotoListResponseDto photoListResponseDto = modelMapper.map(photo, PhotoListResponseDto.class);
			
			photoListDtos.add(photoListResponseDto);
		}
		Page<PhotoListResponseDto> photoListDtoPage = new PageImpl<PhotoListResponseDto>(photoListDtos);
		Map<String, Object> result = new HashMap<>();
		result.put("photoList", photoListDtoPage.getContent());
		result.put("currentPage", photoList.getNumber() + 1);
		result.put("totalItems", photoList.getTotalElements());
		result.put("totalPages", photoList.getTotalPages());
		
		return result;
	}

}
