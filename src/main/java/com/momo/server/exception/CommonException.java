package com.momo.server.exception;

@SuppressWarnings("serial")
public abstract class CommonException extends RuntimeException implements ResultCodeSupport {

    public CommonException(String message) {
	super(message);
    }

}
