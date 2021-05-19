package com.momo.server.service;

import com.momo.server.domain.User;
import com.momo.server.repository.MeetRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;

    @Autowired//meetService에 userService있는게 조금 찝찝하긴함
    private UserService userService;

    ArrayList<Integer> users = meetRepository.findUsers();
    int[][] times = meetRepository.findTimes();

    ArrayList Usernames = userService.findUsername();

    //약속 생성메소드
    public ResponseEntity<?> createMeet() {
        meetRepository.createMeet();
        return ResponseEntity.ok().build();
    }

    //최대가능순 연산
    public String[] getMaxTime() {
        //users와 times 가지고 연산
        return new String[0];

        //users와 findUsername으로 가져온 이름을 매핑시킴
    }

    //최소가능순 연산
    public String[] getMinTime() {
        //users와 times 가지고 연산
        return new String[0];

        //users와 findUsername으로 가져온 이름을 매핑시킴
    }

    //14,15,16 날짜에 색깔 들어가게 하기 위한 연산
    public String[] getColorofDate() {

        return new String[0];
    }

    //누구랑, 언제, 어디서 생성하기 위한 메소드
    public ResponseEntity<?> createMeetSub() {

        return ResponseEntity.ok().build();
    }

    //유저가 타임테이블 저장할때 약속의 타임테이블도 갱신하기 위한 메소드
    public ResponseEntity<?> updateMeetTimetable() {

        return ResponseEntity.ok().build();
    }

}
