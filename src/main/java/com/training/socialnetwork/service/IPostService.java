package com.training.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.training.socialnetwork.dto.request.post.PostCreateRequestDto;
import com.training.socialnetwork.dto.request.post.PostUpdateRequestDto;
import com.training.socialnetwork.dto.response.post.PostCreateResponseDto;
import com.training.socialnetwork.dto.response.post.PostDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostListResponseDto;
import com.training.socialnetwork.dto.response.post.PostUpdateResponseDto;

public interface IPostService {
	
	PostCreateResponseDto createPost(int userId, PostCreateRequestDto postCreateDto) throws Exception;
	
	Page<PostListResponseDto> getTimeline(int userId, Pageable page);
	
	PostDetailResponseDto getPost(int postId) throws Exception;
	
	PostUpdateResponseDto updatePost(PostUpdateRequestDto postUpdateDto, int postId, int userId) throws Exception;
	
	boolean deletePost(int postId, int userId) throws Exception;

}
