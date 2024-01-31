package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.training.socialnetwork.util.constant.Constant;

public class CommentCreateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postId;
	private String content;

	@NotBlank
	@Pattern(regexp = "^[0-9]*$", message = Constant.INVALID)
	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	@NotBlank(message = Constant.INVALID)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
