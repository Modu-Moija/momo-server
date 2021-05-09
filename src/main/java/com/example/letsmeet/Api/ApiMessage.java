package com.example.letsmeet.Api;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiMessage {
	
	private HttpStatus status;
	private String message;
	
	public ApiMessage(String message, HttpStatus status){
		this.status = status;
		this.message = message;
	}

}
