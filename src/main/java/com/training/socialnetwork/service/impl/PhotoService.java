package com.training.socialnetwork.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.service.IPhotoService;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@Service
@Transactional
public class PhotoService implements IPhotoService {

	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private ImageUtils imageUtils;

	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

	private Path foundFile;

	@Override
	public int uploadPhoto(MultipartFile photo) throws IOException {
		if (!imageUtils.isValid(photo)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid photo");
		}
		Path staticPath = Paths.get("static");
		Path imagePath = Paths.get("images");

		if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
			Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
		}
		String photoCode = UUID.randomUUID().toString();

		Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)
				.resolve(photoCode + "-" + photo.getOriginalFilename());
		try (OutputStream os = Files.newOutputStream(file)) {
			os.write(photo.getBytes());
		}
		Photo photoToSave = new Photo();
		photoToSave.setName(photo.getOriginalFilename());
		photoToSave.setCreateDate(new Date());

		photoToSave = photoRepository.save(photoToSave);

		return photoToSave.getPhotoId();
	}

	@Override
	public Resource downloadPhoto(String photoCode) throws IOException {
		Path staticPath = Paths.get("static");
		Path imagePath = Paths.get("images");

		Files.list(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath)).forEach(file -> {
			if (file.getFileName().toString().startsWith(photoCode)) {
				foundFile = file;
				return;
			}
		});

		if (foundFile != null) {
			return new UrlResource(foundFile.toUri());
		}

		return null;
	}

}
