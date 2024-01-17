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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createPost(HttpServletRequest request, @RequestParam String content, @RequestParam MultipartFile[] photos)
			throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		PostCreatedDto result = postService.createPost(userId, content, photos);

		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

	@GetMapping(value = "/timeline")
	public List<PostListDto> getPostList(HttpServletRequest request, @RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "5", required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);
		return postService.getAllPosts(userId, paging);
	}

	@GetMapping(value = "/detail/{postId}")
	public ResponseEntity<Object> getPostDetail(@PathVariable(value = "postId") int postId) throws Exception {
		PostDetailDto result = postService.getPost(postId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PutMapping(value = "/update/{postId}")
	public ResponseEntity<Object> updatePost(HttpServletRequest request, @RequestBody PostUpdateDto post,
			@PathVariable(value = "postId") int postId) throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		PostUpdatedDto result = postService.updatePost(post, postId, userId);

		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}

	@DeleteMapping(value = "/delete/{postId}")
	public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestParam(value = "postId") int postId)
			throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = postService.deletePost(postId, userId);
		if (result) {
			return new ResponseEntity<Object>(Constant.DELETED_SUCCESSFULLY, HttpStatus.OK);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

}
