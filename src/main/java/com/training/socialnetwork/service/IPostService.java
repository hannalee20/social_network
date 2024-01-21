package com.training.socialnetwork.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;

public interface IPostService {
	
	PostCreatedDto createPost(int userId, String content, MultipartFile[] photos) throws Exception;
	
	List<PostListDto> getTimeline(int userId, Pageable page);
	
	PostDetailDto getPost(int postId) throws Exception;
	
	PostUpdatedDto updatePost(String content, MultipartFile[] photos, int postId, int userId) throws Exception;
	
	boolean deletePost(int postId, int userId) throws Exception;
	
	int countPost(int userId);

}
