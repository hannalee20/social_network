package com.training.socialnetwork.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.training.socialnetwork.dto.response.friend.FriendListDto;
import com.training.socialnetwork.dto.response.friend.FriendRequestDto;

public interface IFriendService {

	List<FriendListDto> findAllFriendWithStatus(int userId, Pageable paging);
	
	List<FriendRequestDto> findAllAddFriendRequest(int userId, Pageable paging);
	
	boolean createFriendRequest(int userId1, int userId2) throws Exception;
	
	boolean acceptFriendRequest(int userId1, int userId2) throws Exception;
	
	boolean refuseFriendRequest(int userId1, int userId2);
	
	boolean unfriend(int userId1, int userId2);
	
	int countFriend(int userId);
	
	boolean removeFriendRequest(int userId1, int userId2) throws Exception;
}
