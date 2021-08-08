package com.momo.server.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.momo.server.config.auth.CheckSessionUser;
import com.momo.server.dto.auth.SessionUser;
import com.momo.server.dto.response.MostLeastRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.server.dto.CmRespDto;
import com.momo.server.dto.request.UserTimeUpdateRequestDto;
import com.momo.server.dto.response.UserMeetRespDto;
import com.momo.server.exception.auth.UnauthorizedException;
import com.momo.server.service.TimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/time")
public class TimeController {

    private final TimeService timeService;

    @PutMapping
    public ResponseEntity<?> updateUsertime(HttpServletRequest request,
											@RequestBody @Valid UserTimeUpdateRequestDto requestDto,
											BindingResult bindingResult, @CheckSessionUser SessionUser user) {

	timeService.updateUsertime(user, requestDto);
	ResponseEntity<?> responseCode = new ResponseEntity<>(HttpStatus.OK);
	return new ResponseEntity<>(new CmRespDto<>(responseCode, "시간 수정 성공", null), HttpStatus.OK);
    };

    @GetMapping("/usertime")
    public UserMeetRespDto getUserTime(HttpServletRequest request, @CheckSessionUser SessionUser user) {

	UserMeetRespDto userMeetRespDto = timeService.mapUserMeetRespDto(user);

	return userMeetRespDto;
    }


    @GetMapping("/{meetId}/mostleast")
    public MostLeastRespDto getMostLeastTime(@PathVariable("meetId") String meetId) {

	MostLeastRespDto mostLeastRespDto = timeService.getMostLeastTime(meetId);
	return mostLeastRespDto;
    }

    @GetMapping("/2d")
    public ResponseEntity<?> get2DTimetable(){
        int[][] data = {{2, 0, 0, 0}, {2,0,0,0}, {0,1,0,1}, {0,2,2,1}, {2,0,0,1}, {2,1,1,0},{2,1,1,3}};
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/array")
    public ResponseEntity<?> getArrayTimetable(){
        int[] data = {128, 128, 17, 41, 129, 148, 151};
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
