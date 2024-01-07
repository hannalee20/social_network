package com.training.socialnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.service.ILikeService;

@RestController
@RequestMapping(value = "/like")
public class LikeController {

	@Autowired
	private ILikeService likeService;

	@PostMapping(value = "/add")
	public ResponseEntity<Object> addLike(@RequestParam(value = "postId") int postId,
			@RequestParam(value = "userId") int userId) throws Exception {
		boolean result = likeService.addLikePost(postId, userId);
		
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PostMapping(value = "/unlike")
	public ResponseEntity<Object> unlike(@RequestParam(value = "postId") int postId,
			@RequestParam(value = "userId") int userId) throws Exception {
		boolean result = likeService.unlikePost(postId, userId);
		
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
}
