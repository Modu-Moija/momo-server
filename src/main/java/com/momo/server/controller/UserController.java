package com.momo.server.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult bindingResult,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	User userEntity = userService.login(loginRequestDto);// 추후에 로직 수정해야함
	ResponseEntity<?> responseCode;

	HttpSession session = request.getSession();
	session.setAttribute("user", loginRequestDto);

	request.setAttribute("authuser", loginRequestDto);
	Aes128 aes128 = new Aes128(key);
	String enc = aes128.encrypt(loginRequestDto.getUsername().toString());// 유저이름으로 암호화시켜도 안전한지 모르겠습니다
	System.out.println(enc);
	Cookie authCookie = new Cookie("authuser", enc);
	response.addCookie(authCookie);

	// 유저 존재하지않음(신규유저)
	if (userEntity == null) {
	    userEntity = userService.createUser(loginRequestDto);

	    responseCode = ResponseEntity.status(HttpStatus.CREATED).build();
	    return new ResponseEntity<>(new CmRespDto<>(responseCode, "신규 유저 로그인 성공", null), HttpStatus.CREATED);

	} else {// 유저 존재(기존 유저)

	    responseCode = ResponseEntity.status(HttpStatus.OK).build();
	    return new ResponseEntity<>(new CmRespDto<>(responseCode, "기존 유저 로그인 성공", null), HttpStatus.OK);
	}
    }

}