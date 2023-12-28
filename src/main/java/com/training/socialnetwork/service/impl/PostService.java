package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.training.socialnetwork.entity.PostEntity;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.service.IPostService;

public class PostService implements IPostService{

	@Autowired
	private PostRepository postRepository;
	
	@Override
	public void createPost(PostEntity postEntity) {
		postRepository.save(postEntity);
	}

	@Override
	public List<PostEntity> getListPost() {
		List<PostEntity> result = new ArrayList<PostEntity>();
		result = postRepository.findAll();
		return result;
	}

	@Override
	public PostEntity getPost(int postId) {
		Optional<PostEntity> optional = postRepository.findById(postId);
		PostEntity postEntity = optional.get();
		return postEntity;
	}

	@Override
	public void updatePost(PostEntity postEntity, int postId) {
		postEntity.setPostId(postId);
		postRepository.save(postEntity);
	}

	@Override
	public void deletePost(int postId) {
		postRepository.deleteById(postId);
	}

}
