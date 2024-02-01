package com.training.socialnetwork.dto.response.post;

import java.util.Date;
import java.util.List;

import com.training.socialnetwork.dto.response.comment.CommentDetailDto;

public class PostDetailDto {

	private int postId;
	private int userId;
	private String username;
	private String content;
	private List<String> photoUrl;
	private Date createDate;
	private int likeCount;
	private List<CommentDetailDto> commentList;

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

	public List<String> getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(List<String> photoUrl) {
		this.photoUrl = photoUrl;
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

	public List<CommentDetailDto> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CommentDetailDto> commentList) {
		this.commentList = commentList;
	}

}
