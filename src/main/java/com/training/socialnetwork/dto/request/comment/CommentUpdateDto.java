package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CommentUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int commentId;
	private int postId;
	private int userId;
	private String content;
	private String photoUrl;

	@NotNull
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

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
