package com.training.socialnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.training.socialnetwork.dto.request.post.PostCreateDto;
import com.training.socialnetwork.dto.request.post.PostUpdateDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;

public interface IPostService {
	
	PostCreatedDto createPost(int userId, PostCreateDto postCreateDto) throws Exception;
	
	Page<PostListDto> getTimeline(int userId, Pageable page);
	
	PostDetailDto getPost(int postId) throws Exception;
	
	PostUpdatedDto updatePost(PostUpdateDto postUpdateDto, int postId, int userId) throws Exception;
	
	boolean deletePost(int postId, int userId) throws Exception;

}
