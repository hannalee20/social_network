package com.training.socialnetwork.service;

public interface ILikeService {

	boolean addLikePost(int postId, int userId) throws Exception;
	
	boolean unlikePost(int postId, int userId) throws Exception;
	
	int countLike(int userId);
}
