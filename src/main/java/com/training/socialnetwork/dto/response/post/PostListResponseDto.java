package com.training.socialnetwork.dto.response.post;

import java.util.Date;
import java.util.List;

public class PostListResponseDto {

	private int postId;
	private int userId;
	private String username;
	private String content;
	private List<Integer> photoIdList;
	private Date createDate;
	private int likeCount;
	private int commentCount;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

}
