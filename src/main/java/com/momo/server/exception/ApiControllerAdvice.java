package com.momo.server.exception;

import com.momo.server.exception.outofbound.DatesOutOfBoundsException;
import com.momo.server.exception.outofbound.UsersOutOfBoundsException;
import com.momo.server.exception.validation.InvalidDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.momo.server.dto.CmRespDto;
import com.momo.server.exception.auth.UnauthorizedException;
import com.momo.server.exception.validation.InvalidMeetException;
import com.momo.server.exception.validation.InvalidUsernameExecption;
import com.momo.server.exception.validation.InvalidUserTimeExcpetion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleNotFoundException(CommonException e) {
        log.info("CommonException", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUsernameExecption.class)
    public ResponseEntity<?> userNamehandleValidException(InvalidUsernameExecption e) {
        log.info("ValidException", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), e.getErrorMap()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidMeetException.class)
    public ResponseEntity<?> meetSavehandleValidException(InvalidMeetException e) {
        log.info("ValidException", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), e.getErrorMap()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserTimeExcpetion.class)
    public ResponseEntity<?> userTimeUpdatehandleValidException(InvalidUserTimeExcpetion e) {
        log.info("ValidException", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), e.getErrorMap()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        log.info("권한없음 Exception", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsersOutOfBoundsException.class)
    public ResponseEntity<?> handleUsersOutOfBoundsException(UsersOutOfBoundsException e) {
        log.info("인원수 초과", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DatesOutOfBoundsException.class)
    public ResponseEntity<?> handleDatesOutOfBoundsException(DatesOutOfBoundsException e) {
        log.info("예약날짜수 초과", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<?> handleInvalidDateException(InvalidDateException e) {
        log.info("과거 날짜 약속 생성 시도", e);

        ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(new CmRespDto<>(responseCode, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

}
