package com.training.socialnetwork.controller;

import java.util.Map;

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

import com.training.socialnetwork.dto.request.post.PostCreateRequestDto;
import com.training.socialnetwork.dto.request.post.PostUpdateRequestDto;
import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.dto.response.post.PostCreateResponseDto;
import com.training.socialnetwork.dto.response.post.PostDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostUpdateResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.mapper.ObjectUtils;

@RestController
@RequestMapping(value = "/post")
public class PostController {

	@Autowired
	private IPostService postService;
	
	@Autowired
	private ObjectUtils objectUtils;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/create")
	public ResponseEntity<Object> createPost(HttpServletRequest request, @RequestBody PostCreateRequestDto postCreateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto errorResult = new MessageResponseDto();
		try {
			PostCreateRequestDto requestDto = (PostCreateRequestDto) objectUtils.getDefaulter(postCreateDto);
			if (objectUtils.checkNull(requestDto)) {
				errorResult.setMessage(Constant.ENTER_AT_LEAST_ONE_FIELD);
				return new ResponseEntity<Object>(errorResult, HttpStatus.BAD_REQUEST);
			}
			PostCreateResponseDto result = postService.createPost(userId, requestDto);

			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (CustomException e) {
			errorResult.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(errorResult, e.getHttpStatus());
		} catch (Exception e) {
			errorResult.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/timeline")
	public ResponseEntity<Object> getTimeline(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_1, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page - 1, pageSize);
		try {
			Map<String, Object> result = postService.getTimeline(userId, paging);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/detail/{postId}")
	public ResponseEntity<Object> getPostDetail(@PathVariable(value = "postId") int postId) {
		try {
			PostDetailResponseDto result = postService.getPost(postId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/update/{postId}")
	public ResponseEntity<Object> updatePost(HttpServletRequest request, @PathVariable(value = "postId") int postId, @RequestBody PostUpdateRequestDto postUpdateDto) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto errorResult = new MessageResponseDto();
		try {
			PostUpdateRequestDto requestDto = (PostUpdateRequestDto) objectUtils.getDefaulter(postUpdateDto);
			if (objectUtils.checkNull(requestDto)) {
				errorResult.setMessage(Constant.ENTER_AT_LEAST_ONE_FIELD);
				return new ResponseEntity<Object>(errorResult, HttpStatus.BAD_REQUEST);
			}
			PostUpdateResponseDto result = postService.updatePost(postUpdateDto, postId, userId);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			errorResult.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(errorResult, e.getHttpStatus());
		} catch (Exception e) {
			errorResult.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/{postId}")
	public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestParam(value = "postId") int postId) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
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
