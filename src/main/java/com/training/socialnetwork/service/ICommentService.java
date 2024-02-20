package com.training.socialnetwork.service;

import com.training.socialnetwork.dto.request.comment.CommentCreateRequestDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentCreateResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdateResponseDto;

public interface ICommentService {
	
	CommentCreateResponseDto createComment(int userId, CommentCreateRequestDto commentCreateDto) throws Exception;
	
	CommentUpdateResponseDto updateComment(CommentUpdateRequestDto commentUpdateDto, int commentId, int userId) throws Exception;

	boolean deleteComment(int commentId, int userId) throws Exception;
	
	CommentDetailResponseDto getCommentDetail(int commentId) throws Exception;
	
}
