package com.momo.server.exception;

public abstract class CommonException extends RuntimeException implements ResultCodeSupport {

    public CommonException() {
    }

    public CommonException(String message) {
	super(message);
    }

}
