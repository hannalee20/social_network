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

import com.training.socialnetwork.dto.request.post.PostCreateDto;
import com.training.socialnetwork.dto.request.post.PostUpdateDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	@Autowired
	private IPostService postService;

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createPost(@RequestBody PostCreateDto post) throws Exception {
		PostCreatedDto result = postService.createPost(post);

		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

	@GetMapping(value = "/all/{userId}")
	public List<PostListDto> getPostList(@PathVariable(value = "userId") int userId) {
		return postService.getAllPosts(userId);

	}

	@GetMapping(value = "/detail/{postId}")
	public ResponseEntity<Object> getPostDetail(@PathVariable(value = "postId") int postId) throws Exception {
		PostDetailDto result = postService.getPost(postId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PutMapping(value = "/update/{postId}")
	public ResponseEntity<Object> updatePost(@RequestBody PostUpdateDto post,
			@PathVariable(value = "postId") int postId, @RequestParam(value = "userId") int userId) throws Exception {
		PostUpdatedDto result = postService.updatePost(post, postId, userId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}

	@DeleteMapping(value = "/delete/{postId}")
	public ResponseEntity<Object> deletePost(@RequestParam(value = "postId") int postId,
			@RequestParam(value = "userId") int userId) throws Exception {
		boolean result = postService.deletePost(postId, userId);
		if (result) {
			return new ResponseEntity<Object>(Constant.DELETED_SUCCESSFULLY, HttpStatus.OK);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

}
