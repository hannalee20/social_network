package com.training.socialnetwork.service;

public interface ILikeService {

	boolean updatePostLike(int postId, int userId) throws Exception;
}
