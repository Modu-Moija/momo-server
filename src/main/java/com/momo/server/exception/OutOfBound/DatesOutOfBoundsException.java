package com.momo.server.exception.outofbound;

import com.momo.server.exception.CommonException;

public class DatesOutOfBoundsException extends CommonException {

    public DatesOutOfBoundsException() {
        super("최대 31일의 날짜를 약속으로 생성할 수 있습니다. 약속 날짜를 줄이십시오.");
    }
}