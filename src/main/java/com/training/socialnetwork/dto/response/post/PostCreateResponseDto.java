package com.training.socialnetwork.dto.response.post;

import java.util.List;

public class PostCreateResponseDto {

	private int postId;
	private int userId;
	private String username;
	private String content;
	private List<Integer> photoIdList;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Integer> getPhotoIdList() {
		return photoIdList;
	}

	public void setPhotoIdList(List<Integer> photoIdList) {
		this.photoIdList = photoIdList;
	}

}
