package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@Service
@Transactional
public class FriendService implements IFriendService {

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<FriendListDto> findAllFriendWithStatus(int userId, Pageable paging) {
		List<Friend> friendList = friendRepository.findAllFriendByUserIdAndStatus(userId, Constant.FRIENDED_STATUS,
				paging);

		List<FriendListDto> friendListDtos = new ArrayList<>();

		for (Friend friend : friendList) {
			FriendListDto friendListDto = new FriendListDto();
			friendListDto.setFriendId(friend.getFriendId());
			if (friend.getUser1().getUserId() == userId) {
				friendListDto.setUserId(friend.getUser2().getUserId());
				friendListDto.setUsername(friend.getUser2().getUsername());
				friendListDto.setAvatar(friend.getUser2().getAvatarUrl());
			} else {
				friendListDto.setUserId(friend.getUser1().getUserId());
				friendListDto.setUsername(friend.getUser1().getUsername());
				friendListDto.setAvatar(friend.getUser1().getAvatarUrl());
			}

			friendListDtos.add(friendListDto);
		}
		return friendListDtos;
	}

	@Override
	public boolean createFriendRequest(int userId1, int userId2) throws Exception {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user2 == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendByUser1AndUser2(userId1, userId2);

		if (friend == null) {
			friend = new Friend();
			friend.setUser1(user1);
			friend.setUser2(user2);
		}
		friend.setStatus(Constant.FRIEND_REQUEST);
		friend.setCreateDate(new Date());
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean acceptFriendRequest(int userId1, int userId2) throws Exception {
		User user1 = userRepository.findById(userId1).orElse(null);

		if (user1 == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(userId1, userId2);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.FRIENDED_STATUS);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean refuseFriendRequest(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		
		if (user1 == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(userId1, userId2);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean unfriend(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user1 == null || user2 == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIENDED_STATUS);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public List<FriendRequestDto> findAllAddFriendRequest(int userId, Pageable paging) {
		List<Friend> friendRequestList = friendRepository.findAllFriendRequest(userId, Constant.FRIEND_REQUEST, paging);

		List<FriendRequestDto> friendRequestDtos = new ArrayList<>();

		for (Friend friendRequest : friendRequestList) {
			FriendRequestDto friendRequestDto = new FriendRequestDto();
			friendRequestDto.setUserId(friendRequest.getUser1().getUserId());
			friendRequestDto.setUsername(friendRequest.getUser1().getUsername());
			friendRequestDto.setAvatar(friendRequest.getUser1().getAvatarUrl());
			friendRequestDtos.add(friendRequestDto);
		}
		return friendRequestDtos;
	}

	@Override
	public boolean removeFriendRequest(int userId1, int userId2) throws Exception {
		User user1 = userRepository.findById(userId1).orElse(null);

		if (user1 == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(userId1, userId2);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

}
