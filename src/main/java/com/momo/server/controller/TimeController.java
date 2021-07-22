package com.momo.server.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.User;
import com.momo.server.dto.CmRespDto;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.MostLeastTimeRespDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.exception.auth.UnauthorizedException;
import com.momo.server.exception.valid.InvalidUserTimeExcpetion;
import com.momo.server.service.TimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/time")
public class TimeController {

    private final TimeService timeService;

    @PutMapping
    public ResponseEntity<?> updateUsertime(HttpServletRequest request,
	    @RequestBody @Valid UserTimeUpdateRequestDto requestDto, BindingResult bindingResult) {// @CookieValue(value="gender",
												   // required=false)

	// 추후에 aop로 다 뺄예정
	if (bindingResult.hasErrors()) {
	    Map<String, String> errorMap = new HashMap<>();

	    for (FieldError error : bindingResult.getFieldErrors()) {
		errorMap.put(error.getField(), error.getDefaultMessage());
	    }
	    throw new InvalidUserTimeExcpetion("유효성 검사 실패", errorMap);
	}

	// 세션에서 유저이름찾기
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("user");
	Optional.ofNullable(user).orElseThrow(() -> new UnauthorizedException());

	// System.out.println(user);
	timeService.updateUsertime(user, requestDto);

	ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.OK);
	return new ResponseEntity<>(new CmRespDto<>(responseCode, "시간 수정 성공", null), HttpStatus.OK);
    };

    @GetMapping("/usertime")
    public UserMeetRespDto getUserTime(HttpServletRequest request) {

	// 세션에서 유저찾기
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("user");
	Optional.ofNullable(user).orElseThrow(() -> new UnauthorizedException());

	UserMeetRespDto userMeetRespDto = timeService.mapUserMeetRespDto(user);

	return userMeetRespDto;
    }

    @GetMapping("/MostLeast")
    public MostLeastTimeRespDto getMostLeastTime(HttpServletRequest request) {

	// 세션에서 유저찾기
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute("user");

	MostLeastTimeRespDto MostLeastTimeRespDto = timeService.getMostLeastTime(user.getMeetId());
	return MostLeastTimeRespDto;
    }
}
