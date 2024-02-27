package com.training.socialnetwork.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

public interface IFriendService {

	Map<String, Object> findAllFriendWithStatus(int userId, Pageable paging);
	
	Map<String, Object> findAllAddFriendRequest(int userId, Pageable paging);
	
	int createFriendRequest(int sentUserId, int recievedUserId) throws Exception;
	
	boolean acceptFriendRequest(int sentUserId, int recievedUserId) throws Exception;
	
	boolean refuseFriendRequest(int sentUserId, int recievedUserId);
	
	boolean unfriend(int friendUserId, int loggedInUserId);
	
	boolean removeFriendRequest(int sentUserId, int recievedUserId) throws Exception;
}
