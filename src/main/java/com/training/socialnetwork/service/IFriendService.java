package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.entity.Friend;

public interface IFriendService {

	List<Friend> findAllFriendWithStatus(int userId);
	
	boolean createFriendRequest(int userId1, int userId2);
	
	boolean acceptFriendRequest(int userId1, int userId2);
	
	boolean refuseFriendRequest(int userId1, int userId2);
	
	boolean unfriend(int userId1, int userId2);
}
