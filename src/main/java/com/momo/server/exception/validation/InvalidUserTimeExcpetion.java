package com.momo.server.exception.validation;

import java.util.Map;

@SuppressWarnings("serial")
public class InvalidUserTimeExcpetion extends RuntimeException {

    private Map<String, String> errorMap;

    public InvalidUserTimeExcpetion(String message, Map<String, String> errorMap) {
	super(message);
	this.errorMap = errorMap;
    }

    public InvalidUserTimeExcpetion(String message) {
	super(message);
    }

    public Map<String, String> getErrorMap() {
	return errorMap;
    }
}