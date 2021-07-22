package com.momo.server.exception.valid;

import java.util.Map;

@SuppressWarnings("serial")
public class InvalidUsernameExecption extends RuntimeException {

    private Map<String, String> errorMap;

    public InvalidUsernameExecption(String message, Map<String, String> errorMap) {
	super(message);
	this.errorMap = errorMap;
    }

    public InvalidUsernameExecption(String message) {
	super(message);
    }

    public Map<String, String> getErrorMap() {
	return errorMap;
    }
}
