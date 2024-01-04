package com.training.socialnetwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ICommentService;

public class CommentService implements ICommentService{
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public Comment createComment(Comment comment) {
		Post post = postRepository.findById(comment.getPost().getPostId()).orElse(null);
		User user = userRepository.findById(comment.getUser().getUserId()).orElse(null);
		
		if (post == null || user == null) {
			return null;
		}
		
		return commentRepository.save(comment);
	}

	@Override
	public boolean updateComment(Comment comment, int userId) {
		Post post = postRepository.findById(comment.getPost().getPostId()).orElse(null);
		User user = userRepository.findById(comment.getUser().getUserId()).orElse(null);
		
		if (post == null || user == null || comment.getUser().getUserId() != userId) {
			return false;
		}
		return commentRepository.save(comment) != null;
	}

	@Override
	public boolean deleteComment(int commentId, int userId) {
		Comment comment = commentRepository.findById(commentId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);
		
		if (comment == null || user == null || comment.getUser().getUserId() != userId) {
			return false;
		}
		comment.setDeleteFlg(1);
		return commentRepository.save(comment) != null;
	}

}
