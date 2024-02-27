package com.training.socialnetwork.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ILikeService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@Service
@Transactional
public class LikeService implements ILikeService {

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean addLikePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null || post.getDeleteFlg() == Constant.DELETED_FlG) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}
		
		Like like = likeRepository.findByPostAndUser(post, user);
		if (like != null && like.getDeleteFlg() == Constant.DELETED_FlG) {
			
			throw new CustomException(HttpStatus.BAD_REQUEST, "You have liked this post");
		} else {
			like = new Like();
			like.setPost(post);
			like.setUser(user);
			like.setDeleteFlg(Constant.UNDELETED_FLG);
			like.setCreateDate(new Date());
			like.setUpdateDate(new Date());
			
			return likeRepository.save(like) != null;
		}
	}

	@Override
	public boolean unlikePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null || post.getDeleteFlg() == Constant.DELETED_FlG) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		Like like = likeRepository.findByPostAndUser(post, user);
		
		if (like != null && like.getDeleteFlg() == Constant.UNDELETED_FLG) {
			like.setDeleteFlg(Constant.DELETED_FlG);
			like.setUpdateDate(new Date());

			return likeRepository.save(like) != null;
		} else {
			throw new CustomException(HttpStatus.BAD_REQUEST, "You haven't liked this post");
		}
	}

}
