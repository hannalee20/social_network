package com.training.socialnetwork.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;
import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IFriendService;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class FriendService implements IFriendService {

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<FriendListDto> findAllFriendWithStatus(int userId) {
		List<Friend> friendList = friendRepository.findAllFriendByUserIdAndStatus(userId, Constant.FRIENDED_STATUS);

		return friendList.stream().map(friend -> modelMapper.map(friend, FriendListDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public boolean createFriendRequest(int userId1, int userId2) throws Exception {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user1 == null || user2 == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Friend friend = friendRepository.findFriendByUser1AndUser2(userId1, userId2);

		if (friend == null) {
			friend = new Friend();
			friend.setUser1(user1);
			friend.setUser2(user2);
		}
		friend.setStatus(Constant.FRIEND_REQUEST);
		
		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean acceptFriendRequest(int userId1, int userId2) throws Exception {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user1 == null || user2 == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIEND_REQUEST);

		if (friend == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		friend.setStatus(Constant.FRIENDED_STATUS);

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean refuseFriendRequest(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user1 == null || user2 == null) {
			return false;
		}

		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIEND_REQUEST);

		if (friend == null) {
			return false;
		}

		friend.setStatus(Constant.NOT_FRIEND);

		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean unfriend(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);

		if (user1 == null || user2 == null) {
			return false;
		}

		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, Constant.FRIENDED_STATUS);

		if (friend == null) {
			return false;
		}

		friend.setStatus(Constant.NOT_FRIEND);

		return friendRepository.save(friend) != null;
	}

	@Override
	public List<FriendRequestDto> findAllAddFriendRequest(int userId) {
		List<Friend> friendRequestList = friendRepository.findAllFriendByUserIdAndStatus(userId, Constant.FRIEND_REQUEST);

		return friendRequestList.stream().map(friend -> modelMapper.map(friend, FriendRequestDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public int countFriend(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		return friendRepository.countFriend(userId, dateStart, dateEnd);
	}

}
