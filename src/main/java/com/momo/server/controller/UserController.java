package com.momo.server.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.User;
import com.momo.server.dto.CmRespDto;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.service.TimeService;
import com.momo.server.service.UserService;
import com.momo.server.utils.Aes128;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final TimeService timeService;

    @Value("${aesKey}")
    private String key;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	User requestUser = new User();
	requestUser.setUsername(loginRequest.getUsername());
	requestUser.setMeetId(loginRequest.getMeetId());
	User user = userService.login(requestUser);
	ResponseEntity<?> responseCode;

	if (user != null) {

	    // 세션에 정보저장, 일단 세션으로 구현해놨습니다.. 추후에 쿠키로 수정할수있습니다.
	    HttpSession session = request.getSession();
	    session.setAttribute("user", user);

	    request.setAttribute("authuser", user);
	    responseCode = ResponseEntity.status(HttpStatus.CREATED).build();
	    Aes128 aes128 = new Aes128(key);
	    String enc = aes128.encrypt(user.getUserId().toString());
	    Cookie authCookie = new Cookie("authuser", enc);
	    response.addCookie(authCookie);

	    return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 성공", null), HttpStatus.OK);
	} else {
	    responseCode = ResponseEntity.status(HttpStatus.CONFLICT).build();
	    return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 실패", null), HttpStatus.CONFLICT);
	}
    }

}