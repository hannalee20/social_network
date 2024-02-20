package com.training.socialnetwork.dto.request.post;

import java.io.Serializable;
import java.util.List;

public class PostCreateRequestDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
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
