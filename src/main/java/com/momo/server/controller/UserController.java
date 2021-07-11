package com.momo.server.controller;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.service.MeetService;
import com.momo.server.service.TimeService;
import com.momo.server.service.UserService;
import com.momo.server.utils.Aes128;
import com.momo.server.utils.DateTimeDto;

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
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //System.out.println(loginRequest);
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
            
    		UserMeetRespDto userMeetRespDto = new UserMeetRespDto();
    		ArrayList<DateTimeDto> planList = userService.mapPlanList(user);
    		userMeetRespDto.setMeetId(user.getMeetId());
            userMeetRespDto.setPlanList(planList);
            userMeetRespDto.setColorDate(timeService.getColorDate(key));
            
            return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 성공", userMeetRespDto), HttpStatus.OK);

        }else{
            responseCode = ResponseEntity.status(HttpStatus.CONFLICT).build();
            return new ResponseEntity<>(new CmRespDto<>(responseCode, "유저 로그인 실패", null), HttpStatus.OK);
        }
        


        
    }
}