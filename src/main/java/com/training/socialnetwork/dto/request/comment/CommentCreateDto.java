package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CommentCreateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postId;
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
