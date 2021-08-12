package com.momo.server.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.momo.server.dto.auth.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.momo.server.service.UserService;
import com.momo.server.utils.Aes128;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final HttpSession httpSession;

    @Value("${aesKey}")
    private String key;

    @Autowired
	public UserController(UserService userService, HttpSession httpSession) {
		this.userService = userService;
		this.httpSession = httpSession;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	User userEntity = userService.login(loginRequestDto);
	ResponseEntity<?> responseCode;

	request.setAttribute("authuser", loginRequestDto);
	Aes128 aes128 = new Aes128(key);
	String enc = aes128.encrypt(userEntity.getUserId().toString());// 유저이름으로 암호화시켜도 안전한지 모르겠습니다
	Cookie authCookie = new Cookie("authuser", enc);
	authCookie.setHttpOnly(false);
	response.addCookie(authCookie);

	httpSession.setAttribute("sessionuser", new SessionUser(userEntity));

	responseCode = ResponseEntity.status(HttpStatus.OK).build();
	return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 성공", userEntity), HttpStatus.OK);

    }

}