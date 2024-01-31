package com.training.socialnetwork.util.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;
	private String message;

	public CustomException(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
