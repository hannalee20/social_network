package com.training.socialnetwork.dto.response.user;

public class UserReportResponseDto {

	private int postCount;
	private int commentCount;
	private int friendCount;
	private int likeCount;

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

}
