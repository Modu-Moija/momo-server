package com.momo.server.exception.notfound;

import java.math.BigInteger;

import com.momo.server.exception.CommonException;

@SuppressWarnings("serial")
public class UserNotFoundException extends CommonException {

    public UserNotFoundException(BigInteger userId) {
        super("해당 아이디의 유저를 찾을 수 없습니다. 유저ID : " + userId);
    }
}
