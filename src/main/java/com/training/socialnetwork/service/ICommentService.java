package com.training.socialnetwork.service;

import com.training.socialnetwork.entity.Comment;

public interface ICommentService {
	
	Comment createComment(Comment comment);
	
	boolean updateComment(Comment comment, int userId);

	boolean deleteComment(int commentId, int userId);
}
