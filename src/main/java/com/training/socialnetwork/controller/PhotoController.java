package com.training.socialnetwork.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.common.MessageDto;
import com.training.socialnetwork.dto.response.photo.PhotoUploadedDto;
import com.training.socialnetwork.service.IPhotoService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/photo")
public class PhotoController {

	@Autowired
	private IPhotoService photoService;

	@PostMapping(value = "/upload", consumes = { "multipart/form-data" })
	public ResponseEntity<Object> uploadPhoto(HttpServletRequest request, @RequestParam("image") MultipartFile photo) {
		try {
			PhotoUploadedDto result = photoService.uploadPhoto(photo);

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/download")
	public ResponseEntity<Object> downloadPhoto(int photoId, HttpServletResponse response) {
		Resource resource = null;
		try {
			resource = photoService.downloadPhoto(photoId);
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (resource == null) {
			MessageDto result = new MessageDto();
			result.setMessage(Constant.FILE_NOT_FOUND);
			
			return new ResponseEntity<Object>(result, HttpStatus.NOT_FOUND);
		}
		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}
}
