package com.training.socialnetwork.controller;

import java.util.List;

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
	public ResponseEntity<Object> getFriendList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);
		try {
			List<FriendListDto> friendList = friendService.findAllFriendWithStatus(userId, paging);
			if(friendList != null ) {
				return new ResponseEntity<>(friendList, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(Constant.NO_RESULT, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/add-request")
	public ResponseEntity<Object> createFriendRequest(HttpServletRequest request,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		int userId1 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.createFriendRequest(userId1, userId2);

			return new ResponseEntity<Object>(Constant.SEND_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/accept-request")
	public ResponseEntity<Object> acceptFriendRequest(HttpServletRequest request,
			@RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.acceptFriendRequest(userId1, userId2);

			return new ResponseEntity<Object>(Constant.ACCEPT_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/refuse-request")
	public ResponseEntity<Object> refuseFriendRequest(HttpServletRequest request,
			@RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.refuseFriendRequest(userId1, userId2);

			return new ResponseEntity<Object>(Constant.REFUSE_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "remove-friend")
	public ResponseEntity<Object> removeFriend(HttpServletRequest request, @RequestParam(value = "userId1") int userId1)
			throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.unfriend(userId1, userId2);

			return new ResponseEntity<Object>(Constant.REMOVE_FRIEND_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(value = "all-friend-request")
	public ResponseEntity<Object> getFriendRequestList(HttpServletRequest request,
			@RequestParam(defaultValue = Constant.STRING_0, required = false) int page,
			@RequestParam(defaultValue = Constant.STRING_5, required = false) int pageSize) {
		int userId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		Pageable paging = PageRequest.of(page, pageSize);

		try {
			List<FriendRequestDto> friendRequestList = friendService.findAllAddFriendRequest(userId, paging);
			if (friendRequestList != null) {
				return new ResponseEntity<Object>(friendRequestList, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(Constant.NO_RESULT, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "remove-friend-request")
	public ResponseEntity<Object> removeFriendRequest(HttpServletRequest request,
			@RequestParam(value = "userId1") int userId1) throws Exception {
		int userId2 = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.removeFriendRequest(userId1, userId2);

			return new ResponseEntity<Object>(Constant.REMOVE_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
