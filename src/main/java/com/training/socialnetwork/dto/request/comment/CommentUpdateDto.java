package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class CommentUpdateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;
	private MultipartFile photoUrl;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(MultipartFile photoUrl) {
		this.photoUrl = photoUrl;
	}

}
