package com.training.socialnetwork.service;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;

public interface ICommentService {
	
	CommentCreatedDto createComment(CommentCreateDto comment) throws Exception;
	
	CommentCreatedDto updateComment(CommentUpdateDto comment, int userId) throws Exception;

	boolean deleteComment(int commentId, int userId) throws Exception;
	
	CommentDetailDto getCommentDetail(int commentId) throws Exception;
}
