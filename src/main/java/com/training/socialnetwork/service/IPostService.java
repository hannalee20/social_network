package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.entity.Post;

public interface IPostService {
	
	void createPost(Post post);
	
	List<Post> getAllPosts(int userId);
	
	Post getPost(int postId);
	
	boolean updatePost(Post post, int userId);
	
	boolean deletePost(int postId, int userId);

}
