package com.momo.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.domain.User;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.MeetInfoDto;
import com.momo.server.service.TimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/time")
public class TimeController {
	
	@Autowired
    private TimeService timeService;

    @PutMapping
    public ResponseEntity<String> updateUsertime(HttpServletRequest request, @RequestBody UserTimeUpdateRequestDto requestDto) {
		//세션에서 유저이름찾기
		HttpSession session = request.getSession();
		User user=(User) session.getAttribute("user");

    	timeService.updateUsertime(user, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    };

    
    
    @GetMapping
    public MeetInfoDto getCommonTime(@RequestParam String meetid){

    	MeetInfoDto meetInfoDto = new MeetInfoDto();

    	//날짜에 색깔 들어가게 하기 위한 연산
    	meetInfoDto.setColorDate(timeService.getColorDate(meetid));
    	return meetInfoDto;
    }
    
    
}


    
