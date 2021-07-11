package com.momo.server.controller;

import com.momo.server.domain.User;
import com.momo.server.dto.CmRespDto;
import com.momo.server.dto.request.LoginRequestDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.service.UserService;
import com.momo.server.utils.Aes128;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    @Value("${aesKey}")
    private String key;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println(loginRequest);
        User requestUser = new User();
        requestUser.setUsername(loginRequest.getUsername());
        requestUser.setMeetId(loginRequest.getMeetId());
        User user = userService.login(requestUser);
        ResponseEntity<?> responseCode;

        if (user != null){
            request.setAttribute("authuser", user );
            responseCode = ResponseEntity.status(HttpStatus.CREATED).build();
            Aes128 aes128 = new Aes128(key);
            String enc = aes128.encrypt(user.getUserId().toString());
            Cookie authCookie = new Cookie("authuser", enc);
            response.addCookie(authCookie);

        }else{
            responseCode = ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        UserMeetRespDto userMeetRespDto = new UserMeetRespDto();
        userMeetRespDto.setMeetId(user.getMeetId());
        //planlist와 colordate 입력해야함
        

        return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 성공", userMeetRespDto), HttpStatus.OK);
    }
}