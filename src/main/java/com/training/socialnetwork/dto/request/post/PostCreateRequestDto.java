package com.training.socialnetwork.dto.request.post;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostCreateRequestDto {
	
	@Schema(type = "string", example = " ")
	private String content;
	private List<Integer> photoIdList;

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

}
