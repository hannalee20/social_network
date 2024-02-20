package com.training.socialnetwork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.request.comment.CommentCreateRequestDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentCreateResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdateResponseDto;
import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@RestController
@Validated
@RequestMapping(value = "/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createComment(HttpServletRequest request,
			@RequestBody CommentCreateRequestDto commentCreateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			CommentCreateResponseDto result = commentService.createComment(userId, commentCreateDto);
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/update/{commentId}")
	public ResponseEntity<Object> updateComment(HttpServletRequest request,
			@PathVariable(value = "commentId") int commentId, @RequestBody CommentUpdateRequestDto commentUpdateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			CommentUpdateResponseDto result = commentService.updateComment(commentUpdateDto, commentId, userId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "delete/{commentId}")
	public ResponseEntity<Object> deleteComment(HttpServletRequest request,
			@PathVariable(value = "commentId") int commentId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			commentService.deleteComment(commentId, userId);

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

	@GetMapping(value = "/detail/{commentId}")
	public ResponseEntity<Object> getCommentDetail(@PathVariable(value = "commentId") int commentId) {
		try {
			CommentDetailResponseDto result = commentService.getCommentDetail(commentId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
