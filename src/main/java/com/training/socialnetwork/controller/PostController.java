package com.training.socialnetwork.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	@Autowired
	private IPostService postService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/create", consumes = {"multipart/form-data"})
	public ResponseEntity<Object> createPost(HttpServletRequest request, @ModelAttribute PostCreateDto postCreateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			PostCreatedDto result = postService.createPost(userId, postCreateDto.getContent(), postCreateDto.getPhotos());

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "/timeline")
	public ResponseEntity<Object> getPostList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);
		try {
			List<PostListDto> postList = postService.getAllPosts(userId, paging);
			return new ResponseEntity<>(postList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/detail/{postId}")
	public ResponseEntity<Object> getPostDetail(@PathVariable(value = "postId") int postId) {
		try {
			PostDetailDto result = postService.getPost(postId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/update/{postId}")
	public ResponseEntity<Object> updatePost(HttpServletRequest request, @RequestBody PostUpdateDto post,
			@PathVariable(value = "postId") int postId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			PostUpdatedDto result = postService.updatePost(post, postId, userId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "/delete/{postId}")
	public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestParam(value = "postId") int postId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			postService.deletePost(postId, userId);
			
			return new ResponseEntity<Object>(Constant.DELETE_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
