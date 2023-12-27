package com.training.socialnetwork.service;

import java.util.List;

import com.training.socialnetwork.entity.PostEntity;

public interface IPostService {
	
	public void createPost(PostEntity postEntity);
	
	public List<PostEntity> getListPost();
	
	public PostEntity getPost(int postId);
	
	public void updatePost(PostEntity postEntity, int postId);

}
