package com.training.socialnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.service.ICommentService;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;
	
	@PostMapping(value = "/create")
	public ResponseEntity<Object> createComment(@RequestBody Comment comment) {
		return new ResponseEntity<Object>(commentService.createComment(comment), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateComment(@RequestBody Comment comment, @RequestParam(value = "userId") int userId) throws Exception {
		boolean result = commentService.updateComment(comment, userId);
		
		if(result) {
			return new ResponseEntity<Object>("success", HttpStatus.OK);
		}
		
		throw new Exception("server error");
	}
}
