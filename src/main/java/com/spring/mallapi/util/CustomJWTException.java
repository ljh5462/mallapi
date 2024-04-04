package com.spring.mallapi.util;

public class CustomJWTException extends RuntimeException{
	
	public CustomJWTException(String msg) {
		super(msg);
	}
}
