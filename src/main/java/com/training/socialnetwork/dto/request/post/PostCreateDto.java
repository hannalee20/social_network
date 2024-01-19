package com.training.socialnetwork.dto.request.post;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class PostCreateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;
	private MultipartFile[] photos;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile[] getPhotos() {
		return photos;
	}

	public void setPhotos(MultipartFile[] photos) {
		this.photos = photos;
	}

}
