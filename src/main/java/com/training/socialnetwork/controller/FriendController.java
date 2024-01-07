package com.training.socialnetwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.util.constant.Constant;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

	@Autowired
	private IFriendService friendService;

	@GetMapping(value = "/all-friends/{userId}")
	public List<FriendListDto> getFriendList(@PathVariable(value = "userId") int userId) {

		return friendService.findAllFriendWithStatus(userId);
	}

	@PostMapping(value = "/add-friend")
	public ResponseEntity<Object> createFriendRequest(@RequestParam(value = "userId1") int userId1,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		boolean result = friendService.createFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "/accept-friend")
	public ResponseEntity<Object> acceptFriendRequest(@RequestParam(value = "userId1") int userId1,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		boolean result = friendService.acceptFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "/refuse-friend")
	public ResponseEntity<Object> refuseFriendRequest(@RequestParam(value = "userId1") int userId1,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		boolean result = friendService.refuseFriendRequest(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@PostMapping(value = "remove-friend")
	public ResponseEntity<Object> removeFriend(@RequestParam(value = "userId1") int userId1,
			@RequestParam(value = "userId2") int userId2) throws Exception {
		boolean result = friendService.unfriend(userId1, userId2);

		if (result) {
			return new ResponseEntity<Object>(result, HttpStatus.CREATED);
		}

		throw new Exception(Constant.SERVER_ERROR);
	}

	@GetMapping(value = "all-friend-request/{userId}")
	public List<FriendRequestDto> getFriendRequestList(@PathVariable(value = "userId") int userId) {

		return friendService.findAllAddFriendRequest(userId);
	}
}
