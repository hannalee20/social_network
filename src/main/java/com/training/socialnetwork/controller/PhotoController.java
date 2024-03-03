package com.training.socialnetwork.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.dto.response.photo.PhotoUploadResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPhotoService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@RestController
@RequestMapping(value = "/photo")
public class PhotoController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private IPhotoService photoService;

	@PostMapping(value = "/upload", consumes = { "multipart/form-data" })
	public ResponseEntity<Object> uploadPhoto(HttpServletRequest request, @RequestParam(value = "photo") MultipartFile photo) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			PhotoUploadResponseDto result = photoService.uploadPhoto(photo, userId);

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/download/{photoId}")
	public ResponseEntity<Object> downloadPhoto(@PathVariable int photoId, HttpServletResponse response) {
		Resource resource = null;
		try {
			resource = photoService.downloadPhoto(photoId);
			
			if (resource == null) {
				MessageResponseDto result = new MessageResponseDto();
				result.setMessage(Constant.FILE_NOT_FOUND);

				return new ResponseEntity<Object>(result, HttpStatus.NOT_FOUND);
			}
			String contentType = "application/octet-stream";
			String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/{photoId}")
	public ResponseEntity<Object> deletePhoto(HttpServletRequest request, @PathVariable int photoId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			photoService.deletePhoto(photoId, userId);

			result.setMessage(Constant.DELETE_SUCCESSFULLY);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/all-photos")
	public ResponseEntity<Object> getPhotoList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_1, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page - 1, pageSize);

		try {
			Map<String, Object> result = photoService.getPhotoList(userId, paging);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/all-photos/{userId}")
	public ResponseEntity<Object> getOtherUserPhotoList(@PathVariable int userId,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		Pageable paging = PageRequest.of(page, pageSize);

		try {
			Map<String, Object> result = photoService.getPhotoList(userId, paging);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
