package com.momo.server.controller;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.service.TimeService;
import com.momo.server.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/time")
public class TimeController {
	
    private final TimeService timeService;

    @PutMapping
    public ResponseEntity<String> updateUsertime(HttpServletRequest request, @RequestBody UserTimeUpdateRequestDto requestDto) {//@CookieValue(value="gender", required=false) Cookie genderCookie
    	
    	
		//세션에서 유저이름찾기
		HttpSession session = request.getSession();
		User user=(User)session.getAttribute("user");
		
		//System.out.println(user);
    	timeService.updateUsertime(user, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    };
    
    
    @GetMapping("/usertime")
    public UserMeetRespDto getUserTime(HttpServletRequest request){
    
    	//세션에서 유저찾기
		HttpSession session = request.getSession();
		User user=(User)session.getAttribute("user");
        
        UserMeetRespDto userMeetRespDto = timeService.mapUserMeetRespDto(user);
    	
		return userMeetRespDto;
    }
}


    
