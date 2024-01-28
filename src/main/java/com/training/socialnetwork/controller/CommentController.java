package com.training.socialnetwork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<Object> createComment(HttpServletRequest request, @ParameterObject @ModelAttribute CommentCreateDto commentCreateDto, @RequestParam(value = "file", required = false) MultipartFile photo) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			CommentCreatedDto result = commentService.createComment(userId, commentCreateDto, photo);
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/update/{commentId}", consumes = {"multipart/form-data"})
	public ResponseEntity<Object> updateComment(HttpServletRequest request, @PathVariable(value = "commentId") int commentId, @RequestParam String content, @RequestParam(value = "file", required = false) MultipartFile photo) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			CommentUpdatedDto result = commentService.updateComment(content, photo, commentId, userId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "delete/{commentId}")
	public ResponseEntity<Object> deleteComment(HttpServletRequest request,
			@PathVariable(value = "commentId") int commentId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			commentService.deleteComment(commentId, userId);

			return new ResponseEntity<Object>(Constant.DELETE_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/detail/{commentId}")
	public ResponseEntity<Object> getCommentDetail(@PathVariable(value = "commentId") int commentId) {
		try {
			CommentDetailDto result = commentService.getCommentDetail(commentId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
