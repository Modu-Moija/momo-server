package com.momo.server.exception.auth;

import com.momo.server.exception.CommonException;

@SuppressWarnings("serial")
public class UnauthorizedException extends CommonException {

    public UnauthorizedException() {
	super("로그인이 되어 있지 않습니다. 로그인 후에 실행해주세요");
    }

}