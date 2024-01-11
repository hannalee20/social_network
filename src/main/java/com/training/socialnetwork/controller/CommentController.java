package com.training.socialnetwork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
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
	
	@PostMapping(value = "/create")
	public ResponseEntity<Object> createComment(HttpServletRequest request, @RequestBody CommentCreateDto comment) throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		CommentCreatedDto result = commentService.createComment(userId, comment);

		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{commentId}")
	public ResponseEntity<Object> updateComment(HttpServletRequest request, @RequestBody CommentUpdateDto comment, @PathVariable(value = "commentId") int commentId) throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		CommentCreatedDto result = commentService.updateComment(comment, commentId, userId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}

	@DeleteMapping(value = "delete/{commentId}")
	public ResponseEntity<Object> deleteComment(HttpServletRequest request, @PathVariable(value = "commentId") int commentId) throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = commentService.deleteComment(commentId, userId);
		
		if(result) {
			return new ResponseEntity<Object>(Constant.DELETED_SUCCESSFULLY, HttpStatus.OK);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}
	
	@GetMapping(value = "/detail/{commentId}")
	public ResponseEntity<Object> getCommentDetail(@PathVariable(value = "commentId") int commentId) throws Exception {
		CommentDetailDto result = commentService.getCommentDetail(commentId);
		
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
}
