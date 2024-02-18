package com.training.socialnetwork.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
			Page<FriendListDto> friendList = friendService.findAllFriendWithStatus(userId, paging);
			Map<String, Object> response = new HashMap<>();
			response.put("friendList", friendList.getContent());
			response.put("currentPage", friendList.getNumber());
			response.put("totalItems", friendList.getTotalElements());
			response.put("totalPages", friendList.getTotalPages());

			return new ResponseEntity<Object>(response, HttpStatus.OK);
//			if(!friendList.isEmpty()) {
//				return new ResponseEntity<>(friendList, HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(Constant.NO_RESULT, HttpStatus.NO_CONTENT);
//			}
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/add-request")
	public ResponseEntity<Object> createFriendRequest(HttpServletRequest request,
			@RequestParam(value = "recievedUserId") int recievedUserId) throws Exception {
		int sentUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.createFriendRequest(sentUserId, recievedUserId);

			return new ResponseEntity<Object>(Constant.SEND_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.CREATED);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/accept-request")
	public ResponseEntity<Object> acceptFriendRequest(HttpServletRequest request,
			@RequestParam(value = "sentUserId") int sentUserId) throws Exception {
		int recievedUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.acceptFriendRequest(sentUserId, recievedUserId);

			return new ResponseEntity<Object>(Constant.ACCEPT_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/refuse-request")
	public ResponseEntity<Object> refuseFriendRequest(HttpServletRequest request,
			@RequestParam(value = "sentUserId") int sentUserId) throws Exception {
		int recievedUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.refuseFriendRequest(sentUserId, recievedUserId);

			return new ResponseEntity<Object>(Constant.REFUSE_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "remove-friend")
	public ResponseEntity<Object> removeFriend(HttpServletRequest request,
			@RequestParam(value = "friendUserId") int friendUserId) throws Exception {
		int loggedInUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.unfriend(friendUserId, loggedInUserId);

			return new ResponseEntity<Object>(Constant.REMOVE_FRIEND_SUCCESSFULLY, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
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
			if (!friendRequestList.isEmpty()) {
				return new ResponseEntity<Object>(friendRequestList, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(Constant.NO_RESULT, HttpStatus.NO_CONTENT);
			}

		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "remove-friend-request")
	public ResponseEntity<Object> removeFriendRequest(HttpServletRequest request,
			@RequestParam(value = "recievedUserId") int recievedUserId) throws Exception {
		int sentUserId = jwtUtils.getUserIdFromJwt(jwtUtils.getJwt(request));
		try {
			friendService.removeFriendRequest(sentUserId, recievedUserId);

			return new ResponseEntity<Object>(Constant.REMOVE_FRIEND_REQUEST_SUCCESSFULLY, HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<Object>(e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
