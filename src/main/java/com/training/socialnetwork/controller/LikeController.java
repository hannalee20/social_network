package com.training.socialnetwork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/like")
public class LikeController {

	@PostMapping(value = "/add")
	public ResponseEntity<Object> addLike(@RequestParam(value = "postId") int postId, @RequestParam(value = "userId") int userId) {
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}
}
