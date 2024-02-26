package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.response.friend.FriendListResponseDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestListResponseDto;
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
	public Page<FriendListResponseDto> findAllFriendWithStatus(int userId, Pageable paging) {
		Page<Friend> friendList = friendRepository.findAllFriendByUserIdAndStatus(userId, Constant.FRIENDED_STATUS,
				paging);

		List<FriendListResponseDto> friendListDtos = new ArrayList<>();

		for (Friend friend : friendList) {
			FriendListResponseDto friendListDto = new FriendListResponseDto();
			friendListDto.setFriendId(friend.getFriendId());
			if (friend.getSentUser().getUserId() == userId) {
				friendListDto.setUserId(friend.getReceivedUser().getUserId());
				friendListDto.setUsername(friend.getReceivedUser().getUsername());
//				friendListDto.setAvatar(friend.getReceivedUser().getAvatarUrl());
			} else {
				friendListDto.setUserId(friend.getSentUser().getUserId());
				friendListDto.setUsername(friend.getSentUser().getUsername());
//				friendListDto.setAvatar(friend.getSentUser().getAvatarUrl());
			}

			friendListDtos.add(friendListDto);
		}
		Page<FriendListResponseDto> result = new PageImpl<FriendListResponseDto>(friendListDtos);
		return result;
	}

	@Override
	public int createFriendRequest(int sentUserId, int recievedUserId) throws Exception {
		User sentUser = userRepository.findById(sentUserId).orElse(null);
		User recievedUser = userRepository.findById(recievedUserId).orElse(null);

		if (recievedUser == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendBySentUserAndReceivedUser(sentUserId, recievedUserId);

		if (friend == null) {
			friend = new Friend();
			friend.setSentUser(sentUser);
			friend.setReceivedUser(recievedUser);
			friend.setStatus(Constant.FRIEND_REQUEST);
		} else {
			if (friend.getStatus() == Constant.NUMBER_0 && friend.getSentUser().getUserId() == sentUserId) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "You have already sent a friend request");
			} else if (friend.getStatus() == Constant.NUMBER_1) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "You are already friends");
			} else if (friend.getStatus() == Constant.NUMBER_0 && friend.getReceivedUser().getUserId() == sentUserId) {
				friend.setStatus(Constant.FRIENDED_STATUS);
			}
		}
		
		friend.setCreateDate(new Date());
		friend.setUpdateDate(new Date());

		friend = friendRepository.save(friend);
		
		return friend.getStatus();
	}

	@Override
	public boolean acceptFriendRequest(int sentUserId, int recievedUserId) throws Exception {
		User sentUser = userRepository.findById(sentUserId).orElse(null);

		if (sentUser == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(sentUserId, recievedUserId);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.FRIENDED_STATUS);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean refuseFriendRequest(int sentUserId, int recievedUserId) {
		User sentUser = userRepository.findById(sentUserId).orElse(null);

		if (sentUser == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(sentUserId, recievedUserId);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean unfriend(int friendUserId, int loggedInUserId) {
		User friendUser = userRepository.findById(friendUserId).orElse(null);
		User loggedInUser = userRepository.findById(loggedInUserId).orElse(null);

		if (friendUser == null || loggedInUser == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendByUserIdAndStatus(friendUserId, loggedInUserId,
				Constant.FRIENDED_STATUS);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

	@Override
	public Page<FriendRequestListResponseDto> findAllAddFriendRequest(int userId, Pageable paging) {
		Page<Friend> friendRequestList = friendRepository.findAllFriendRequest(userId, Constant.FRIEND_REQUEST, paging);

		List<FriendRequestListResponseDto> friendRequestDtos = new ArrayList<>();

		for (Friend friendRequest : friendRequestList) {
			FriendRequestListResponseDto friendRequestDto = new FriendRequestListResponseDto();
			friendRequestDto.setUserId(friendRequest.getSentUser().getUserId());
			friendRequestDto.setUsername(friendRequest.getSentUser().getUsername());
			friendRequestDtos.add(friendRequestDto);
		}
		Page<FriendRequestListResponseDto> result = new PageImpl<FriendRequestListResponseDto>(friendRequestDtos);
		
		return result;
	}

	@Override
	public boolean removeFriendRequest(int sentUserId, int recievedUserId) throws Exception {
		User sentUser = userRepository.findById(sentUserId).orElse(null);

		if (sentUser == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "User does not exist");
		}

		Friend friend = friendRepository.findFriendRequestByUserId(sentUserId, recievedUserId);

		if (friend == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Friend request does not exist");
		}

		friend.setStatus(Constant.NOT_FRIEND);
		friend.setUpdateDate(new Date());

		return friendRepository.save(friend) != null;
	}

}
