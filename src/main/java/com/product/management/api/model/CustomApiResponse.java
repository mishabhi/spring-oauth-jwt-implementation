package com.product.management.api.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL) 
public class CustomApiResponse {
	
	private String message;
	
	@JsonProperty("response")
	private Object customResponse;
	
	private HttpStatus statusCode;
	
	public CustomApiResponse setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public CustomApiResponse setCustomResponse(Object customResponse) {
		this.customResponse = customResponse;
		return this;
	}
	
	public Object getCustomResponse() {
		return this.customResponse;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public CustomApiResponse setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
		return this;
	}
}
