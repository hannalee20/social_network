package com.training.socialnetwork.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.response.common.MessageResponseDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

	@Autowired
	private IFriendService friendService;

	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping(value = "/all-friends")
	public ResponseEntity<Object> getFriendList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);
		try {
			Map<String, Object> result = friendService.findAllFriendWithStatus(userId, paging);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/add-request")
	public ResponseEntity<Object> createFriendRequest(HttpServletRequest request,
			@RequestParam(value = "recievedUserId") int recievedUserId) throws Exception {
		int sentUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			int status = friendService.createFriendRequest(sentUserId, recievedUserId);

			if(status == Constant.NUMBER_0) {
				result.setMessage(Constant.SEND_FRIEND_REQUEST_SUCCESSFULLY);
			} else {
				result.setMessage(Constant.ACCEPT_FRIEND_REQUEST_SUCCESSFULLY);
			}
			
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/accept-request")
	public ResponseEntity<Object> acceptFriendRequest(HttpServletRequest request,
			@RequestParam(value = "sentUserId") int sentUserId) throws Exception {
		int recievedUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			friendService.acceptFriendRequest(sentUserId, recievedUserId);

			result.setMessage(Constant.ACCEPT_FRIEND_REQUEST_SUCCESSFULLY);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/refuse-request")
	public ResponseEntity<Object> refuseFriendRequest(HttpServletRequest request,
			@RequestParam(value = "sentUserId") int sentUserId) throws Exception {
		int recievedUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			friendService.refuseFriendRequest(sentUserId, recievedUserId);

			result.setMessage(Constant.REFUSE_FRIEND_REQUEST_SUCCESSFULLY);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "remove-friend")
	public ResponseEntity<Object> removeFriend(HttpServletRequest request,
			@RequestParam(value = "friendUserId") int friendUserId) throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			friendService.unfriend(friendUserId, loggedInUserId);

			result.setMessage(Constant.REMOVE_FRIEND_SUCCESSFULLY);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (CustomException e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, e.getHttpStatus());
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "all-friend-request")
	public ResponseEntity<Object> getFriendRequestList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);

		try {
			Map<String, Object> result = friendService.findAllAddFriendRequest(userId, paging);
			
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			MessageResponseDto result = new MessageResponseDto();
			result.setMessage(e.getMessage());
			
			return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "remove-friend-request")
	public ResponseEntity<Object> removeFriendRequest(HttpServletRequest request,
			@RequestParam(value = "recievedUserId") int recievedUserId) throws Exception {
		int sentUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		MessageResponseDto result = new MessageResponseDto();
		try {
			friendService.removeFriendRequest(sentUserId, recievedUserId);

			result.setMessage(Constant.REMOVE_FRIEND_REQUEST_SUCCESSFULLY);
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
