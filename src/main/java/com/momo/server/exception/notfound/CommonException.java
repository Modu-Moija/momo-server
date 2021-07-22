package com.momo.server.exception.notfound;

import com.momo.server.exception.ResultCodeSupport;

@SuppressWarnings("serial")
public abstract class CommonException extends RuntimeException implements ResultCodeSupport {

    public CommonException() {
    }

    public CommonException(String message) {
	super(message);
    }

}
