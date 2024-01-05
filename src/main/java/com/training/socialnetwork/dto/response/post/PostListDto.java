package com.training.socialnetwork.dto.response.post;

import java.util.Date;

public class PostListDto {

	private int postId;
	private int userId;
	private String username;
	private String content;
	private String photoUrl;
	private Date createDate;
	private int likeTotal;
	private int commentTotal;

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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getLikeTotal() {
		return likeTotal;
	}

	public void setLikeTotal(int likeTotal) {
		this.likeTotal = likeTotal;
	}

	public int getCommentTotal() {
		return commentTotal;
	}

	public void setCommentTotal(int commentTotal) {
		this.commentTotal = commentTotal;
	}

}
