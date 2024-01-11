package com.training.socialnetwork.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;
import com.training.socialnetwork.security.JwtUtils;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

	@Autowired
	private IFriendService friendService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping(value = "/all-friends")
	public List<FriendListDto> getFriendList(HttpServletRequest request) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		
		return friendService.findAllFriendWithStatus(userId);
	}

	@PostMapping(value = "/add-friend")
	public ResponseEntity<Object> createFriendRequest(HttpServletRequest request,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		int userId1 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = friendService.createFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "/accept-friend")
	public ResponseEntity<Object> acceptFriendRequest(HttpServletRequest request, @RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = friendService.acceptFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "/refuse-friend")
	public ResponseEntity<Object> refuseFriendRequest(HttpServletRequest request, @RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = friendService.refuseFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "remove-friend")
	public ResponseEntity<Object> removeFriend(HttpServletRequest request, @RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		boolean result = friendService.unfriend(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@GetMapping(value = "all-friend-request")
	public List<FriendRequestDto> getFriendRequestList(HttpServletRequest request) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		
		return friendService.findAllAddFriendRequest(userId);
	}
}
