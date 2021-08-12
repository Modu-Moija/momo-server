package com.momo.server.exception.outofbound;

import com.momo.server.exception.CommonException;

public class UsersOutOfBoundsException extends CommonException {

    public UsersOutOfBoundsException(String meetId) {
        super("해댱 약속의 최대 인원수(30)를 초과하였습니다. : " + meetId);
    }
}