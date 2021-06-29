package com.momo.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.momo.server.dto.request.MeetSaveRequestDto;
import com.momo.server.repository.MeetRepository;

import lombok.RequiredArgsConstructor;

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

//    public ArrayList findUsername(){
//        username = userService.findUsername();
//        return userService.findUsername();
//    };

    //약속 생성메소드
    public ResponseEntity<?> createMeet(MeetSaveRequestDto requestDto, String hashedUrl) {
    	
    	System.out.println(requestDto);
    	
        LocalDate startDate = requestDto.getDates().get(0);
        LocalDate endDate = requestDto.getDates().get(1);

        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        LocalDate curDate = startDate;

        while (!curDate.equals(endDate.plusDays(1))) {
            dates.add(curDate);
            curDate=curDate.plusDays(1);
        }
        requestDto.setDates(dates);

//        int[] checkArray = new int[col];
//        requestDto.setCheckArray(checkArray);
        requestDto.setCreated(LocalDateTime.now().plusHours(9));
        requestDto.setMeetId(hashedUrl);
        //requestDto.setMeetSubInfo(new MeetSub(dates));

        
        //meet의 times 이차원 배열 row 계산
        String start = requestDto.getStart().split(":")[0];
        String end = requestDto.getEnd().split(":")[0];

        int row = Integer.parseInt(end) - Integer.parseInt(start);
        row = (int)(60 / requestDto.getGap()) * row;
        
        
    	//meet의 times 이차원 배열 col 계산
        int col = dates.size();
        
        //meet의 times 이차원배열 0으로 채우기
        int[][] temptimes = new int[row][col];
        
        for (int i = 0; i < row; i++) { // 행 반복
			for (int j = 0; j < col; j++) { // 열 반복
				temptimes[i][j] = 0;
			}
		}
        
        //추후에 성능개선을 위해 캐시프랜들리 코드 적용 생각해보면 좋을 것 같음(행과 열 위치 연산개선)
        //참조링크 https://hot2364928.tistory.com/58
        
//        for (int i = 0; i < row; i++) { // 행 반복
//			for (int j = 0; j < col; j++) { // 열 반복
//				System.out.println(i+"행 "+j+"열의 값:"+temptimes[i][j]);
//			}
//		}
        
        requestDto.setTimes(temptimes);
        meetRepository.createMeet(requestDto.toEntity());
        
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

}


