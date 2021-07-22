package com.momo.server.exception;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class UserNotFoundException extends CommonException {

    public UserNotFoundException(BigInteger userId) {
	super("해당 아이디의 유저를 찾을 수 없습니다. 유저ID : " + userId);
    }

}
