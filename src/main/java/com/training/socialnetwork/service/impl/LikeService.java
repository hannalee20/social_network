package com.training.socialnetwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ILikeService;

@Service
public class LikeService implements ILikeService{

	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean updatePostLike(int postId, int userId) {
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);
		
		if(post == null || user == null) {
			return false;
		}
		
		Like likeEntity = likeRepository.findByPostAndUser(post, user);
		
		if (likeEntity == null) {
			likeEntity = new Like();
			likeEntity.setPost(post);
			likeEntity.setUser(user);
			likeEntity.setDeleteFlg(0);
		} else {
			likeEntity.setDeleteFlg(1);
		}
		
		return likeRepository.save(likeEntity) != null;
	}
	
	

}
