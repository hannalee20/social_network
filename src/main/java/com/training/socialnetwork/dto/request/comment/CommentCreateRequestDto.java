package com.training.socialnetwork.dto.request.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.training.socialnetwork.util.constant.Constant;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommentCreateRequestDto {

	@NotBlank
	@Pattern(regexp = "^[0-9]*$", message = Constant.INVALID)
	private int postId;

	@Schema(type = "string", example = " ")
	private String content;

	private Integer photoId;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

}
