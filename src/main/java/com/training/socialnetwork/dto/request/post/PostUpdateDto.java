package com.training.socialnetwork.dto.request.post;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class PostUpdateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postId;
	private int userId;
	private String content;
	private String photoUrl;

	@NotNull
	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	@NotNull
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
