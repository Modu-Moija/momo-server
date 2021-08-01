package com.momo.server.exception.valid;

import com.momo.server.exception.CommonException;

import java.util.Map;

public class UsersOutOfBoundsException extends CommonException {

    public UsersOutOfBoundsException(String meetId) {
        super("해댱 약속의 최대 인원수(30)를 초과하였습니다. : " + meetId);
    }

}