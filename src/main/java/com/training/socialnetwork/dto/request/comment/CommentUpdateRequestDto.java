package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommentUpdateRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(type = "string", example = " ")
	private String content;

	private Integer photoId;

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
