package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.entity.Post;

public interface IPostService {
	
	boolean createPost(Post post);
	
	List<Post> getAllPosts(int userId);
	
	Post getPost(int postId);
	
	Post updatePost(Post post, int userId);
	
	boolean deletePost(int postId, int userId);

}
