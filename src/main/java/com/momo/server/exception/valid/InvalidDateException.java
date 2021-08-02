package com.momo.server.exception.valid;

import com.momo.server.exception.CommonException;

public class InvalidDateException extends CommonException {

    public InvalidDateException() {
        super("과거의 날짜를 시작날짜로 지정할 수 없습니다.");
    }

}