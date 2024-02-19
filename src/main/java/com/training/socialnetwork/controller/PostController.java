package com.training.socialnetwork.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.training.socialnetwork.dto.request.post.PostCreateDto;
import com.training.socialnetwork.dto.request.post.PostUpdateDto;
import com.training.socialnetwork.dto.response.common.MessageDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	@Autowired
	private IPostService postService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createPost(HttpServletRequest request, @RequestBody PostCreateDto postCreateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			PostCreatedDto result = postService.createPost(userId, postCreateDto);

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (CustomException e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/timeline")
	public ResponseEntity<Object> getTimeline(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);
		try {
			Page<PostListDto> postList = postService.getTimeline(userId, paging);
			Map<String, Object> result = new HashMap<>();
			result.put("postList", postList.getContent());
			result.put("currentPage", postList.getNumber());
			result.put("totalItems", postList.getTotalElements());
			result.put("totalPages", postList.getTotalPages());

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/detail/{postId}")
	public ResponseEntity<Object> getPostDetail(@PathVariable(value = "postId") int postId) {
		try {
			PostDetailDto result = postService.getPost(postId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/update/{postId}")
	public ResponseEntity<Object> updatePost(HttpServletRequest request, @PathVariable(value = "postId") int postId, @RequestBody PostUpdateDto postUpdateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			PostUpdatedDto result = postService.updatePost(postUpdateDto, postId, userId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageDto result = new MessageDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "/delete/{postId}")
	public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestParam(value = "postId") int postId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageDto result = new MessageDto();
		try {
			postService.deletePost(postId, userId);
			
			result.setMessage(Constant.DELETE_SUCCESSFULLY);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
