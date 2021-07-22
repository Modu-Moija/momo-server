package com.momo.server.exception.valid;

import java.util.Map;

@SuppressWarnings("serial")
public class UsernameValidExecption extends RuntimeException {

    private Map<String, String> errorMap;

    public UsernameValidExecption(String message, Map<String, String> errorMap) {
	super(message);
	this.errorMap = errorMap;
    }

    public UsernameValidExecption(String message) {
	super(message);
    }

    public Map<String, String> getErrorMap() {
	return errorMap;
    }
}
