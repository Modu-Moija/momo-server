package com.momo.server.exception.valid;

import java.util.Map;

@SuppressWarnings("serial")
public class MeetSaveValidException extends RuntimeException {

    private Map<String, String> errorMap;

    public MeetSaveValidException(String message, Map<String, String> errorMap) {
	super(message);
	this.errorMap = errorMap;
    }

    public MeetSaveValidException(String message) {
	super(message);
    }

    public Map<String, String> getErrorMap() {
	return errorMap;
    }
}
