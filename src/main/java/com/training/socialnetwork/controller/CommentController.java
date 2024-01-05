package com.training.socialnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createComment(@RequestBody CommentCreateDto comment) throws Exception {
		CommentCreatedDto result = commentService.createComment(comment);

		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{commentId}")
	public ResponseEntity<Object> updateComment(@RequestBody CommentUpdateDto comment, @PathVariable(value = "commentId") int commentId,
			@RequestParam(value = "userId") int userId) throws Exception {
		CommentCreatedDto result = commentService.updateComment(comment, userId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}

	@DeleteMapping(value = "delete/{commentId}")
	public ResponseEntity<Object> deleteComment(@PathVariable(value = "commentId") int commentId,
			@RequestParam(value = "userId") int userId) throws Exception {
		boolean result = commentService.deleteComment(commentId, userId);
		
		if(result) {
			return new ResponseEntity<Object>(Constant.DELETED_SUCCESSFULLY, HttpStatus.OK);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}
}
