package com.training.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;

public interface ICommentService {
	
	CommentCreatedDto createComment(int userId, CommentCreateDto commentCreateDto, MultipartFile photo) throws Exception;
	
	CommentUpdatedDto updateComment(CommentUpdateDto comment, int commentId, int userId) throws Exception;

	boolean deleteComment(int commentId, int userId) throws Exception;
	
	CommentDetailDto getCommentDetail(int commentId) throws Exception;
	
//	int countComment(int userId);
}
