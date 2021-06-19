package com.momo.server.service;

import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MeetService {


    private final MeetRepository meetRepository;

    //meetService에 userService있는게 조금 찝찝하긴함
    private final UserService userService;

    ArrayList<Integer> users;
    int[][] times;
    ArrayList username;

    public ArrayList<Integer> findUsers(){
        users = meetRepository.findUsers();
        return meetRepository.findUsers();
    };

    public int[][] findTimes(){
        times = meetRepository.findTimes();
        return meetRepository.findTimes();
    };

    public ArrayList findUsername(){
        username = userService.findUsername();
        return userService.findUsername();
    };

    //약속 생성메소드
    public ResponseEntity<?> createMeet(MeetSaveRequestDto requestDto, String hashedUrl) {
        LocalDateTime startDate = requestDto.getDates().get(0);
        LocalDateTime endDate = requestDto.getDates().get(1);

        ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>();
        LocalDateTime curDate = startDate;

        while (!curDate.equals(endDate.plusDays(1))) {
            dates.add(curDate);
            curDate=curDate.plusDays(1);
        }

        requestDto.setDates(dates);

        String start = requestDto.getStart().split(":")[0];
        String end = requestDto.getEnd().split(":")[0];

        int col = Integer.parseInt(end) - Integer.parseInt(start);
        col = (int)(60 / requestDto.getGap()) * col;
//        int[] checkArray = new int[col];
//        requestDto.setCheckArray(checkArray);
        requestDto.setCreated(LocalDateTime.now().plusHours(9));

        requestDto.setMeetId(hashedUrl);
        //requestDto.setMeetSubInfo(new MeetSub(dates));

        meetRepository.createMeet(requestDto);
        return ResponseEntity.ok().build();
    }

    //최대가능순 연산
    public String[] getMaxTime() {
        //users와 times 가지고 연산
        //users와 Username으로 가져온 이름을 매핑시킴
        return new String[0];
    }

    //최소가능순 연산
    public String[] getMinTime() {
        //users와 times 가지고 연산

        //users와 findUsername으로 가져온 이름을 매핑시킴
        return new String[0];
    }

    //14,15,16 날짜에 색깔 들어가게 하기 위한 연산
    public String[] getColorofDate() {

        return new String[0];
    }

    //'누구랑, 언제, 어디서' 생성하기 위한 메소드
    public ResponseEntity<?> createMeetSub() {

        return ResponseEntity.ok().build();
    }

    //유저가 타임테이블 저장할때 약속의 타임테이블도 갱신하기 위한 메소드
    public ResponseEntity<?> updateMeetTimetable() {

        return ResponseEntity.ok().build();
    }

    public void addUser(String meetId, int userId){

    }

}


