package com.training.socialnetwork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.ILikeService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@RestController
@RequestMapping(value = "/like")
public class LikeController {

	@Autowired
	private ILikeService likeService;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/add")
	public ResponseEntity<Object> addLike(HttpServletRequest request, @RequestParam(value = "postId") int postId)
			throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			likeService.addLikePost(postId, userId);

			result.setMessage(Constant.LIKED);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/unlike")
	public ResponseEntity<Object> unlike(HttpServletRequest request, @RequestParam(value = "postId") int postId)
			throws Exception {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			likeService.unlikePost(postId, userId);

			result.setMessage(Constant.UNLIKED);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());

			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
