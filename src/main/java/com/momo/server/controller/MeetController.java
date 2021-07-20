package com.momo.server.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;
import com.momo.server.dto.CmRespDto;
import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.dto.response.MeetInfoRespDto;
import com.momo.server.service.MeetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/meet")
public class MeetController {

    private final MeetService meetService;

    @PostMapping
    public ResponseEntity<?> createMeet(@RequestBody MeetSaveRequestDto requestDto) {

	String hashedUrl = Hashing.sha256().hashString(requestDto.toString(), StandardCharsets.UTF_8).toString()
		.substring(0, 15);
	meetService.createMeet(requestDto, hashedUrl);

	ResponseEntity<?> responseCode = ResponseEntity.status(HttpStatus.CREATED).build();
	return new ResponseEntity<>(new CmRespDto<>(responseCode, "약속 생성 성공", hashedUrl), HttpStatus.CREATED);
    };

    @GetMapping("/{meetId}")
    public ResponseEntity<?> getMeetInfo(@PathVariable("meetId") String meetId) {

	MeetInfoRespDto meetInfoRespDto = meetService.getMeetInfo(meetId);

	ResponseEntity<?> responseCode = ResponseEntity.status(HttpStatus.OK).build();
	return new ResponseEntity<>(new CmRespDto<>(responseCode, "약속조회 성공", meetInfoRespDto), HttpStatus.OK);
    }

}
