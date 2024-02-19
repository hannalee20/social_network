package com.training.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;

public interface IFriendService {

	Page<FriendListDto> findAllFriendWithStatus(int userId, Pageable paging);
	
	Page<FriendRequestDto> findAllAddFriendRequest(int userId, Pageable paging);
	
	boolean createFriendRequest(int sentUserId, int recievedUserId) throws Exception;
	
	boolean acceptFriendRequest(int sentUserId, int recievedUserId) throws Exception;
	
	boolean refuseFriendRequest(int sentUserId, int recievedUserId);
	
	boolean unfriend(int friendUserId, int loggedInUserId);
	
	boolean removeFriendRequest(int sentUserId, int recievedUserId) throws Exception;
}
