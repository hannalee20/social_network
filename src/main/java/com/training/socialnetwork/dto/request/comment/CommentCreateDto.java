package com.training.socialnetwork.dto.request.comment;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

public class CommentCreateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int postId;
	private String content;
	private MultipartFile photo;

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

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

}
