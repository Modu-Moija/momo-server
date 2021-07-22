package com.momo.server.exception;

@SuppressWarnings("serial") // https://zzznara2.tistory.com/186, 서프레스 워닝 해결
public class MeetNotFoundException extends CommonException {

    public MeetNotFoundException(String meetId) {
	super("해당 아이디의 약속을 찾을 수 없습니다. 약속ID : " + meetId);
    }

}