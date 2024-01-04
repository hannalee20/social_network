package com.training.socialnetwork.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.Friend;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IFriendService;

@Service
public class FriendService implements IFriendService{
	
	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private UserRepository userRepository;
	@Override
	public List<Friend> findAllFriendWithStatus(int userId) {
		return friendRepository.findAllFriendByUserIdAndStatus(userId, 1);
	}

	@Override
	public boolean createFriendRequest(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);
		
		if(user1 == null || user2 == null) {
			return false;
		}
		
		Friend friend = friendRepository.findFriendByUser1AndUser2(userId1, userId2);
		
		if(friend == null) {
			friend = new Friend();
			friend.setUser1(user1);
			friend.setUser2(user2);
		}
		friend.setStatus(0);
		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean acceptFriendRequest(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);
		
		if(user1 == null || user2 == null) {
			return false;
		}
		
		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, 0);
		
		if (friend == null) {
			return false;
		}
		
		friend.setStatus(1);
		
		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean refuseFriendRequest(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);
		
		if(user1 == null || user2 == null) {
			return false;
		}
		
		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, 0);
		
		if (friend == null) {
			return false;
		}
		
		friend.setStatus(2);
		
		return friendRepository.save(friend) != null;
	}

	@Override
	public boolean unfriend(int userId1, int userId2) {
		User user1 = userRepository.findById(userId1).orElse(null);
		User user2 = userRepository.findById(userId2).orElse(null);
		
		if(user1 == null || user2 == null) {
			return false;
		}
		
		Friend friend = friendRepository.findFriendByUserIdAndStatus(userId1, userId2, 1);
		
		if (friend == null) {
			return false;
		}
		
		friend.setStatus(2);
		
		return friendRepository.save(friend) != null;
	}

}
