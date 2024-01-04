package com.training.socialnetwork.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping(value = "/all/{userId}")
	public List<Post> getPostList(@PathVariable(value = "userId") int userId) {
		return postService.getAllPosts(userId);
		
	}
	
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updatePost(@RequestBody Post post, @RequestParam(value = "userId") int userId) {
		return new ResponseEntity<Object>(postService.updatePost(post, userId), HttpStatus.OK);
		
	}
	
	@DeleteMapping(value = "/delete")
	
	public ResponseEntity<Object> deletePost(@RequestParam(value = "postId") int postId, @RequestParam(value = "userId") int userId) throws Exception {
		boolean result = postService.deletePost(postId, userId);
		if(result) {
			return new ResponseEntity<Object>("success", HttpStatus.OK);
		}
		throw new Exception("server error");
	}
	
}
