package com.momo.server.service;

import com.momo.server.repository.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;


    //약속 생성메소드
    public ResponseEntity<?> createMeet() {
        meetRepository.createMeet();
        return ResponseEntity.ok().build();
    }

    //최대가능순 연산
    public String[] getMaxTime() {
        return new String[0];
    }

    //최소가능순 연산
    public String[] getMinTime() {
        return new String[0];
    }

    //14,15,16 날짜에 색깔 들어가게 하기 위한 연산
    public String[] getColorofDate() {
        return new String[0];
    }

    //누구랑, 언제, 어디서 생성하기 위한 연산
    public ResponseEntity<?> createMeetSub() {
        return ResponseEntity.ok().build();
    }

}
