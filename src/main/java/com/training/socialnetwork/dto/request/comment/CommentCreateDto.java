package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CommentCreateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postId;
	private String content;

	@NotBlank(message = "The postId is required")
	@Pattern(regexp = "^[0-9]*$", message = "The postId is invalid..")
	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	@NotBlank
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
