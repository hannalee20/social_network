package com.training.socialnetwork.dto.request.post;

import java.io.Serializable;

public class PostCreateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;
	private String photoUrl;

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
