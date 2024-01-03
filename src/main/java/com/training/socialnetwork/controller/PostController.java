package com.training.socialnetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.service.IPostService;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	@Autowired
	private IPostService postService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostMapping(value = "/create")
	public ResponseEntity<Object> createPost(@RequestBody Post post) throws Exception {
		boolean result = postService.createPost(post);
		
		if(result) {
			return new ResponseEntity<Object>("success", HttpStatus.OK);
		}
		
		throw new Exception("server error");
	}
	
//	@GetMapping(value = "")
}
