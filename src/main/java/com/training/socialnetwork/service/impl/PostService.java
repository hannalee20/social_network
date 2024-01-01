package com.training.socialnetwork.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IPostService;

public class PostService implements IPostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void createPost(Post post) {
		postRepository.save(post);
	}

	@Override
	public List<Post> getAllPosts(int userId) {
		List<Post> postList = postRepository.findAllByUserUserIdOrderByUpdateDateDesc(userId);
		postList.stream().peek(post ->{
			List<Comment> commentList = post.getCommentList()
					.stream()
					.sorted((comment1, comment2) -> {
						if (comment1.getUpdateDate().after(comment2.getUpdateDate())) {
							return 1;
						} else if (comment1.getUpdateDate().before(comment2.getUpdateDate())) {
							return -1;
						}
						return 0;
					}).collect(Collectors.toList());
			post.setCommentList(commentList);
		}).collect(Collectors.toList());
		return postList;
	}

	@Override
	public Post getPost(int postId) {
		Optional<Post> optional = postRepository.findById(postId);
		Post post = optional.get();
		return post;
	}

	@Override
	public boolean updatePost(Post post, int userId) {
		User user = userRepository.findById(post.getUser().getUserId()).orElse(null);
		if(user.getUserId() != userId) {
			return false;
		}

		return postRepository.save(post) != null;
	}

	@Override
	public boolean deletePost(int postId, int userId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post.getUser().getUserId() != userId) {
			return false;
		}
		post.setDeleteFlg(1);
		return postRepository.save(post) != null;
	}

}
